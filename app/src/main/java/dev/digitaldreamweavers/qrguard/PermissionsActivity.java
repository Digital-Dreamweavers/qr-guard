package dev.digitaldreamweavers.qrguard;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PermissionsActivity extends AppCompatActivity {

    private final String TAG = "PermissionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_permissions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.button_grant_permissions).setOnClickListener(view -> grantPermissions());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionsGranted()) {
            startMainActivity();
        }
    }

    private void grantPermissions() {
        requestMultiplePermissionsLauncher.launch(MainActivity.REQUIRED_PERMISSIONS);
    }

    private boolean permissionsGranted() {
        for (String permission : MainActivity.REQUIRED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private final ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                boolean allPermissionsGranted = true;
                for (boolean permissionGranted : permissions.values()) {
                    allPermissionsGranted &= permissionGranted;
                }

                if (allPermissionsGranted) {
                    Log.w(TAG, "All permissions granted");
                    startMainActivity();
                } else {
                    Log.w(TAG, "Not all permissions granted, go to settings.");
                    openAppSettings();
                }
            });

}