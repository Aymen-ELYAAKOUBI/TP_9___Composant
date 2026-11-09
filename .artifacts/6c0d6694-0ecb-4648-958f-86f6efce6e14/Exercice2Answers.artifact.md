# Réponses aux Questions - Exercice 2 (TP 9 : Composant Android)

Ce document fournit les réponses détaillées aux 40 questions de l'Exercice 2 du TP 9 (Module M205 - Développement Mobile - OFPPT).

---

## 1. Découverte des tâches asynchrones

### 1. Quelles sont les différentes manières de gérer des tâches asynchrones dans une application Android ?
- **Threads Java standard (`Thread`, `Runnable`)** : Approche bas niveau pour exécuter du code en arrière-plan.
- **`AsyncTask`** (déprécié) : Ancienne classe Android pour exécuter des tâches en arrière-plan et mettre à jour l'UI.
- **`Handler` et `Looper`** : Permet d'envoyer des messages ou des runnables d'un thread à un autre (notamment du thread worker vers le thread principal).
- **`ExecutorService` / `ThreadPoolExecutor`** : Gestion avancée de pools de threads.
- **Coroutines Kotlin** : Approche moderne, recommandée, basée sur des fonctions suspendues (`suspend`) pour un code asynchrone séquentiel et non bloquant.
- **RxJava / Flow** : Programmation réactive pour gérer des flux de données asynchrones.

### 2. Comment utiliser les Coroutines Kotlin pour effectuer des tâches en arrière-plan sans bloquer l'interface utilisateur ?
En utilisant le mot-clé `suspend` pour les fonctions exécutant des opérations longues (réseau, base de données) et en lançant une coroutine avec le dispatcher approprié via un scope (`viewModelScope.launch(Dispatchers.IO) { ... }`).

### 3. Qu'est-ce que le problème du "Main Thread" en Android, et comment les tâches asynchrones peuvent-elles le résoudre ?
Le **Main Thread** (ou UI Thread) gère l'affichage à l'écran et les interactions utilisateur (60 à 120 images par seconde). Si une opération bloquante (I/O réseau ou disque) y est exécutée, l'application se fige, ce qui provoque une erreur **ANR (Application Not Responding)**. Les tâches asynchrones déplacent ces opérations sur des threads de travail secondaires (`Dispatchers.IO`) pour garder le Main Thread fluide.

### 4. Quelle est la différence entre les Threads et les Coroutines en Kotlin ? Quand faut-il utiliser l'un ou l'autre ?
- Un **Thread** est une ressource lourde gérée par le système d'exploitation (coûteuse en mémoire et en temps de commutation).
- Une **Coroutine** est un "thread léger" géré par l'environnement Kotlin, capable de se suspendre sans bloquer de thread système (plusieurs coroutines peuvent partager un seul thread).
- **Utilisation** : Toujours privilégier les coroutines en Kotlin pour le développement Android moderne.

---

## 2. Utilisation du WorkManager

### 5. Qu'est-ce que le WorkManager en Android et dans quels cas est-il recommandé de l'utiliser ?
`WorkManager` est une bibliothèque Jetpack pour planifier des tâches en arrière-plan **persistantes** (qui survivent au redémarrage de l'appareil et à la fermeture de l'app). Recommandé pour les tâches garanties d'être exécutées (synchronisation de données, envois de rapports, sauvegardes).

### 6. Comment créer et configurer une tâche en arrière-plan avec WorkManager pour une exécution immédiate (OneTimeWorkRequest) ?
```kotlin
val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
WorkManager.getInstance(context).enqueue(syncRequest)
```

### 7. Comment configurer une tâche qui s'exécute périodiquement avec WorkManager (PeriodicWorkRequest) ?
```kotlin
val periodicRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES).build()
WorkManager.getInstance(context).enqueueUniquePeriodicWork(
    "SyncWork",
    ExistingPeriodicWorkPolicy.KEEP,
    periodicRequest
)
```

### 8. Quelles sont les contraintes que l'on peut définir dans une tâche avec WorkManager (ex. : réseau, charge de la batterie) ?
```kotlin
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .setRequiresCharging(true)
    .build()
```

---

## 3. Implémentation de JobScheduler

### 9. Quelle est la différence entre WorkManager et JobScheduler ? Quand faut-il privilégier l'utilisation de l'un par rapport à l'autre ?
- **`JobScheduler`** est une API native du framework Android (API 21+) mais nécessite beaucoup de code boilerplate et gère mal la rétrocompatibilité sur les anciennes versions d'Android.
- **`WorkManager`** est une surcouche moderne (recommandée) qui utilise en interne `JobScheduler` (sur API 23+) ou `AlarmManager`/`BroadcastReceiver` (sur API < 23). **Toujours privilégier WorkManager**.

