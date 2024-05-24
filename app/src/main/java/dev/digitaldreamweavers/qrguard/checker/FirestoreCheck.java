package dev.digitaldreamweavers.qrguard.checker;

import java.net.URL;

/**
 * FirestoreCheck.java
 * This class is responsible for checking a URL against the Firestore database.
 * QR Guard uses Firestore to store safety reports compliant with the CheckInterface.java interface.
 *
 * This class is a part of the QR Guard project.
 *
 * @version 1.0
 * @since 1.0
**/
public class FirestoreCheck implements CheckInterface {

    private final String TAG = "Checker (Firestore)";

    public FirestoreCheck(URL url) {
        if (url == null) {
            return;
        }
    }
}
