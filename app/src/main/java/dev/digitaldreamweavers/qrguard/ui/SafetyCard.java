package dev.digitaldreamweavers.qrguard.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.digitaldreamweavers.qrguard.R;

public class SafetyCard extends Fragment {

    private SafetyCardViewModel mViewModel;

    public static SafetyCard newInstance() {
        return new SafetyCard();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_safety_card, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SafetyCardViewModel.class);
        // TODO: Use the ViewModel
    }

}