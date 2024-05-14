package dev.digitaldreamweavers.qrguard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import com.google.common.util.concurrent.ListenableFuture;

import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import dev.digitaldreamweavers.qrguard.databinding.ActivityMainBinding;
import dev.digitaldreamweavers.qrguard.ui.ViewFinderFragment;
import dev.digitaldreamweavers.qrguard.ui.ViewFinderViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private PreviewView qrViewFinder;
    private ExecutorService executor;
    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ViewFinderViewModel viewFinderModel;

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.cameraContainer,
                        new ViewFinderFragment())
                .commit();

        viewFinderModel = new ViewModelProvider(this).get(ViewFinderViewModel.class);

        // get camera perms, not proper implementation
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            startCamera();
        }

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

        preview.setSurfaceProvider(qrViewFinder.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }


    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        // Bind camera to listener.
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.w(TAG, "Could not add Camera Listener: ", e);
            }
            }, ContextCompat.getMainExecutor(this));

        // Create a Toast object
        Toast toast = Toast.makeText(this, "Camera starts here.", Toast.LENGTH_SHORT);

        // Show the Toast
        toast.show();
    }



}