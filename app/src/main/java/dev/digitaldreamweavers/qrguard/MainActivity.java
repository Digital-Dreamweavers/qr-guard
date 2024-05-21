package dev.digitaldreamweavers.qrguard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.digitaldreamweavers.qrguard.databinding.ActivityMainBinding;
import dev.digitaldreamweavers.qrguard.ui.camera.ViewFinderFragment;
import dev.digitaldreamweavers.qrguard.ui.camera.ViewFinderViewModel;

import androidx.fragment.app.FragmentTransaction;
import dev.digitaldreamweavers.qrguard.ui.BottomNavigationFragment;
import dev.digitaldreamweavers.qrguard.ui.profile.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    private ExtendedFloatingActionButton scanFAB;


    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ViewFinderViewModel viewFinderModel;

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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bottom_navigation, new BottomNavigationFragment());
        transaction.commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new ViewFinderFragment()).commit();






        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.viewFinder,
                        new ViewFinderFragment())
                .commit();

        // Initialise camera provider
        Log.w(TAG, "Starting Camera...");
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        viewFinderModel.initCameraProviderFuture(cameraProviderFuture);

        // Initialise Barcode Scanner
        qrScanner = BarcodeScanning.getClient();

        startCamera();


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    private void updateFAB(int status) {
        runOnUiThread(() -> {
            switch (status) {
                case 0: // Waiting
                    Log.i(TAG, "WAITING");
                    //scanFAB.setText(R.string.qr_status_waiting);
                    break;
                case 1: // Invalid
                    Log.i(TAG, "INVALID");
                    scanFAB.setText(R.string.qr_status_invalid);
                    break;

                case 2: // Ready
                    Log.i(TAG, "READY");
                    scanFAB.setText(R.string.qr_status_ready);
                    break;
            }
        });
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

