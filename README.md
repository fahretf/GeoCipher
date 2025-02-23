GeoCipher

GeoCipher is a location based encrypted messaging app that allows users to drop and retrieve secret messages at real-world GPS coordinates. Messages are encrypted using AES-GCM and can only be unlocked by users who visit the exact location and enter the correct decryption key.
Status

Some current features include:

    Continuous GPS-based location retrieval
    Displaying user location (latitude and longitude)
    Encrypted message storage using AES-GCM
    Secure message retrieval via Firestore
    User authentication for message decryption
    UI for viewing and decrypting messages

Tech Stack

    Kotlin – Android development
    AES-256 Encryption – Securing messages using AES-GCM
    GPS-based location tracking – Fetching real-time location
    Firebase Firestore – Cloud storage for encrypted messages

Setup and installation
1. Clone the repository

git clone https://github.com/fahretf/GeoCipher.git
cd GeoCipher

2. Open the project in Android Studio

    Ensure you have the latest version of Android Studio installed.
    Open the GeoCipher project in Android Studio.

3. Configure Firebase

    Create a Firebase project in Firebase Console.
    Add an Android App to your Firebase project.
    Download the google-services.json file and place it in the app/ directory.
    Enable Firestore in Firebase and set up Firestore rules.

4. Run the project

    Connect an Android device or launch an emulator.
    Build and run the project using Android Studio.

Features and implementation
1. Location Tracking

    Uses FusedLocationProviderClient to continuously retrieve user location
    Ensures permissions handling and GPS availability checks

2. Message Encryption & Decryption

    Encrypts messages with AES-GCM (AES-256)
    Uses PBKDF2 key derivation to derive encryption keys from some user input

3. Secure Message Storage & Retrieval

    Stores encrypted messages in Firestore with latitude and longitude metadata
    Allows users to retrieve messages within a defined GPS radius


