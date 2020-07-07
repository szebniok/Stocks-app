package com.example.stocks.stock_list;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stocks.R;
import com.example.stocks.StockRecyclerViewFragment;
import com.example.stocks.stock_details.StockDetailsActivity_;
import com.example.stocks.databinding.ActivityStockListBinding;
import com.example.stocks.domain.Stock;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;

@DataBound
@EActivity(R.layout.activity_stock_list)
public class StockListActivity extends AppCompatActivity {

    @Bean
    StockListViewModel stockListViewModel;

    @BindingObject
    ActivityStockListBinding binding;

    @FragmentById(R.id.stockListRecyclerViewFragment)
    StockRecyclerViewFragment fragment;

    @AfterViews
    void setBinding() {
        binding.setViewmodel(stockListViewModel);
        binding.setLifecycleOwner(this);

        fragment.setItemClickHandler(this::handleClick);

        stockListViewModel.getStocks();
        stockListViewModel.stocks.observe(this, stocks -> {
            fragment.updateStocks(stocks);
        });
    }

    public void handleClick(Stock stock) {
        Intent intent = new Intent(this, StockDetailsActivity_.class);
        intent.putExtra("symbol", stock.getSymbol());
        startActivity(intent);
    }
}
