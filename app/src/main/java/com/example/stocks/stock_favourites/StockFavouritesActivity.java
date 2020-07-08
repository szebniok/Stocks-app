package com.example.stocks.stock_favourites;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stocks.R;
import com.example.stocks.StockRecyclerViewFragment;
import com.example.stocks.databinding.ActivityStockFavouritesBinding;
import com.example.stocks.domain.Stock;
import com.example.stocks.stock_details.StockDetailsActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;

@DataBound
@EActivity(R.layout.activity_stock_favourites)
public class StockFavouritesActivity extends AppCompatActivity {

    @Bean
    StockFavouritesViewModel viewModel;

    @FragmentById(R.id.stockFavouritesRecyclerViewFragment)
    StockRecyclerViewFragment fragment;

    @BindingObject
    ActivityStockFavouritesBinding binding;

    @AfterViews
    void setup() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);


        fragment.setItemClickHandler(this::handleClick);

        viewModel.stocks.observe(this, stocks -> {
            fragment.updateStocks(stocks);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getFavourites();
    }

    void handleClick(Stock stock) {
        Intent intent = new Intent(this, StockDetailsActivity_.class);
        intent.putExtra("symbol", stock.getSymbol());
        startActivity(intent);
    }
}
