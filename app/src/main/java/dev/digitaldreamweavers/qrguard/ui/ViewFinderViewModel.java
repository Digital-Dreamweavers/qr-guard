package dev.digitaldreamweavers.qrguard.ui;

import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;

public class ViewFinderViewModel extends ViewModel {
    private final MutableLiveData<ListenableFuture<ProcessCameraProvider>> cameraProviderFuture = new MutableLiveData<>();

    private final MutableLiveData<Integer> fabStatus = new MutableLiveData<>();

    public final static int FAB_STATUS_WAITING = 0;


    public LiveData<ListenableFuture<ProcessCameraProvider>> getCameraProviderFuture() {
        return cameraProviderFuture;
    }

    public void initCameraProviderFuture(ListenableFuture<ProcessCameraProvider> future) {
        cameraProviderFuture.setValue(future);
    }

}