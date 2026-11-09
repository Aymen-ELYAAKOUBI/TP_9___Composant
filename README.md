# TP 9 : Composant Android (M205)

**Filière :** Développement digital option mobile 
**Niveau :** 2ème année  
**Institution :** CMC Rabat  

---

## 📋 Table des Matières
1. [Présentation du Projet (Exercice 1)](#-présentation-du-projet-exercice-1)
2. [Architecture et Composants de l'Application](#-architecture-et-composants-de-lapplication)
3. [Guide des Permissions Android](#-guide-des-permissions-android)
4. [Exercice 2 : Réponses aux 40 Questions (Q&A)](#-exercice-2--réponses-aux-40-questions-qa)

---

## 🏃‍♂️ Présentation du Projet (Exercice 1)

L'application mobile développée dans le cadre de ce TP permet de :
- **Suivi des Entraînements** : Démarrer, suivre et arrêter une session d'activité physique (durée, distance, calories) en utilisant les **Coroutines Kotlin** de manière non bloquante.
- **Tâches en Arrière-plan (WorkManager)** : Exécuter des synchronisations périodiques ou ponctuelles de données avec un serveur en arrière-plan (`SyncWorker`), même si l'application est fermée.
- **Gestion des Permissions** : Demander dynamiquement les permissions sensibles (Localisation GPS, Bluetooth, Contacts) avec un workflow de justification (Rationale).

---

## 🛠️ Architecture et Composants de l'Application

### 1. WorkManager (`SyncWorker.kt`)
Gère l'exécution garantie de tâches en arrière-plan :
```kotlin
class SyncWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        // Synchronisation des données d'entraînement
        return Result.success()
    }
}
```

### 2. Gestionnaire d'Entraînement (`WorkoutManager.kt`)
Utilise `kotlinx.coroutines` et `StateFlow` pour mettre à jour l'UI en temps réel sans bloquer le thread principal :
```kotlin
class WorkoutManager {
    private val _isTracking = MutableStateFlow(false)
    val durationSeconds: StateFlow<Int> = _durationSeconds.asStateFlow()
    // ...
}
```

### 3. Déclaration des Permissions (`AndroidManifest.xml`)
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

---

## 📚 Exercice 2 : Réponses aux 40 Questions (Q&A)

### 1. Découverte des tâches asynchrones
1. **Quelles sont les différentes manières de gérer des tâches asynchrones dans une application Android ?**
   - Threads Java standard (`Thread`, `Runnable`), `AsyncTask` (déprécié), `Handler`/`Looper`, `ExecutorService`, **Coroutines Kotlin**, et RxJava/Flow.
2. **Comment utiliser les Coroutines Kotlin pour effectuer des tâches en arrière-plan sans bloquer l'interface utilisateur ?**
   - En utilisant le mot-clé `suspend` et en lançant la coroutine sur un Dispatcher adapté (`Dispatchers.IO`) via un scope (`viewModelScope`).
3. **Qu'est-ce que le problème du "Main Thread" en Android, et comment les tâches asynchrones peuvent-elles le résoudre ?**
   - Le Main Thread gère le rendu graphique. S'il est bloqué par une opération longue, l'app se fige (erreur **ANR**). Les tâches asynchrones déplacent ces opérations vers des threads secondaires.
4. **Quelle est la différence entre les Threads et les Coroutines en Kotlin ?**
   - Les Threads sont gérés par l'OS et sont lourds. Les Coroutines sont des "threads légers" gérés par Kotlin, capables de se suspendre sans bloquer de thread système.

---

### 2. Utilisation du WorkManager
5. **Qu'est-ce que le WorkManager en Android et dans quels cas est-il recommandé ?**
   - Une bibliothèque Jetpack pour les tâches en arrière-plan **persistantes** (survivent au redémarrage). Recommandé pour la synchronisation de données et les sauvegardes garanties.
6. **Comment créer et configurer une tâche avec WorkManager pour une exécution immédiate (`OneTimeWorkRequest`) ?**
   - `val request = OneTimeWorkRequestBuilder<SyncWorker>().build(); WorkManager.getInstance(context).enqueue(request)`
7. **Comment configurer une tâche qui s'exécute périodiquement (`PeriodicWorkRequest`) ?**
   - `PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES).build()`
8. **Quelles sont les contraintes que l'on peut définir (réseau, batterie) ?**
   - `Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).setRequiresCharging(true).build()`

---

### 3. Implémentation de JobScheduler
9. **Quelle est la différence entre WorkManager et JobScheduler ?**
   - `JobScheduler` est une API native (API 21+) complexe et non unifiée. `WorkManager` est la surcouche moderne recommandée qui englobe `JobScheduler` avec une rétrocompatibilité transparente.
10. **Comment utiliser JobScheduler pour planifier une tâche sur réseau Wi-Fi ?**
    - Via `JobInfo.Builder` avec `setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)`.
11. **Comment gérer les priorités et conditions avec JobScheduler ?**
    - En configurant les paramètres de `JobInfo` (`setRequiresCharging`, `setRequiredNetworkType`, etc.).

---

### 4. Manipulation des Threads avec Kotlin
12. **Comment créer et gérer des Threads en Kotlin ?**
    - `Thread { /* code */ }.start()`
13. **Quelle est l'importance des Coroutines Kotlin pour remplacer les Threads ?**
    - Code plus propre, pas de "callback hell", gestion propre du cycle de vie et économie des ressources système.
14. **Comment synchroniser les données entre threads sans causer de concurrence ?**
    - Utilisation de `Mutex`, de structures thread-safe, ou de `StateFlow` confinés au thread principal.
15. **Que sont les Dispatchers dans Kotlin ?**
    - `Dispatchers.Main` (UI), `Dispatchers.IO` (Réseau/Disque), `Dispatchers.Default` (Calculs lourds).

---

### 5. Présentation du fichier Manifest.xml
16. **Quel est le rôle du fichier AndroidManifest.xml ?**
    - Décrire l'application au système (composants, activités, services, permissions, niveau d'API).
17. **Comment déclarer des permissions dans le Manifest ?**
    - `<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />`
18. **Que sont les permissions d'installation vs d'exécution (runtime) ?**
    - *Install-time* : accordées à l'installation (ex: Internet). *Runtime* : demandées dynamiquement (ex: Localisation).

---

### 6 & 7. Permissions d'installation et Runtime permissions
19. **Quelles permissions sont demandées à l'installation ?**
    - Les permissions normales (ex: `INTERNET`, `ACCESS_NETWORK_STATE`).
20. **Comment déclarer la localisation et le réseau dans le Manifest ?**
    - `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `INTERNET`.
21. **Pourquoi les runtime permissions doivent-elles être demandées après l'installation ?**
    - Pour protéger la vie privée de l'utilisateur (accès aux données sensibles comme le GPS ou les contacts).
22. **Comment demander des permissions d'exécution ?**
    - Via `ActivityResultContracts.RequestPermission()` ou `RequestMultiplePermissions()`.
23. **Différence entre permission normale et dangereuse ?**
    - *Normale* : risque minime, accordée automatiquement. *Dangereuse* : accès aux données personnelles, nécessite l'accord explicite de l'utilisateur.
24. **Meilleures pratiques en cas de refus de permission ?**
    - Expliquer l'utilité (Rationale), proposer une alternative, et éviter de bloquer l'application.

---

### 8 & 9. Workflow et Localisation
25. **Comment implémenter un workflow de demande de permissions ?**
    - Vérifier si accordée -> Afficher une explication si nécessaire -> Demander la permission -> Gérer le résultat.
26. **Qu'est-ce qu'un rationale (raison) ?**
    - Un message explicatif affiché à l'utilisateur avant la popup système s'il a déjà refusé une fois.
27. **Comment demander la localisation en respectant la confidentialité ?**
    - Ne la demander qu'en cas de besoin réel (ex: pendant le sport) et privilégier `ACCESS_COARSE_LOCATION` si la précision GPS exacte n'est pas requise.
28. **Comment gérer localisation fine (GPS) et approximative (réseau) ?**
    - En déclarant et demandant les deux selon le besoin précis de l'application.
29. **Limitations de la localisation en arrière-plan ?**
    - Sur Android 10+, nécessite une permission spécifique (`ACCESS_BACKGROUND_LOCATION`) demandée séparément.

---

### 10, 11 & 12. Réseau, Contacts et Bluetooth
30. **Comment déclarer la permission d'accès au réseau ?**
    - `ACCESS_NETWORK_STATE` et `INTERNET` (permissions normales).
31. **Meilleures pratiques pour vérifier la connectivité ?**
    - Utiliser `ConnectivityManager` et gérer les erreurs réseau via `try-catch`.
32. **Étapes pour utiliser l'accès aux contacts ?**
    - Déclarer `READ_CONTACTS` dans le Manifest, vérifier l'état, demander la permission, et interroger le `ContentResolver`.
33. **Règles de confidentialité pour les contacts ?**
    - N'accéder qu'aux champs strictement nécessaires et ne jamais stocker/transmettre sans consentement.
34. **Permissions Bluetooth nécessaires ?**
    - Android 12+ : `BLUETOOTH_CONNECT` et `BLUETOOTH_SCAN`. Versions antérieures : `BLUETOOTH` et `BLUETOOTH_ADMIN`.
35. **Comment vérifier l'état du Bluetooth ?**
    - Via `BluetoothAdapter` après vérification des permissions de connexion.

---

### 13 & 14. Bibliothèques tierces et EasyPermissions
36. **Principales bibliothèques de permissions ?**
    - EasyPermissions, PermissionsDispatcher, Dexter.
37. **Avantages d'utiliser EasyPermissions ?**
    - Réduction du code répétitif (boilerplate), gestion centralisée des rationales et des callbacks.
38. **Comment intégrer EasyPermissions ?**
    - `implementation("pub.devrel:easypermissions:3.0.0")`
39. **Comment l'utiliser pour la localisation, le Bluetooth et les contacts ?**
    - `EasyPermissions.hasPermissions(...)` et `EasyPermissions.requestPermissions(...)`.
40. **Avantages par rapport à la gestion manuelle ?**
    - Moins d'erreurs, gestion élégante des dialogues explicatifs et code plus lisible.
