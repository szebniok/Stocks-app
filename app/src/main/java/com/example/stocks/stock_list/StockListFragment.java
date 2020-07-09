package com.example.stocks.stock_list;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.stocks.R;
import com.example.stocks.StockRecyclerViewFragment;
import com.example.stocks.StockRecyclerViewFragment_;
import com.example.stocks.databinding.FragmentStockListBinding;
import com.example.stocks.domain.Stock;
import com.example.stocks.stock_details.StockDetailsActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;

@DataBound
@EFragment(R.layout.fragment_stock_list)
public class StockListFragment extends Fragment {
    @Bean
    StockListViewModel stockListViewModel;

    @BindingObject
    FragmentStockListBinding binding;

    @AfterViews
    void setBinding() {
        binding.setViewmodel(stockListViewModel);
        binding.setLifecycleOwner(this);

        StockRecyclerViewFragment_ fragment = new StockRecyclerViewFragment_();

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.stockListRecyclerViewFragment, fragment)
                .commit();

        fragment.setItemClickHandler(this::handleClick);

        stockListViewModel.getStocks();
        stockListViewModel.stocks.observe(this, stocks -> {
            fragment.updateStocks(stocks);
        });
    }

    public void handleClick(Stock stock) {
        Intent intent = new Intent(getActivity(), StockDetailsActivity_.class);
        intent.putExtra("symbol", stock.getSymbol());
        startActivity(intent);
    }
}
