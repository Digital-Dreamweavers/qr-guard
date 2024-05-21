package dev.digitaldreamweavers.qrguard.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dev.digitaldreamweavers.qrguard.R;
import dev.digitaldreamweavers.qrguard.ui.camera.ViewFinderFragment;
import dev.digitaldreamweavers.qrguard.ui.login.LoginActivity;
import dev.digitaldreamweavers.qrguard.ui.map.MapFragment;

public class BottomNavigationFragment extends Fragment {

    private FirebaseAuth mAuth;
    private BottomNavigationViewModel mViewModel;

    private String TAG = "BottomNavigationFragment";

    // Bind fragments to navigation bar.
    private final NavigationBarView.OnItemSelectedListener onItemSelected = item -> {
        Log.i(TAG, "Item selected: " + item.getItemId());
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.navigation_camera) {
            Log.i(TAG, "Camera selected.");
            fragment = new ViewFinderFragment();
            // TODO: Camera
        } else if (id == R.id.navigation_profile) {
            Log.i(TAG, "Profile selected.");
            // TODO: Profile
        } else if (id == R.id.navigation_map) {
            Log.i(TAG, "Map selected.");
            fragment = new MapFragment();
            // TODO: Map
        }

        // Replace the current fragment with the selected fragment.
        if (fragment != null) {
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
            return true;
        }

        Log.w(TAG, id + " does not fit into following IDs: " + R.id.navigation_camera + ", " + R.id.navigation_profile + ", " + R.id.navigation_map);

        return false;
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mViewModel = new ViewModelProvider(this).get(BottomNavigationViewModel.class);


        // Let's start with the camera fragment.
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new ViewFinderFragment()).commit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);


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

        // Bind the BottomNavigationView to the listener.
        Log.i(TAG, "Binding BottomNavigationView to listener.");
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(onItemSelected);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check login status when the fragment is resumed
        mViewModel.checkLoginStatus();
    }

}







