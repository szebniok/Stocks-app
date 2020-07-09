package com.example.stocks.stock_favourites;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.stocks.R;
import com.example.stocks.StockRecyclerViewFragment;
import com.example.stocks.databinding.FragmentStockFavouritesBinding;
import com.example.stocks.domain.Stock;
import com.example.stocks.stock_details.StockDetailsActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;

@DataBound
@EFragment(R.layout.fragment_stock_favourites)
public class StockFavouritesFragment extends Fragment {

    @Bean
    StockFavouritesViewModel viewModel;

    @FragmentById(R.id.stockFavouritesRecyclerViewFragment)
    StockRecyclerViewFragment fragment;

    @BindingObject
    FragmentStockFavouritesBinding binding;

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
    public void onResume() {
        super.onResume();
        viewModel.getFavourites();
    }

    void handleClick(Stock stock) {
        Intent intent = new Intent(getActivity(), StockDetailsActivity_.class);
        intent.putExtra("symbol", stock.getSymbol());
        startActivity(intent);
    }
}
