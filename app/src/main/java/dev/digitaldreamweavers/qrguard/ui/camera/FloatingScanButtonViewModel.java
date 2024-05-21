package dev.digitaldreamweavers.qrguard.ui.camera;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.net.URL;

public class FloatingScanButtonViewModel extends ViewModel {
    private final MutableLiveData<Integer> fabStatus = new MutableLiveData<>();
    private final MutableLiveData<URL> barcodeUrl = new MutableLiveData<>();

    public LiveData<Integer> getFabStatus() {
        return fabStatus;
    }

    public void setFabStatus(int status, URL url) {
        Integer statusInteger = status;
        barcodeUrl.setValue(url);
        if (!statusInteger.equals(fabStatus.getValue())) {
            fabStatus.setValue(status);
        }
    }
}