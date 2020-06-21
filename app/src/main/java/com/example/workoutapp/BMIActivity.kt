package com.example.workoutapp

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bmi.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View
    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)
        setSupportActionBar(toolbar_bmi_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "CALCULATE BMI"
        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        makeMetricUnitsViewVisible()
        rb_bmi.setOnCheckedChangeListener { radiogroup: RadioGroup, checkedId: Int ->
            if (checkedId == R.id.rb_metricUnit)
                makeMetricUnitsViewVisible()
            else
                makeUSUnitsViewVisible()
        }


        btn_submit_bmi.setOnClickListener {
            try {


                if (currentVisibleView == METRIC_UNITS_VIEW) {
                    if (checkMetricInputValid()) {
                        val weightKG = et_weight_kg.text.toString().toFloat()
                        val heightM = (et_height_cm.text.toString().toFloat()) / 100
                        val bmi = weightKG / (heightM * heightM)
                        displayBMIResult(bmi)

                    } else {
                        Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (checkUsUnitValid()) {
                        val weightLbs = et_weight_kg.text.toString().toFloat()
                        val heightFeet = et_height_feet.text.toString().toFloat()
                        val heightInch =
                            et_height_inch.text.toString().toFloat() + (heightFeet * 12)
                        val bmi = 703 * weightLbs / (heightInch * heightInch)
                        displayBMIResult(bmi)
                    } else {
                        Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show()
                    }

                }
            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }

    }

    private fun makeMetricUnitsViewVisible() {
        currentVisibleView = METRIC_UNITS_VIEW
        ll_bmi_metric.visibility = View.VISIBLE
        ll_bmi_us.visibility = View.GONE
        ll_bmi_result.visibility = View.GONE
        et_height_cm.text!!.clear()
        et_weight_kg.text!!.clear()


    }

    private fun makeUSUnitsViewVisible() {
        currentVisibleView = US_UNITS_VIEW
        ll_bmi_metric.visibility = View.GONE
        ll_bmi_us.visibility = View.VISIBLE
        ll_bmi_result.visibility = View.GONE
        et_height_feet.text!!.clear()
        et_height_inch.text!!.clear()
        et_weight_lbs.text!!.clear()
    }

    private fun checkMetricInputValid(): Boolean {
        var isValid = true
        if (et_weight_kg.text.toString().isEmpty())
            isValid = false
        else if (et_height_cm.text.toString().isEmpty())
            isValid = false

        return isValid
    }


    private fun checkUsUnitValid(): Boolean {
        var isValid = true
        if (et_weight_lbs.text.toString().isEmpty())
            isValid = false
        else if (et_height_feet.text.toString().isEmpty())
            isValid = false
        else if (et_height_inch.text.toString().isEmpty())
            isValid = false

        return isValid
    }

    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        bmi_result.text = bmiValue
        bmi_result_quality.text = bmiLabel
        bmi_result_quality_message.text = bmiDescription
        ll_bmi_result.visibility = View.VISIBLE


    }


}
