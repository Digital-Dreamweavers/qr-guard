package dev.digitaldreamweavers.qrguard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import dev.digitaldreamweavers.qrguard.databinding.ActivityReportBinding;

public class ReportActivity extends AppCompatActivity {

    private ActivityReportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}