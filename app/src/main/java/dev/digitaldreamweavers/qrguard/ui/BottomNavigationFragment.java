package dev.digitaldreamweavers.qrguard.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import dev.digitaldreamweavers.qrguard.MainActivity;
import dev.digitaldreamweavers.qrguard.MapsActivity;
import dev.digitaldreamweavers.qrguard.ui.login.LoginActivity;
import dev.digitaldreamweavers.qrguard.R;
import dev.digitaldreamweavers.qrguard.ui.profile.ProfileActivity;

public class BottomNavigationFragment extends Fragment {

    private FirebaseAuth mAuth;
    private BottomNavigationViewModel mViewModel;

    // Define constant variables for the menu item IDs
    private static final int NAVIGATION_MAP_ID = R.id.navigation_map;
    private static final int NAVIGATION_CAMERA_ID = R.id.navigation_camera;
    private static final int NAVIGATION_PROFILE_ID = R.id.navigation_profile;

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

        // Observe the login status
        mViewModel.getIsLoggedIn().observe(getViewLifecycleOwner(), isLoggedIn -> {
            if (isLoggedIn) {
                // User is logged in, navigate to MainActivity
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish(); // Finish current activity to prevent user from going back to the login screen
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle navigation item clicks
        if (item.getItemId() == NAVIGATION_MAP_ID) {
            // Handle navigation map
            navigateToMapActivity();
            return true;
        } else if (item.getItemId() == NAVIGATION_CAMERA_ID) {
            // Handle navigation camera
            navigateToCameraActivity();
            return true;
        } else if (item.getItemId() == NAVIGATION_PROFILE_ID) {
            // Handle navigation profile
            navigateToProfileActivity();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToMapActivity() {
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        startActivity(intent);
    }

    private void navigateToCameraActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    private void navigateToProfileActivity() {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
    }
}





