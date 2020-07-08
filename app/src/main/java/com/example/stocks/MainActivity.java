package com.example.stocks;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stocks.stock_favourites.StockFavouritesActivity_;
import com.example.stocks.stock_list.StockListActivity_;
import com.example.stocks.stock_search.StockSearchActivity_;

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

    @Click(R.id.favouritesButton)
    public void onFavouritesClick() {
        Intent intent = new Intent(this, StockFavouritesActivity_.class);
        startActivity(intent);
    }
}