package com.example.stocks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Click(R.id.stocksButton)
    public void onStocksClick() {
        Intent intent = new Intent(this, StockListActivity_.class);
        startActivity(intent);
    }
}