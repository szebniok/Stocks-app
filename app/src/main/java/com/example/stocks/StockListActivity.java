package com.example.stocks;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocks.databinding.ActivityStockListBinding;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@DataBound
@EActivity(R.layout.activity_stock_list)
public class StockListActivity extends AppCompatActivity {

    @Bean
    StockListViewModel stockListViewModel;

    @BindingObject
    ActivityStockListBinding binding;

    private StockListRecyclerAdapter adapter;

    @ViewById(R.id.stockListRecyclerView)
    RecyclerView recyclerView;

    private List<Stock> stocks;

    @AfterViews
    void setBinding() {
        binding.setViewmodel(stockListViewModel);
        binding.setLifecycleOwner(this);

        adapter = new StockListRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        stockListViewModel.getStocks();
        stockListViewModel.stocks.observe(this, stocks -> {
            this.stocks = stocks;
            adapter.updateStocks(stocks)
        });
    }

    }
}
