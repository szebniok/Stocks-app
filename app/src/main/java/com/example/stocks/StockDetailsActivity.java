package com.example.stocks;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stocks.databinding.ActivityStockDetailsBinding;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_stock_details)
@DataBound
public class StockDetailsActivity extends AppCompatActivity {

    @BindingObject
    ActivityStockDetailsBinding binding;

    @Bean
    StockDetailsViewModel viewModel;

    @AfterViews
    public void setBinding() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        String symbol = getIntent().getStringExtra("symbol");
        viewModel.getQuote(symbol);
    }
}
