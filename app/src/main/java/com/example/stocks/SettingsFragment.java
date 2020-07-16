package com.example.stocks;

import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_settings)
public class SettingsFragment extends Fragment {

    @ViewById(R.id.refreshRateSpinner)
    Spinner refreshRateSpinner;
}
