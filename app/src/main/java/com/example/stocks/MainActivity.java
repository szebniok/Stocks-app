package com.example.stocks;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Click(R.id.summaryButton)
    public void onSummaryClick() {
        Intent intent = new Intent(this, StockListActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.searchButton)
    public void onSearchClick() {
        Intent intent = new Intent(this, StockSearchActivity_.class);
        startActivity(intent);
    }
}