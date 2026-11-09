package com.example.tp_9___composant

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var workoutManager: WorkoutManager
    private lateinit var tvDuration: TextView
    private lateinit var tvDistance: TextView
    private lateinit var tvCalories: TextView
    private lateinit var tvStatus: TextView

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val grantedCount = permissions.entries.count { it.value }
        tvStatus.text = "Permissions accordées : $grantedCount / ${permissions.size}"
        Toast.makeText(this, "Permissions mises à jour", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        workoutManager = WorkoutManager()

        tvDuration = findViewById(R.id.tvDuration)
        tvDistance = findViewById(R.id.tvDistance)
        tvCalories = findViewById(R.id.tvCalories)
        tvStatus = findViewById(R.id.tvStatus)

        val btnStartWorkout = findViewById<Button>(R.id.btnStartWorkout)
        val btnStopWorkout = findViewById<Button>(R.id.btnStopWorkout)
        val btnSyncWork = findViewById<Button>(R.id.btnSyncWork)
        val btnRequestPermissions = findViewById<Button>(R.id.btnRequestPermissions)
        val btnShowQa = findViewById<Button>(R.id.btnShowQa)

        btnStartWorkout.setOnClickListener {
            workoutManager.startWorkout()
            tvStatus.text = "Statut : Entraînement en cours..."
            Toast.makeText(this, "Entraînement démarré", Toast.LENGTH_SHORT).show()
        }

        btnStopWorkout.setOnClickListener {
            workoutManager.stopWorkout()
            tvStatus.text = "Statut : Entraînement arrêté."
            Toast.makeText(this, "Entraînement arrêté", Toast.LENGTH_SHORT).show()
        }

        btnSyncWork.setOnClickListener {
            val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
            WorkManager.getInstance(this).enqueue(syncRequest)
            tvStatus.text = "Statut : Tâche WorkManager en arrière-plan lancée."
            Toast.makeText(this, "Sync Worker enfilé", Toast.LENGTH_SHORT).show()
        }

        btnRequestPermissions.setOnClickListener {
            checkAndRequestPermissions()
        }

        btnShowQa.setOnClickListener {
            showExercice2Dialog()
        }

        observeWorkout()
    }

    private fun observeWorkout() {
        lifecycleScope.launch {
            launch {
                workoutManager.durationSeconds.collectLatest { secs ->
                    val mins = secs / 60
                    val remainingSecs = secs % 60
                    tvDuration.text = String.format(Locale.getDefault(), "Durée:\n%02d:%02d", mins, remainingSecs)
                }
            }
            launch {
                workoutManager.distanceMeters.collectLatest { meters ->
                    tvDistance.text = String.format(Locale.getDefault(), "Distance:\n%.1f m", meters)
                }
            }
            launch {
                workoutManager.caloriesBurned.collectLatest { cals ->
                    tvCalories.text = String.format(Locale.getDefault(), "Calories:\n%.1f kcal", cals)
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionsList = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionsList.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissionsList.add(Manifest.permission.BLUETOOTH_SCAN)
        }

        val notGranted = permissionsList.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Permissions requises")
                .setMessage("Cette application a besoin des permissions de localisation, Bluetooth et contacts pour le suivi des entraînements et des fonctionnalités connectées.")
                .setPositiveButton("Autoriser") { _, _ ->
                    permissionLauncher.launch(notGranted.toTypedArray())
                }
                .setNegativeButton("Annuler", null)
                .show()
        } else {
            Toast.makeText(this, "Toutes les permissions sont déjà accordées !", Toast.LENGTH_SHORT).show()
            tvStatus.text = "Statut : Permissions déjà accordées."
        }
    }

    private fun showExercice2Dialog() {
        AlertDialog.Builder(this)
            .setTitle("Exercice 2 : Résumé des Q&A")
            .setMessage("Le TP comporte 40 questions théoriques et pratiques sur :\n\n1. Tâches asynchrones (Coroutines, Threads, Main Thread)\n2. WorkManager & JobScheduler\n3. Manifest.xml & Permissions (Install-time vs Runtime)\n4. Localisation, Réseau, Bluetooth & Contacts\n5. Bibliothèque EasyPermissions\n\nConsultez l'artefact 'Exercice2Answers.artifact.md' pour les réponses détaillées à l'ensemble des 40 questions.")
            .setPositiveButton("Fermer", null)
            .show()
    }
}
