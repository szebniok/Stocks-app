package com.example.stocks.settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocks.domain.PreferencesService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean
public class SettingsViewModel extends ViewModel {
    public MutableLiveData<Integer> refreshRateSpinnerIndex = new MutableLiveData<>();

    @Bean
    PreferencesService preferencesService;

    public void getRefreshRatePreference() {
        refreshRateSpinnerIndex.postValue(preferencesService.getRefreshRatePreferenceIndex());
    }

    public void setRefreshRatePreference(int pos) {
        preferencesService.setRefreshRatePreferenceIndex(pos);
        refreshRateSpinnerIndex.postValue(pos);
    }
}
