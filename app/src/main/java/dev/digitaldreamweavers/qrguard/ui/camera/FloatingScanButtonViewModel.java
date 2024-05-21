package dev.digitaldreamweavers.qrguard.ui.camera;

import androidx.annotation.Nullable;
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

    public void setFabStatus(int status, @Nullable URL url) {
        Integer statusInteger = status;

        if (url != null) {
            barcodeUrl.setValue(url);
        }

        if (!statusInteger.equals(fabStatus.getValue())) {
            fabStatus.setValue(status);
        }
    }
}