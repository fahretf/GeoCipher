# GeoCipher

GeoCipher is a location-based encrypted messaging app that allows users to drop and retrieve secret messages at real-world GPS coordinates. Messages are encrypted using AES-GCM and can only be unlocked by users who visit the exact location and enter the correct decryption key.

- Continuous GPS-based location retrieval
- Displaying user location (latitude and longitude)
- Encrypted message storage
- Secure message retrieval via Firestore
- User authentication (only via key for now) for message decryption
- UI for viewing and decrypting messages

## Tech Stack

- **Kotlin** – Android development
- **AES-256 Encryption** – Securing messages using AES-GCM
- **GPS-based location tracking** – Fetching real time location
- **Firebase Firestore** – Cloud storage for encrypted messages

## Setup & Installation

### 1. Clone the repository

```sh
git clone https://github.com/fahretf/GeoCipher.git
cd GeoCipher
```

### 2. Open the project in Android Studio

### 3. Configure Firebase
1. Create a Firebase project in [Firebase Console](https://console.firebase.google.com/).
2. Add an Android App to your Firebase project.
3. Download the `google-services.json` file and place it in the `app/` directory.
4. Enable **Firestore** in Firebase and set up Firestore rules.
5. You will also need to create an index due to the filtering conditions

### 4. Run the project
- Connect an Android device or launch an emulator, though an Android device is preferred.
- Build and run

## Features & Implementation

### 1. Location Tracking
- Uses FusedLocationProviderClient to continuously retrieve user location
- Ensures permissions handling and GPS availability checks

### 2. Message Encryption & Decryption
- Encrypts messages with AES-GCM (AES-256)
- Uses PBKDF2 key derivation to derive encryption keys from user input

### 3. Secure Message Storage & Retrieval
- Stores encrypted messages in Firestore with latitude & longitude
- Allows users to retrieve messages within a defined GPS radius