### 10. Comment utiliser JobScheduler pour planifier une tâche qui ne s'exécute que lorsqu'un réseau Wi-Fi est disponible ?
En créant un `JobInfo.Builder`, en définissant `setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)`, et en planifiant via le `JobScheduler`.

### 11. Comment gérer les priorités et les conditions (comme la batterie, la connectivité réseau) avec JobScheduler ?
Via `JobInfo.Builder`: `setRequiresCharging(true)`, `setRequiredNetworkType(...)`, `setBackoffCriteria(...)`.

---

## 4. Manipulation des Threads avec Kotlin

### 12. Comment créer et gérer des Threads en Kotlin pour exécuter des tâches en arrière-plan dans une application Android ?
```kotlin
Thread {
    // Tâche en arrière-plan
    val data = fetchData()
    runOnUiThread {
        // Mise à jour UI
    }
}.start()
```

### 13. Quelle est l'importance des Coroutines Kotlin pour remplacer les Threads dans Android, et quelles sont les meilleures pratiques pour les utiliser ?
Elles éliminent les "callback hell", réduisent la consommation mémoire, simplifient la gestion du cycle de vie (`viewModelScope`) et garantissent la lisibilité du code asynchrone.

### 14. Comment synchroniser les données entre les différents threads en Kotlin sans causer de problèmes de concurrence ?
En utilisant `Mutex`, des structures de données thread-safe, ou en utilisant l'architecture **Single Source of Truth** avec des `StateFlow` confinés au thread principal.

### 15. Que sont les Dispatchers dans Kotlin et comment les utiliser pour contrôler où une coroutine est exécutée ?
- `Dispatchers.Main` : Thread principal (UI).
- `Dispatchers.IO` : Optimisé pour les opérations réseau et disque.
- `Dispatchers.Default` : Optimisé pour les calculs lourds (CPU-intensive).

---

## 5. Présentation du fichier Manifest.xml

### 16. Quel est le rôle du fichier AndroidManifest.xml dans une application Android ?
Il décrit l'application au système Android (nom, icône, activités, services, récepteurs, permissions requises et niveau d'API minimal).

### 17. Comment déclarer des permissions dans le fichier AndroidManifest.xml pour accéder à la localisation, au Bluetooth et au réseau ?
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

### 18. Que sont les permissions d’installation et permissions d'exécution (runtime permissions) dans Android ?
- **Install-time permissions** : Permissions accordées automatiquement à l'installation (ex: `INTERNET`).
- **Runtime permissions** : Permissions sensibles (localisation, contacts, caméra) demandées dynamiquement pendant l'exécution de l'application.

---

## 6. Utilisation des Install-time permissions

### 19. Quelles sont les permissions que l'on déclare dans le fichier AndroidManifest.xml et qui sont demandées au moment de l'installation ?
Les permissions normales (ex: `ACCESS_NETWORK_STATE`, `INTERNET`, `VIBRATE`).

### 20. Comment déclarer correctement la permission d'accès à la localisation et au réseau dans le AndroidManifest.xml ?
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

---

## 7. Utilisation des Runtime permissions

### 21. Quelles sont les runtime permissions en Android et pourquoi doivent-elles être demandées après l'installation ?
Ce sont des permissions dites **dangereuses** (accès aux données personnelles : GPS, contacts, micro, caméra). Elles sont demandées au moment de leur utilisation pour donner le contrôle total à l'utilisateur sur sa vie privée.

### 22. Comment demander des permissions d'exécution telles que l'accès à la localisation, au réseau, au Bluetooth, et aux contacts ?
En utilisant l'API standard `ActivityResultContracts.RequestPermission()` ou `RequestMultiplePermissions()`.

### 23. Quelle est la différence entre une permission de type normale et une permission de type dangereuse ?
- **Normale** : Risque minime pour la vie privée (ex: accès Internet). Accordée à l'installation.
- **Dangereuse** : Accès aux données sensibles (ex: GPS, contacts). Nécessite l'approbation explicite de l'utilisateur à l'exécution.

### 24. Quelles sont les meilleures pratiques pour gérer le refus de permissions par un utilisateur ?
Expliquer clairement pourquoi la permission est nécessaire (Rationale), proposer une alternative, et ne pas bloquer l'application entière si une fonctionnalité secondaire échoue.

---

## 8. Utilisation des Workflow pour la demande de permissions

### 25. Comment implémenter un workflow de demande de permissions pour guider l'utilisateur ?
Vérifier si la permission est déjà accordée -> Si non, vérifier si une explication (rationale) doit être affichée -> Lancer la demande de permission -> Gérer le résultat (accordé / refusé).

