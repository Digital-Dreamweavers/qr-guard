package dev.digitaldreamweavers.qrguard.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BottomNavigationViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();

    public BottomNavigationViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }

    public void checkLoginStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        isLoggedIn.setValue(currentUser != null);
    }
}

