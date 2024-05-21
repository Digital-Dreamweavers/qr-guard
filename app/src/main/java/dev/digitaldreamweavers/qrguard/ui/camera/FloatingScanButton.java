package dev.digitaldreamweavers.qrguard.ui.camera;

import androidx.annotation.UiThread;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

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
        mViewModel.getFabStatus().observe(getViewLifecycleOwner(), this::updateFabStatus);


        // TODO: Use the ViewModel
    }

    @UiThread
    void updateFabStatus(int status) {
        switch (status) {
            case FAB_STATUS_WAITING:
                //fab.setIconResource(R.drawable.ic_baseline_qr_code_scanner_24);
                fab.setText(R.string.qr_status_waiting);
                break;
            case FAB_STATUS_INVALID:
                //fab.setIconResource(R.drawable.ic_baseline_qr_code_scanner_24);
                fab.setText(R.string.qr_status_invalid);
                break;
            case FAB_STATUS_VALID:
                //fab.setIconResource(R.drawable.ic_baseline_qr_code_scanner_24);
                fab.setText(R.string.qr_status_ready);
                break;
        }
    }

}