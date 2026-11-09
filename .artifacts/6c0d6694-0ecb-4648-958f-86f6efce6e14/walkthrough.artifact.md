# Walkthrough - TP 9: Composant Android (M205)

Completed the implementation of TP 9 based on the requirements in `TP_9___Composant.pdf`.

## What Was Accomplished

### 1. Exercice 1 : Application Android de Suivi d'Activité & Tâches en Arrière-plan
- **Manifest.xml** : Configuré avec les permissions nécessaires (Internet, Réseau, Localisation Fine/Approximative, Bluetooth Connect/Scan, Contacts).
- **WorkManager (`SyncWorker.kt`)** : Implémenté pour gérer les tâches de synchronisation de données en arrière-plan.
- **Coroutines (`WorkoutManager.kt`)** : Implémenté pour suivre en temps réel les séances d'entraînement (durée, distance, calories) de manière non bloquante.
- **Interface Utilisateur (`MainActivity.kt` & `activity_main.xml`)** : Tableau de bord Material Design interactif permettant de démarrer/arrêter l'entraînement, lancer le WorkManager, demander les permissions d'exécution avec boîtes de dialogue explicatives, et consulter le résumé de l'Exercice 2.

### 2. Exercice 2 : Réponses aux 40 Questions Théoriques & Pratiques
- Créé l'artefact [Exercice2Answers.artifact.md](file:///C:/Users/user/AndroidStudioProjects/TP_9___Composant/.artifacts/6c0d6694-0ecb-4648-958f-86f6efce6e14/Exercice2Answers.artifact.md) fournissant des réponses détaillées et structurées aux 40 questions du TP (tâches asynchrones, WorkManager vs JobScheduler, permissions d'installation vs runtime, localisation, Bluetooth, contacts, et bibliothèque EasyPermissions).

## Verification Results

- **Build Status**: `:app:assembleDebug` completed successfully with zero compilation errors.
- **Unit Tests**: Verified project structure and extraction tools successfully.
