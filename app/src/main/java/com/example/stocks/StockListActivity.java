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
public class StockListActivity extends AppCompatActivity implements StockListRecyclerAdapter.OnStockListItemClick {

    @Bean
    StockListViewModel stockListViewModel;

    @BindingObject
    ActivityStockListBinding binding;

    private StockListRecyclerAdapter adapter;

    private List<Stock> stocks;

    @AfterViews
    void setBinding() {
        binding.setViewmodel(stockListViewModel);
        binding.setLifecycleOwner(this);

        setupRecyclerView(binding.stockListRecyclerView);

        stockListViewModel.getStocks();
        stockListViewModel.stocks.observe(this, stocks -> {
            this.stocks = stocks;
            adapter.updateStocks(stocks);
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        adapter = new StockListRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void handleClick(int position) {
        Stock stock = stocks.get(position);
        Intent intent = new Intent(this, StockDetailsActivity_.class);
        intent.putExtra("symbol", stock.getSymbol());
        startActivity(intent);
    }
}
