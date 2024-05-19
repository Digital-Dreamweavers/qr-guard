package dev.digitaldreamweavers.qrguard.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dev.digitaldreamweavers.qrguard.MainActivity;
import dev.digitaldreamweavers.qrguard.MapsActivity;
import dev.digitaldreamweavers.qrguard.R;
import dev.digitaldreamweavers.qrguard.ui.login.LoginActivity;
import dev.digitaldreamweavers.qrguard.ui.profile.ProfileActivity;

public class BottomNavigationFragment extends Fragment {

    private FirebaseAuth mAuth;
    private BottomNavigationViewModel mViewModel;

    private String TAG = "BottomNavigationFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mViewModel = new ViewModelProvider(this).get(BottomNavigationViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_map) {
                    navigateToMapActivity();
                    return true;
                } else if (id == R.id.navigation_camera) {
                    navigateToCameraActivity();
                    return true;
                } else if (id == R.id.navigation_profile) {
                    navigateToProfileActivity();
                    return true;
                }
                return false;
            }

        });

        // Observe the login status
        mViewModel.getIsLoggedIn().observe(getViewLifecycleOwner(), isLoggedIn -> {
            if (isLoggedIn) {
                // TODO: Get Profile picture
                Log.i(TAG, "User is logged in.");
            } else {
                // User is not logged in, navigate to LoginActivity
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish(); // Finish current activity to prevent user from going back to the main screen if they press back
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check login status when the fragment is resumed
        mViewModel.checkLoginStatus();
    }

    private void navigateToMapActivity() {
        startActivity(new Intent(requireActivity(), MapsActivity.class));
    }

    private void navigateToCameraActivity() {
        startActivity(new Intent(requireActivity(), MainActivity.class));
    }

    private void navigateToProfileActivity() {
        startActivity(new Intent(requireActivity(), ProfileActivity.class));
    }
}







