package dev.digitaldreamweavers.qrguard.ui.profile;

import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import dev.digitaldreamweavers.qrguard.R;
import dev.digitaldreamweavers.qrguard.ui.setting.SettingActivity;


public class ProfileFragment extends Fragment {

    private final String TAG = "ProfileFragment";
    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Find the settings button and set an OnClickListener
        Button btnSettings = view.findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SettingsActivity
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fetch and display user's data
        fetchAndDisplayUserData();
    }

    private void fetchAndDisplayUserData() {
        // Fetch and display user's data
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Get user's email
            String email = user.getEmail();
            if (email != null && !email.isEmpty()) {
                // Find the TextView for displaying the email
                TextView textViewEmail = getView().findViewById(R.id.text_email);
                // Set the user's email in the TextView
                textViewEmail.setText(email);
            }

            // Get user's display name
            String displayName = user.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                // Find the TextView for displaying the display name
                TextView textViewName = getView().findViewById(R.id.text_name);
                // Set the user's display name in the TextView
                textViewName.setText(displayName);
            }
        }
    }
}





