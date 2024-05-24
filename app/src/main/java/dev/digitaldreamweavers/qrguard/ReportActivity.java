package dev.digitaldreamweavers.qrguard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URL;

import dev.digitaldreamweavers.qrguard.checker.CheckInterface;
import dev.digitaldreamweavers.qrguard.checker.PhishTankCheck;
import dev.digitaldreamweavers.qrguard.databinding.ActivityReportBinding;

public class ReportActivity extends AppCompatActivity {

    private ActivityReportBinding binding;

    private static final String TAG = "ReportActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String urlToCheck = intent.getStringExtra("url");
        Log.i(TAG, "Found: " + urlToCheck);

        CheckInterface checker = performCheck(urlToCheck);
        Log.i(TAG, checker.status.toString());

        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    private CheckInterface performCheck(String url) {
        return new PhishTankCheck(url);
    }
}