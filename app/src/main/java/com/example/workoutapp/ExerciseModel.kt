package com.example.workoutapp

data class ExerciseModel(
    val id: Int,
    val name: String,
    val image: Int,
    var isSelected: Boolean,
    var isCompleted: Boolean
) {
}