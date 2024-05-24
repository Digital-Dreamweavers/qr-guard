package dev.digitaldreamweavers.qrguard.checker;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
public class FirestoreCheck extends Check {

    private final String TAG = "Checker (Firestore)";

    private FirebaseFirestore db;
    private String hashedUrl;

    public FirestoreCheck(String url) {
        if (url == null) {
            return;
        }

        // Create hash of scanned URL.
        hashedUrl = hashUrl(url);
        Log.i(TAG, "Checking Hash: " + hashedUrl);
        db = FirebaseFirestore.getInstance();
        try {
            checkFirestore();
        } catch (Exception e) {
            Log.e(TAG, "Error occurred: " + e.getMessage());
            setSafetyStatus(SafetyStatus.UNKNOWN);
        }
        Log.i(TAG, "Check complete.");
    }

    private void checkFirestore() {

        // Find the hashed URL in Firestore.
        db.collection("qrData").document(hashedUrl).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Pull status and scancount.
                        parseScanCount(documentSnapshot.getLong("scanCount"));
                        setUrl(documentSnapshot.getString("url"));
                        parseStatus(documentSnapshot.getString("status"));
                        notifyOnReady();
                    } else {
                        // Document does not exist.
                        Log.i(TAG, "Document does not exist.");
                        setSafetyStatus(SafetyStatus.UNKNOWN);
                        notifyOnReady();
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred.
                    Log.e(TAG, "Error occurred: " + e.getMessage());
                    setSafetyStatus(SafetyStatus.UNKNOWN);
                    notifyOnReady();
                });
    }

    // parseStatus: Converts the status from Firestore to a SafetyStatus enum.
    private void parseStatus(String strStatus) {
        try {
            SafetyStatus status = SafetyStatus.valueOf(strStatus);
            Log.i(TAG, "Status String: " + strStatus);
            setSafetyStatus(SafetyStatus.UNVERIFIED_UNSAFE);
            Log.i(TAG, "Status: " + getSafetyStatus().toString());
        } catch (IllegalArgumentException e) {
            // Invalid status.
            Log.e(TAG, "Status invalid.");
        }
    }

    // parseScanCount: Converts the scan count from Firestore to a long.
    private void parseScanCount(Long scanCount) {
        if (scanCount == null) {
            setTotalScans(0);
        } else {
            setTotalScans(scanCount);
        }
    }
}
