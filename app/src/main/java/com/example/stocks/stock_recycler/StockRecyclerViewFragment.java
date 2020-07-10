package com.example.stocks.stock_recycler;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocks.R;
import com.example.stocks.databinding.FragmentStockRecyclerViewBinding;
import com.example.stocks.domain.Stock;
import com.example.stocks.stock_details.StockDetailsActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@DataBound
@EFragment(R.layout.fragment_stock_recycler_view)
public class StockRecyclerViewFragment extends Fragment {

    @BindingObject
    FragmentStockRecyclerViewBinding binding;

    public enum ListType {
        SUMMARY("SUMMARY"), FAVOURITES("FAVOURITES");

        final String encodedType;

        ListType(String type) {
            this.encodedType = type;
        }
    }

    private List<Stock> stocks;
    private StockRecyclerViewAdapter adapter;

    @ViewById(R.id.stockRecyclerView)
    RecyclerView recyclerView;

    @Bean
    StockRecyclerViewViewModel viewModel;

    private ListType type;

    public static StockRecyclerViewFragment_ newInstance(ListType type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type.encodedType);

        StockRecyclerViewFragment_ newFragment = new StockRecyclerViewFragment_();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public static StockRecyclerViewFragment_ newInstance(ListType type, String query) {
        StockRecyclerViewFragment_ newFragment = newInstance(type);

        Bundle bundle = newFragment.getArguments();
        bundle.putString("query", query);
        newFragment.setArguments(bundle);

        return newFragment;
    }

    @AfterViews
    void setup() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        String encodedType = getArguments().getString("type");
        setType(encodedType);

        adapter = new StockRecyclerViewAdapter(this::handleClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        showDefaultResults();

        viewModel.stocks.observe(this, this::updateStocks);
    }

    private void setType(String encodedType) {
        if (encodedType.equals(ListType.SUMMARY.encodedType)) {
            this.type = ListType.SUMMARY;
        } else if (encodedType.equals(ListType.FAVOURITES.encodedType)) {
            this.type = ListType.FAVOURITES;
        }
    }

    public void showSearchResults(String query) {
        viewModel.search(query);
    }

    public void showDefaultResults() {
        switch (this.type) {
            case SUMMARY:
                viewModel.getSummary();
                break;
            case FAVOURITES:
                viewModel.getFavourites();
                break;
        }
    }

    private void updateStocks(List<Stock> stocks) {
        this.stocks = stocks;
        adapter.updateStocks(stocks);
    }

    public void handleClick(int position) {
        Intent intent = new Intent(getActivity(), StockDetailsActivity_.class);
        intent.putExtra("symbol", stocks.get(position).getSymbol());
        startActivity(intent);
    }
}
