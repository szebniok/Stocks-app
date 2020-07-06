package com.example.stocks;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocks.databinding.ActivityStockSearchBinding;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@DataBound
@EActivity(R.layout.activity_stock_search)
public class StockSearchActivity extends AppCompatActivity implements StockListRecyclerAdapter.OnStockListItemClick {
    @ViewById
    EditText searchEditText;

    @ViewById
    Button searchSubmit;

    @ViewById(R.id.searchResults)
    RecyclerView recyclerView;

    @BindingObject
    ActivityStockSearchBinding binding;

    @Bean
    StockSearchViewModel viewModel;

    private StockListRecyclerAdapter adapter;
    private List<Stock> searchResults;

    @AfterViews
    void setup() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        adapter = new StockListRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Click(R.id.searchSubmit)
    public void onSearchSubmit(View view) {
        String searchText = searchEditText.getText().toString();

        viewModel.search(searchText);
        viewModel.stocks.observe(this, stocks -> {
            this.searchResults = stocks;
            adapter.updateStocks(searchResults);
        });
    }

    @Override
    public void handleClick(int position) {
        Stock stock = searchResults.get(position);
        Intent intent = new Intent(this, StockDetailsActivity_.class);
        intent.putExtra("symbol", stock.getSymbol());
        startActivity(intent);
    }
}
