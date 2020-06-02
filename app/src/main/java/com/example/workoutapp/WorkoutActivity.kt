package com.example.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_workout.*
import kotlinx.android.synthetic.main.dialog_exit_workout.*
import java.util.*

class WorkoutActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer: CountDownTimer? = null
    private var workoutTimer: CountDownTimer? = null
    private var selectedOption: Int = -1
    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var textToSpeech: TextToSpeech? = null
    private var startSound: MediaPlayer? = null
    private var startString = "Get ready for the exercise"
    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        setSupportActionBar(toolbar_exercise_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ll_workout_view.visibility = View.GONE
        ll_rest_view.visibility = View.VISIBLE
        toolbar_exercise_activity.setNavigationOnClickListener {
            onBackPressed()
        }
        startSound = MediaPlayer.create(this, R.raw.whistle_sound)
        exerciseList = WorkoutExercise.getExerciseList()
        textToSpeech = TextToSpeech(this, this)
        speakUp(startString)
        speakUp(upcoming_exercise_text.text.toString())

        setupRestCountdownTimer()   //setting up the rest countdown Timer
        exerciseStatusAdapterSetRecyclerView()


    }

    override fun onDestroy() {
        restTimer?.cancel()
        workoutTimer?.cancel()
        textToSpeech?.shutdown()
        textToSpeech?.stop()
        startSound?.stop()
        super.onDestroy()
    }

    private fun setupRestCountdownTimer() {
        try {
            restTimer?.cancel()

            ll_rest_view.visibility = View.VISIBLE
            ll_workout_view.visibility = View.GONE

            selectedOption++
            upcoming_exercise_text.text = exerciseList!![selectedOption].name

            progressBar_workout.max = 10
            if (selectedOption != 0)
                startSound!!.start()
            Handler().postDelayed({
                speakUp(startString)
                speakUp(upcoming_exercise_text.text.toString())

            }, 2000)



            restCountdownTimer()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun setupWorkoutCountdownTimer() {
        try {
            progressBar_workout.max = 30
            workoutTimer?.cancel()
            ll_rest_view.visibility = View.GONE
            ll_workout_view.visibility = View.VISIBLE
            workoutCountdownTimer()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun restCountdownTimer() {


        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar_rest.progress = (millisUntilFinished / 1000).toInt()
                rest_timer.text = (millisUntilFinished / 1000).toString()

            }

            override fun onFinish() {
                exercise_image.setImageResource(exerciseList!![selectedOption].image)
                exercise_name.text = exerciseList!![selectedOption].name
                speakUp("Start")
                speakUp(exercise_name.text.toString())
                exerciseList!![selectedOption].isSelected = true
                exerciseAdapter!!.notifyDataSetChanged()

                setupWorkoutCountdownTimer()

            }
        }.start()
    }

    private fun workoutCountdownTimer() {

        workoutTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar_workout.progress = (millisUntilFinished / 1000).toInt()
                workout_timer.text = ((millisUntilFinished / 1000) + 1).toString()


                if (millisUntilFinished <= 3000)
                    speakUp(workout_timer.text.toString())

            }

            override fun onFinish() {
                exerciseList!![selectedOption].isSelected = false
                exerciseList!![selectedOption].isCompleted = true
                exerciseAdapter!!.notifyDataSetChanged()
                if (selectedOption < exerciseList!!.size - 1) {
                    setupRestCountdownTimer()

                } else {

                    Toast.makeText(
                        this@WorkoutActivity,
                        "Exercise Completed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    val intent = Intent(this@WorkoutActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()

    }

    private fun speakUp(text: String) {
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_ADD, null, "")
    }

    private fun customExitWorkoutDialog() {
        val exitDialog = Dialog(this)
        exitDialog.setContentView(R.layout.dialog_exit_workout)
        exitDialog.setTitle("Exit Workout Dialog")
        val noButton = exitDialog.btn_NO
        noButton.setOnClickListener {
            exitDialog.dismiss()
        }
        val yesButton = exitDialog.btn_YES
        yesButton.setOnClickListener {

            super.onBackPressed()
            exitDialog.dismiss()
        }
        exitDialog.show()


    }

    private fun exerciseStatusAdapterSetRecyclerView() {
        recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(this, exerciseList!!)
        recycler_view.adapter = exerciseAdapter
    }

    override fun onBackPressed() {
        customExitWorkoutDialog()

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA)
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show()
        }
    }

}
