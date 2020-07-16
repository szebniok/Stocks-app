package com.example.stocks.settings;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.stocks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_settings)
public class SettingsFragment extends Fragment implements Spinner.OnItemSelectedListener {

    @ViewById(R.id.refreshRateSpinner)
    Spinner refreshRateSpinner;

    @Bean
    SettingsViewModel viewModel;

    @AfterViews
    void setup() {
        ArrayAdapter<CharSequence> refreshRateSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.refresh_rate_entries, android.R.layout.simple_spinner_item);

        refreshRateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        refreshRateSpinner.setAdapter(refreshRateSpinnerAdapter);
        refreshRateSpinner.setOnItemSelectedListener(this);

        viewModel.getRefreshRatePreference();
        viewModel.refreshRateSpinnerIndex.observe(this, this::onRefreshRateSpinnerIndexChange);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        viewModel.setRefreshRatePreference(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void onRefreshRateSpinnerIndexChange(Integer integer) {
        refreshRateSpinner.setSelection(integer);
    }

}
