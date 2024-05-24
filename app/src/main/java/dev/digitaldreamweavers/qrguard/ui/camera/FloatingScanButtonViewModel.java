package dev.digitaldreamweavers.qrguard.ui.camera;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.net.URL;

public class FloatingScanButtonViewModel extends ViewModel {
    private final MutableLiveData<FloatingScanButton.Status> fabStatus = new MutableLiveData<>();
    private final MutableLiveData<URL> barcodeUrl = new MutableLiveData<>();

    public LiveData<FloatingScanButton.Status> getFabStatus() {
        return fabStatus;
    }

    public void setFabStatus(FloatingScanButton.Status status, @Nullable URL url) {

        if (url != null) {
            barcodeUrl.setValue(url);
        }

        if (!status.equals(fabStatus.getValue())) {
            fabStatus.setValue(status);
        }
    }

    public URL getBarcodeUrl() {
        return barcodeUrl.getValue();
    }
}