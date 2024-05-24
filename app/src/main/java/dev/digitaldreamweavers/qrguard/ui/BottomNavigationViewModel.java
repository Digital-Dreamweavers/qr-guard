package dev.digitaldreamweavers.qrguard.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class BottomNavigationViewModel extends ViewModel {

    private final MutableLiveData<Integer> selectedItem = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void setSelectedItem(int itemId) {
        selectedItem.setValue(itemId);
    }

    public LiveData<Integer> getSelectedItem() {
        return selectedItem;
    }

    public LiveData<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }

    public void checkLoginStatus() {
        if (mAuth.getCurrentUser() != null) {
            isLoggedIn.setValue(true);
        } else {
            isLoggedIn.setValue(false);
        }
    }
}

