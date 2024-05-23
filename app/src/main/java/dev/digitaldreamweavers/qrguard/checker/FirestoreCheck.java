package dev.digitaldreamweavers.qrguard.checker;

import java.net.URL;

public class FirestoreCheck implements CheckInterface {

    private final String TAG = "Checker (Firestore)";

    public FirestoreCheck(URL url) {
        if (url == null) {
            return;
        }
    }
}
