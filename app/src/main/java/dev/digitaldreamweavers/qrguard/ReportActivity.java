package dev.digitaldreamweavers.qrguard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import dev.digitaldreamweavers.qrguard.checker.Check;
import dev.digitaldreamweavers.qrguard.checker.PhishTankCheck;
import dev.digitaldreamweavers.qrguard.databinding.ActivityReportBinding;
import dev.digitaldreamweavers.qrguard.ui.SafetyCard;
import dev.digitaldreamweavers.qrguard.ui.SafetyCardViewModel;

public class ReportActivity extends AppCompatActivity {

    private ActivityReportBinding binding;

    private SafetyCardViewModel mVMSafetyCard;

    public Check currentCheck;

    private static final String TAG = "ReportActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String urlToCheck = intent.getStringExtra("url");
        Log.i(TAG, "Found: " + urlToCheck);

        currentCheck = performCheck(urlToCheck);
        Log.i(TAG, currentCheck.getSafetyStatus().toString());

        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the fragment.
        SafetyCard safetyCardFragment = (SafetyCard) getSupportFragmentManager().findFragmentById(R.id.SafetyCardContainer);

        if (safetyCardFragment != null) {
            mVMSafetyCard = new ViewModelProvider(safetyCardFragment).get(SafetyCardViewModel.class);
            mVMSafetyCard.setChecker(currentCheck);
        } else {
            Log.e(TAG, "SafetyCardFragment is null.");
        }



    }

    private Check performCheck(String url) {
        return new PhishTankCheck(url);
    }
}