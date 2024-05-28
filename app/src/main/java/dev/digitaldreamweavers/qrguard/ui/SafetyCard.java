package dev.digitaldreamweavers.qrguard.ui;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.analytics.FirebaseAnalytics;

import dev.digitaldreamweavers.qrguard.R;
import dev.digitaldreamweavers.qrguard.checker.Check;

public class SafetyCard extends Fragment {

    private SafetyCardViewModel mViewModel;

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final String TAG = "SafetyCard";

    private TextView txtRating;
    private TextView txtExplainer;
    private TextView txtDisclaimer;

    private ImageView ratingIcon;

    public static SafetyCard newInstance() {
        return new SafetyCard();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_safety_card, container, false);

        // Get text components.
        txtRating = view.findViewById(R.id.txtRating);
        txtExplainer = view.findViewById(R.id.txtExplainer);
        txtDisclaimer = view.findViewById(R.id.txtDisclaimer);
        ratingIcon = view.findViewById(R.id.imgRating);

        return view;
    }

    // Adjust the colour scheme of the safetycard based on ratings.
    private void setupReport(Check check) {
        Log.i(TAG, "Setting up report...");
        Log.i(TAG, check.toString());
        Check.SafetyStatus status = check.getSafetyStatus();
        Log.i(TAG, "Status: " + status.toString());

        switch (status) {
            case VERIFIED_SAFE:
            case UNVERIFIED_SAFE:
                txtRating.setText(R.string.reportActivity_rating_SAFE);
                ratingIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.rating_safe));
                txtExplainer.setText(R.string.reportActivity_safe_explainer);
                break;
            case VERIFIED_UNSAFE:
            case UNVERIFIED_UNSAFE:
                txtRating.setText(R.string.reportActivity_rating_UNSAFE);
                ratingIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.rating_dangerous));
                txtExplainer.setText(R.string.reportActivity_unsafe_explainer);
                // Log event with Firebase Analytics for unsafe QR code
                logUnsafeQRCodeEvent();
                break;

            // Includes Unknown rating as well.
            default:
                txtRating.setText(R.string.reportActivity_rating_UNKNOWN);
                ratingIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.rating_unrated));
                txtExplainer.setText(R.string.reportActivity_unknown_explainer);
                break;
        }

        if (check.isPhishTank()) {
            txtDisclaimer.setText(R.string.reportActivity_phishtank_disclaimer);
        } else {
            txtDisclaimer.setText(R.string.reportActivity_firestore_disclaimer);
        }
    }

    // Method to log an event for unsafe QR code with Firebase Analytics
    private void logUnsafeQRCodeEvent() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "SafetyCardFragment");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Safety Card Screen");
        mFirebaseAnalytics.logEvent("UnsafeQRCodeDetected", bundle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SafetyCardViewModel.class);
        mViewModel.getChecker().observe(getViewLifecycleOwner(), this::setupReport);
    }

    public void setCheck(Check currentCheck) {
        mViewModel.setChecker(currentCheck);
    }
}

