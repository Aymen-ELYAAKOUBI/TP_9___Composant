# Implementation Plan - TP 9: Composant Android (WorkManager, Coroutines, Permissions)

Implement the practical work (TP 9) covering physical activity tracking, background tasks with WorkManager, JobScheduler concepts, runtime permissions (Location, Bluetooth, Network, Contacts), and answering the 40 theoretical/practical questions in Exercice 2.

## User Review Required

> [!IMPORTANT]
> This TP covers both the practical Android application (Exercice 1: Workout tracker, WorkManager sync, runtime permissions) and the comprehensive answers to all 40 questions of Exercice 2.

## Proposed Changes

### Dependencies & Manifest
- **[MODIFY]** [build.gradle.kts](file:///C:/Users/user/AndroidStudioProjects/TP_9___Composant/app/build.gradle.kts): Add WorkManager dependency (`androidx.work:work-runtime-ktx`).
- **[MODIFY]** [AndroidManifest.xml](file:///C:/Users/user/AndroidStudioProjects/TP_9___Composant/app/src/main/AndroidManifest.xml): Add required permissions (Location, Bluetooth, Contacts, Network).

### Background & Worker Components
- **[NEW]** [SyncWorker.kt](file:///C:/Users/user/AndroidStudioProjects/TP_9___Composant/app/src/main/java/com/example/tp_9___composant/SyncWorker.kt): WorkManager worker for background data synchronization.
- **[NEW]** [WorkoutManager.kt](file:///C:/Users/user/AndroidStudioProjects/TP_9___Composant/app/src/main/java/com/example/tp_9___composant/WorkoutManager.kt): Coroutines-based workout session tracker.

### UI & Activities
- **[MODIFY]** [MainActivity.kt](file:///C:/Users/user/AndroidStudioProjects/TP_9___Composant/app/src/main/java/com/example/tp_9___composant/MainActivity.kt): Dashboard UI with workout controls, permission request flows, WorkManager trigger, and Q&A viewer for Exercice 2.
- **[MODIFY]** [activity_main.xml](file:///C:/Users/user/AndroidStudioProjects/TP_9___Composant/app/src/main/res/layout/activity_main.xml): Clean Material layout for the dashboard.

### Documentation & Answers
- **[NEW]** [Exercice2Answers.md](file:///C:/Users/user/AndroidStudioProjects/TP_9___Composant/.artifacts/6c0d6694-0ecb-4648-958f-86f6efce6e14/Exercice2Answers.artifact.md): Detailed answers to all 40 questions of Exercice 2.

## Verification Plan

### Automated Tests
- Run unit tests to verify build and worker logic (`app:testDebugUnitTest`).

### Manual Verification
- Build and deploy the app to verify permissions handling, workout tracking, and WorkManager sync tasks.
