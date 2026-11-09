package com.example.tp_9___composant

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorkoutManager {
    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private var workoutJob: Job? = null

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking.asStateFlow()

    private val _durationSeconds = MutableStateFlow(0)
    val durationSeconds: StateFlow<Int> = _durationSeconds.asStateFlow()

    private val _distanceMeters = MutableStateFlow(0.0)
    val distanceMeters: StateFlow<Double> = _distanceMeters.asStateFlow()

    private val _caloriesBurned = MutableStateFlow(0.0)
    val caloriesBurned: StateFlow<Double> = _caloriesBurned.asStateFlow()

    fun startWorkout() {
        if (_isTracking.value) return
        _isTracking.value = true
        _durationSeconds.value = 0
        _distanceMeters.value = 0.0
        _caloriesBurned.value = 0.0

        workoutJob = scope.launch {
            while (isActive && _isTracking.value) {
                delay(1000L)
                _durationSeconds.value += 1
                // Simulate running: ~2.5 meters per second, ~0.12 calories per second
                _distanceMeters.value += 2.5
                _caloriesBurned.value += 0.12
            }
        }
    }

    fun stopWorkout() {
        _isTracking.value = false
        workoutJob?.cancel()
        workoutJob = null
    }

    fun reset() {
        stopWorkout()
        _durationSeconds.value = 0
        _distanceMeters.value = 0.0
        _caloriesBurned.value = 0.0
    }
}
