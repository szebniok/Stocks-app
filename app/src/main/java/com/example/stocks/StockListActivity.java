package com.example.stocks;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.stocks.databinding.ActivityStockListBinding;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@DataBound
@EActivity(R.layout.activity_stock_list)
public class StockListActivity extends AppCompatActivity {

    @Bean
    private StockListViewModel stockListViewModel;

    @BindingObject
    ActivityStockListBinding binding;

    @ViewById(R.id.textview)
    TextView text;

    @AfterViews
    void setBinding() {
        binding.setViewmodel(stockListViewModel);
        binding.setLifecycleOwner(this);

        stockListViewModel.getStocks();
        stockListViewModel.stocks.observe(this, stocks -> {
                    text.setText(stocks.get(0).getShortName());
                }
        );
    }
}
