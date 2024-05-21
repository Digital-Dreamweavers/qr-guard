package dev.digitaldreamweavers.qrguard.ui.camera;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.digitaldreamweavers.qrguard.R;

public class FloatingScanButton extends Fragment {

    private FloatingScanButtonViewModel mViewModel;

    public static FloatingScanButton newInstance() {
        return new FloatingScanButton();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_floating_scan_button, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FloatingScanButtonViewModel.class);
        // TODO: Use the ViewModel
    }

}