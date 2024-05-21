package dev.digitaldreamweavers.qrguard.ui.camera;

import androidx.camera.view.PreviewView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.digitaldreamweavers.qrguard.R;

public class ViewFinderFragment extends Fragment {

    private ViewFinderViewModel mViewModel;
    private PreviewView qrViewFinder;

    public static ViewFinderFragment newInstance() {
        return new ViewFinderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        qrViewFinder = new PreviewView(getContext());

        return inflater.inflate(R.layout.fragment_view_finder, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ViewFinderViewModel.class);
        // TODO: Use the ViewModel
    }

}