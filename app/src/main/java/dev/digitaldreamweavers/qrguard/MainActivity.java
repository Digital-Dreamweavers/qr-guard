package dev.digitaldreamweavers.qrguard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import androidx.camera.core.ImageCapture;

import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;

import java.util.concurrent.ExecutorService;

import dev.digitaldreamweavers.qrguard.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private PreviewView qrViewFinder;
    private ExecutorService executor;
    private ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        qrViewFinder = findViewById(R.id.viewFinder);

        // get camera perms, not proper implementation
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            startCamera();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }


    private void startCamera() {
        // Create a Toast object
        Toast toast = Toast.makeText(this, "Camera starts here.", Toast.LENGTH_SHORT);

        // Show the Toast
        toast.show();
    }

}