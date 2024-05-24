package dev.digitaldreamweavers.qrguard;

import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class LocationTrackingActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private CollectionReference qrCodesRef;
    private ListenerRegistration qrCodesListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_tracking);

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize Firestore reference
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        qrCodesRef = db.collection("unsafe_qr_codes");

        // Request location permissions
        requestLocationPermissions();

        // Start location updates
        startLocationUpdates();
    }

    // Method to request location permissions (you need to implement this)
    private void requestLocationPermissions() {
        // Implement this method to request location permissions from the user
    }

    // Method to start location updates (you need to implement this)
    private void startLocationUpdates() {
        // Implement this method to start location updates
    }

    // Method to determine user's proximity to unsafe QR codes and trigger notification if near
    private void checkProximityToUnsafeQRCodes(Location userLocation) {
        qrCodesListener = qrCodesRef.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                // Handle error
                return;
            }

            if (querySnapshot != null) {
                for (DocumentSnapshot qrCodeSnapshot : querySnapshot.getDocuments()) {
                    double qrCodeLatitude = qrCodeSnapshot.getDouble("latitude");
                    double qrCodeLongitude = qrCodeSnapshot.getDouble("longitude");

                    // Calculate distance between user's location and QR code's location
                    float distance = calculateDistance(userLocation.getLatitude(), userLocation.getLongitude(), qrCodeLatitude, qrCodeLongitude);

                    // Define threshold distance for proximity (e.g., 100 meters)
                    float thresholdDistance = 100; // in meters

                    // Check if user is within threshold distance of QR code
                    if (distance <= thresholdDistance) {
                        // User is near unsafe QR code, trigger notification using FCM
                        sendNotification("Unsafe QR Code Nearby", "Exercise caution when scanning nearby QR codes.");
                    }
                }
            }
        });
    }

    // Method to calculate distance between two coordinates (you need to implement this)
    private float calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Implement this method to calculate distance between two coordinates
        return 0; // Placeholder return value
    }

    // Method to send notification using Firebase Cloud Messaging
    private void sendNotification(String title, String message) {
        // Construct the notification payload
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("message", message);

        // Send data message using Firebase Cloud Messaging
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("21551772525")
                .setData(data)
                .build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove Firestore listener
        if (qrCodesListener != null) {
            qrCodesListener.remove();
        }
    }
}


