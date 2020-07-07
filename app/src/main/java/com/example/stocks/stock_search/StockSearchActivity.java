package com.example.stocks.stock_search;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stocks.R;
import com.example.stocks.StockRecyclerViewFragment;
import com.example.stocks.stock_details.StockDetailsActivity_;
import com.example.stocks.databinding.ActivityStockSearchBinding;
import com.example.stocks.domain.Stock;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;


@DataBound
@EActivity(R.layout.activity_stock_search)
public class StockSearchActivity extends AppCompatActivity {
    @ViewById
    EditText searchEditText;

    @ViewById
    Button searchSubmit;

    @FragmentById(R.id.stockSearchRecyclerViewFragment)
    StockRecyclerViewFragment fragment;

    @BindingObject
    ActivityStockSearchBinding binding;

    @Bean
    StockSearchViewModel viewModel;

    @AfterViews
    void setup() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        fragment.setItemClickHandler(this::handleClick);
    }

    @Click(R.id.searchSubmit)
    public void onSearchSubmit(View view) {
        String searchText = searchEditText.getText().toString();

        viewModel.search(searchText);
        viewModel.stocks.observe(this, stocks -> {
            fragment.updateStocks(stocks);
        });
    }

    public void handleClick(Stock stock) {
        Intent intent = new Intent(this, StockDetailsActivity_.class);
        intent.putExtra("symbol", stock.getSymbol());
        startActivity(intent);
    }
}
