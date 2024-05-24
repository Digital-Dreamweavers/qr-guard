package dev.digitaldreamweavers.qrguard;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import dev.digitaldreamweavers.qrguard.ui.SafetyCard;
import dev.digitaldreamweavers.qrguard.ui.SafetyCardViewModel;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dev.digitaldreamweavers.qrguard.checker.Check;
import dev.digitaldreamweavers.qrguard.checker.PhishTankCheck;
import dev.digitaldreamweavers.qrguard.databinding.ActivityReportBinding;
import dev.digitaldreamweavers.qrguard.ui.SafetyCard;
import dev.digitaldreamweavers.qrguard.ui.SafetyCardViewModel;
import dev.digitaldreamweavers.qrguard.ui.map.MapFragment;

public class ReportActivity extends AppCompatActivity {

    private ActivityReportBinding binding;
    private static final String TAG = "ReportActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0.0;
    private double longitude = 0.0;

    private SafetyCardViewModel mVMSafetyCard;

    public Check currentCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();



        // Initialize Location Services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Intent intent = getIntent();
        String urlToCheck = intent.getStringExtra("url");
        Log.i(TAG, "Found: " + urlToCheck);

        currentCheck = performCheck(urlToCheck);
        Log.i(TAG, currentCheck.getSafetyStatus().toString());


        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView urlTextView = findViewById(R.id.urlTextView);
        urlTextView.setText(urlToCheck);

        // Get the fragment.
        SafetyCard safetyCardFragment = (SafetyCard) getSupportFragmentManager().findFragmentById(R.id.SafetyCardContainer);

        // Setup the Safety card
        if (safetyCardFragment != null) {
            mVMSafetyCard = new ViewModelProvider(safetyCardFragment).get(SafetyCardViewModel.class);
            mVMSafetyCard.setChecker(currentCheck);
        } else {
            Log.e(TAG, "SafetyCardFragment is null.");
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map, new MapFragment())
                .commit();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // Request location permission if not granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                getLocationAndStoreData(urlToCheck, currentCheck.getSafetyStatus().toString());
            }
        } else {
            Log.w(TAG, "User is not authenticated.");
        }

        // Subscribe to FCM topic for receiving notifications about unsafe QR codes
        FirebaseMessaging.getInstance().subscribeToTopic("unsafe_qr_codes");
    }

    private Check performCheck(String url) {
        return new PhishTankCheck(url);
    }

    private void getLocationAndStoreData(String url, String status) {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    Location location = task.getResult();
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                                storeQRData(url, status, latitude, longitude);
                            }
                        });
            } else {
                storeQRData(url, status, latitude, longitude); // Store data without location
            }
        } catch (SecurityException e) {
            Log.e(TAG, "Location permission not granted", e);
            storeQRData(url, status, latitude, longitude); // Store data without location
        }
    }

    private String getLocationName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(address.getAddressLine(i)).append(", ");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 2); // Remove trailing comma and space
                return stringBuilder.toString();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error getting location name", e);
        }
        return null;
    }

    private void storeQRData(String url, String status, double latitude, double longitude) {
        String documentId = hashUrl(url);

        String locationName = getLocationName(latitude, longitude);
        Log.i(TAG, "Location Name: " + locationName);

        final DocumentReference docRef = db.collection("qrData").document(documentId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Document exists, increment the scan count
                        long currentCount = document.getLong("scanCount");
                        docRef.update("scanCount", currentCount + 1,
                                        "latitude", latitude,
                                        "longitude", longitude,
                                        "locationName", locationName)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.i(TAG, "QR Data scan count successfully incremented!");
                                        } else {
                                            Log.w(TAG, "Error incrementing scan count", task.getException());
                                        }
                                    }
                                });
                    } else {
                        // Document does not exist, create a new document
                        Map<String, Object> qrData = new HashMap<>();
                        qrData.put("url", url);
                        qrData.put("status", status);
                        qrData.put("scanCount", 1);
                        qrData.put("latitude", latitude);
                        qrData.put("longitude", longitude);
                        qrData.put("timestamp", System.currentTimeMillis());
                        qrData.put("locationName", locationName);

                        docRef.set(qrData)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.i(TAG, "QR Data successfully written!");
                                        } else {
                                            Log.w(TAG, "Error writing document", task.getException());
                                        }
                                    }
                                });
                    }
                }else{
                        Log.w(TAG, "Error getting document", task.getException());
                    }
                }

        });

        }

    private String hashUrl(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(url.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing URL", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = getIntent();
                String urlToCheck = intent.getStringExtra("url");
                Check checker = performCheck(urlToCheck);
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    getLocationAndStoreData(urlToCheck, checker.getSafetyStatus().toString());

                }
            } else {
                Log.w(TAG, "Location permission denied.");
                Intent intent = getIntent();
                String urlToCheck = intent.getStringExtra("url");
                Check checker = performCheck(urlToCheck);
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    storeQRData(urlToCheck, checker.getSafetyStatus().toString(), latitude, longitude); // Store data without location
                }
            }
        }
    }


    private void showPopUpMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}




