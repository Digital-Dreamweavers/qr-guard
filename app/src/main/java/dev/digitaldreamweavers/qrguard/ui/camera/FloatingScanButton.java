package dev.digitaldreamweavers.qrguard.ui.camera;

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

public class FloatingScanButton extends Fragment {


    public final static int FAB_STATUS_WAITING = 0;
    public final static int FAB_STATUS_INVALID = 1;
    public final static int FAB_STATUS_VALID = 2;

    private String TAG = "FloatingScanButtonFragment";

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
        mViewModel.getFabStatus().observe(getViewLifecycleOwner(), this::updateFabText);

        // TODO: Use the ViewModel
    }

    public void setFabStatus(int status, @Nullable URL url) {
        mViewModel.setFabStatus(status, url);
    }

    public void setFabStatus(int status) {
        mViewModel.setFabStatus(status, null);
    }

    @UiThread
    void updateFabText(int status) {
        switch (status) {
            case FAB_STATUS_WAITING:
                fab.setIconResource(R.drawable.mystery_48dp);
                fab.setText(R.string.qr_status_waiting);

                fab.show();
                fab.extend();

                fab.setClickable(false);
                break;

            case FAB_STATUS_INVALID:
                fab.setIconResource(R.drawable.unknown_48dp);
                fab.setText(R.string.qr_status_invalid);

                fab.setClickable(false);
                break;

            case FAB_STATUS_VALID:
                fab.setIconResource(R.drawable.baseline_qr_code_scanner_64);
                fab.setText(R.string.qr_status_ready);

                fab.setClickable(true);
                break;
        }
    }

}