package dev.digitaldreamweavers.qrguard.ui.camera;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.net.URL;

import dev.digitaldreamweavers.qrguard.R;
import dev.digitaldreamweavers.qrguard.ReportActivity;

public class FloatingScanButton extends Fragment {

    public enum Status {
        WAITING,
        INVALID,
        VALID
    }

    private final String TAG = "FloatingScanButtonFragment";

    private FloatingScanButtonViewModel mViewModel;

    private ExtendedFloatingActionButton fab;

    private View view;

    public static FloatingScanButton newInstance() {
        return new FloatingScanButton();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_floating_scan_button, container, false);
        fab = view.findViewById(R.id.scanButton);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FloatingScanButtonViewModel.class);

        // Observe changes.
        mViewModel.getFabStatus().observe(getViewLifecycleOwner(), this::updateFab);

        // TODO: Use the ViewModel
    }

    public void setFabStatus(Status status, @Nullable URL url) {
        mViewModel.setFabStatus(status, url);
    }

    public void setFabStatus(Status status) {
        mViewModel.setFabStatus(status, null);
    }

    @UiThread
    void updateFab(Status status) {
        switch (status) {
            case WAITING:
                fab.setIconResource(R.drawable.mystery_48dp);
                fab.setText(R.string.qr_status_waiting);

                fab.show();
                fab.extend();

                fab.setClickable(false);
                break;

            case INVALID:
                fab.setIconResource(R.drawable.unknown_48dp);
                fab.setText(R.string.qr_status_invalid);

                fab.setClickable(false);
                break;

            case VALID:
                fab.setIconResource(R.drawable.baseline_qr_code_scanner_64);
                fab.setMaxLines(2);
                String scanMessage = getString(R.string.qr_status_ready, mViewModel.getBarcodeUrl().getHost());
                fab.setText(scanMessage);

                fab.setOnClickListener(v -> {
                    navigateToSafetyReport();
                });
                fab.setClickable(true);
                break;
        }
    }

    private void navigateToSafetyReport() {
        Intent intent = new Intent(getContext(), ReportActivity.class);
        intent.putExtra("url", mViewModel.getBarcodeUrl().toString());
        startActivity(intent);
    }

}