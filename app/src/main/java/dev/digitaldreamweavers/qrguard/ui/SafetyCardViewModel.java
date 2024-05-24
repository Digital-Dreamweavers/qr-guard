package dev.digitaldreamweavers.qrguard.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dev.digitaldreamweavers.qrguard.checker.Check;

public class SafetyCardViewModel extends ViewModel {

    private MutableLiveData<Check> checker = new MutableLiveData<>();

    public void setChecker(Check checker) {
        this.checker.setValue(checker);
    }
    public LiveData<Check> getChecker() {
        return checker;
    }

}