### 26. Qu'est-ce qu'un rationale (raison) pour une permission dans Android ?
Un message explicatif affiché à l'utilisateur avant la boîte de dialogue système si l'utilisateur a précédemment refusé la permission sans cocher "Ne plus demander".

---

## 9. Utilisation de la Permission de localisation

### 27. Comment demander l'accès à la localisation de manière à respecter les bonnes pratiques de confidentialité ?
Ne demander la localisation que lorsque l'utilisateur active une fonctionnalité en ayant besoin (ex: suivi d'entraînement) et privilégier `ACCESS_COARSE_LOCATION` si la précision GPS exacte n'est pas indispensable.

### 28. Comment gérer les permissions d'accès à la localisation fine (GPS) et approximative (réseau) ?
En demandant les deux conjointement ou de manière progressive selon le besoin de l'application.

### 29. Quelles sont les limitations et les bonnes pratiques concernant l'utilisation de la localisation en arrière-plan ?
Sur Android 10+, la localisation en arrière-plan nécessite une permission spécifique (`ACCESS_BACKGROUND_LOCATION`) qui doit être demandée séparément après la localisation au premier plan.

---

## 10. Présentation de la permission d’accès au réseau

### 30. Comment déclarer et demander la permission d'accès au réseau pour effectuer des opérations réseau ?
Déclarer `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />` et `<uses-permission android:name="android.permission.INTERNET" />`. Ces permissions sont de type normal (accordées à l'installation).

### 31. Quelles sont les meilleures pratiques pour vérifier la connectivité réseau et gérer les erreurs ?
Utiliser `ConnectivityManager` pour écouter l'état du réseau et encapsuler les appels réseau dans des blocs `try-catch` avec gestion des exceptions.

---

## 11. Présentation de la permission d’accès aux contacts de téléphone

### 32. Quelles sont les étapes pour demander et utiliser la permission d'accès aux contacts ?
1. Déclarer `<uses-permission android:name="android.permission.READ_CONTACTS" />` dans le Manifest.
2. Vérifier si `ContextCompat.checkSelfPermission` est accordé.
3. Demander la permission via `requestPermissionLauncher`.
4. Interroger le `ContentResolver` pour récupérer les contacts.

### 33. Comment utiliser l'API des Contacts en Android tout en respectant les règles de confidentialité ?
N'accéder qu'aux champs strictement nécessaires et ne jamais stocker ou transmettre les carnets d'adresses sans consentement explicite.

---

## 12. Présentation de la permission d’accès au Bluetooth

### 34. Quelles permissions sont nécessaires pour accéder au Bluetooth dans Android et comment les déclarer ?
Sur Android 12 (API 31)+ :
```xml
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
```
Sur les versions antérieures :
```xml
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
```

### 35. Comment vérifier l'état du Bluetooth et se connecter à un appareil Bluetooth externe (ex: capteur cardiaque) ?
Vérifier `BluetoothAdapter`, s'assurer que les permissions `BLUETOOTH_CONNECT` sont accordées, puis lancer l'appareillage et la connexion GATT.

---

## 13. Aperçu sur les bibliothèques de permissions existantes

### 36. Quelles sont les principales bibliothèques tierces pour faciliter la gestion des permissions ?
- **EasyPermissions** (Google / Developer community)
- **PermissionsDispatcher**
- **Dexter**

### 37. Quels sont les avantages d'utiliser une bibliothèque comme EasyPermissions ?
Elle réduit considérablement le code répétitif (boilerplate) pour vérifier les permissions, afficher les rationales et gérer les callbacks de refus ou d'acceptation.

---

## 14. Focus sur la bibliothèque EasyPermission : Demandes d’accès

### 38. Comment intégrer la bibliothèque EasyPermission dans votre projet Android ?
Ajouter la dépendance dans `build.gradle.kts` :
```kotlin
implementation("pub.devrel:easypermissions:3.0.0")
```

### 39. Comment utiliser EasyPermission pour demander des permissions de localisation, Bluetooth et contacts ?
```kotlin
if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
    // Permission déjà accordée
} else {
    EasyPermissions.requestPermissions(
        this,
        "Cette application a besoin de la localisation pour le suivi d'entraînement.",
        RC_LOCATION_PERM,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}
```

### 40. Quels sont les avantages de EasyPermission par rapport à la gestion manuelle ?
- Gestion centralisée des annotations ou des vérifications d'état.
- Gestion élégante des boîtes de dialogue de justification (rationale).
- Moins de risques d'erreurs dans l'implémentation des requêtes multiples.
