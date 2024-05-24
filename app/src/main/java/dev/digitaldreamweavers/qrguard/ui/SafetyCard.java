package dev.digitaldreamweavers.qrguard.ui;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import dev.digitaldreamweavers.qrguard.R;
import dev.digitaldreamweavers.qrguard.checker.Check;

public class SafetyCard extends Fragment {

    private SafetyCardViewModel mViewModel;
    private Check check;

    private static final String TAG = "SafetyCard";

    private MaterialCardView reportCard;
    private TextView txtRating;
    private TextView txtExplainer;
    private TextView txtDisclaimer;

    public static SafetyCard newInstance() {
        return new SafetyCard();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_safety_card, container, false);

        reportCard = view.findViewById(R.id.reportCard);
        reportCard.setClickable(false);

        // Get text components.
        txtRating = view.findViewById(R.id.txtRating);
        txtExplainer = view.findViewById(R.id.txtExplainer);
        txtDisclaimer = view.findViewById(R.id.txtDisclaimer);

        return view;
    }

    // Adjust the colour scheme of the safetycard based on ratings.
    private void setupReport(Check check) {
        Log.i(TAG, "Setting up report...");
        int background;
        int textColor;
        Check.SafetyStatus status = check.getSafetyStatus();
        Log.i(TAG, "Status: " + status);

        switch (status) {
            case VERIFIED_SAFE:
            case UNVERIFIED_SAFE: ;
                txtRating.setText(R.string.reportActivity_rating_SAFE);
                txtExplainer.setText(R.string.reportActivity_safe_explainer);
                break;
            case VERIFIED_UNSAFE:
            case UNVERIFIED_UNSAFE:
                txtRating.setText(R.string.reportActivity_rating_UNSAFE);
                txtExplainer.setText(R.string.reportActivity_unsafe_explainer);
                break;

            // Includes Unknown rating as well.
            default:
                txtRating.setText(R.string.reportActivity_rating_UNKNOWN);
                txtExplainer.setText(R.string.reportActivity_unknown_explainer);
                break;
        }

        if (check.isPhishTank()) {
            txtDisclaimer.setText(R.string.reportActivity_phishtank_disclaimer);
        } else {
            txtDisclaimer.setText(R.string.reportActivity_firestore_disclaimer);
        }

        //reportCard.setCardBackgroundColor(background);
        //txtRating.setTextColor(textColor);
        //txtExplainer.setTextColor(textColor);
        //txtDisclaimer.setTextColor(textColor);

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