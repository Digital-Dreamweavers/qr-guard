package dev.digitaldreamweavers.qrguard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import java.net.URL;

import dev.digitaldreamweavers.qrguard.databinding.ActivityMainBinding;
import dev.digitaldreamweavers.qrguard.ui.BottomNavigationFragment;

/*
 *
 *  MainActivity.java
 *
 *  The MainActivity along with the layout activity_main.xml is the entry point and base for
 *  some of the main screens (Map, Camera and Profile).
 *
 *  In the context of entry point, it also checks for permissions then prepares the camera
 *  while authenticating with Google Sign-In.
 *
 *  ATTENTION IF COMPILING FROM SOURCE!
 *  When signing in with Google, you may get a DEVELOPER_ERROR 10 result in Logcat.
 *  This is because Google Sign-in will NOT work without an authorised SHA-1 fingerprint in Firebase,
 *
 */

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    public static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if permissions are granted.
        if (!permissionsGranted()) {
            Log.w(TAG, "Permissions not granted by user, starting PermissionsActivity...");
            startPermissionsActivity();
        } else {
            Log.w(TAG, "Permissions granted by user, proceeding.");
        }

        // Add BottomNavigationFragment
        // The BNF will activate the viewfinder.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bottom_navigation, new BottomNavigationFragment());
        transaction.commit();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }


    private boolean permissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    // Starts ReportActivity to scan url.
    private void startReportActivity(URL urlToScan) {
        // TODO: Start ReportActivity.
    }

    private void startPermissionsActivity() {
        Intent intent = new Intent(this, PermissionsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

