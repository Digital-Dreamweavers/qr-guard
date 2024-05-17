package dev.digitaldreamweavers.qrguard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import dev.digitaldreamweavers.qrguard.databinding.ActivityMainBinding;
import dev.digitaldreamweavers.qrguard.ui.ViewFinderFragment;
import dev.digitaldreamweavers.qrguard.ui.ViewFinderViewModel;
import dev.digitaldreamweavers.qrguard.ui.login.LoginActivity;

import androidx.fragment.app.FragmentTransaction;
import dev.digitaldreamweavers.qrguard.ui.BottomNavigationFragment;
import dev.digitaldreamweavers.qrguard.ui.profile.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private PreviewView qrViewFinder;

    private ExecutorService executor;
    private ImageCapture imageCapture;
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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_camera) {
                // Start CameraActivity
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                finish(); // Optional: Finish current activity
                return true;
            } else if (id == R.id.navigation_profile) {
                // Start ProfileActivity
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish(); // Optional: Finish current activity
                return true;
            }
            return false;
        });



        // Check if permissions are granted.
        if (!permissionsGranted()) {
            Log.w(TAG, "Permissions not granted by user, starting PermissionsActivity...");
            startPermissionsActivity();
        } else {
            Log.w(TAG, "Permissions granted by user, proceeding.");
        }

        viewFinderModel = new ViewModelProvider(this).get(ViewFinderViewModel.class);

        // Add BottomNavigationFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bottom_navigation, new BottomNavigationFragment());
        transaction.commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.viewFinder,
                        new ViewFinderFragment())
                .commit();

        // Initialise camera provider
        Log.w(TAG, "Starting Camera...");
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        viewFinderModel.initCameraProviderFuture(cameraProviderFuture);

        startCamera();


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        Preview preview = new Preview.Builder()
                .build();


        CameraSelector cameraSelector = new CameraSelector.Builder()
                //.requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        qrViewFinder = findViewById(R.id.viewFinder);

        Log.w(TAG, "Setting Surface Provider for camera...");
        preview.setSurfaceProvider(qrViewFinder.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }


    private void startCamera() {

        viewFinderModel.getCameraProviderFuture().observe(this, cameraProviderFuture -> {
            if (cameraProviderFuture != null) {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    Log.w(TAG, "Could not add Camera Listener: ", e);
                }
            } else {
                Log.w(TAG, "Camera Provider Future is null");
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

    private void startPermissionsActivity() {
        Intent intent = new Intent(this, PermissionsActivity.class);
        startActivity(intent);
        finish();
    }

}

