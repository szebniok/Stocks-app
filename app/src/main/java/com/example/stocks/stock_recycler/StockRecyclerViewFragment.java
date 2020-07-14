package com.example.stocks.stock_recycler;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocks.R;
import com.example.stocks.databinding.FragmentStockRecyclerViewBinding;
import com.example.stocks.domain.Stock;
import com.example.stocks.stock_details.StockDetailsActivity;
import com.example.stocks.stock_details.StockDetailsActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.subjects.BehaviorSubject;

@DataBound
@EFragment(R.layout.fragment_stock_recycler_view)
public class StockRecyclerViewFragment extends Fragment {
    public enum ListType {
        SUMMARY("SUMMARY"), FAVOURITES("FAVOURITES");

        final String encodedType;

        ListType(String type) {
            this.encodedType = type;
        }
    }

    @BindingObject
    FragmentStockRecyclerViewBinding binding;

    @ViewById(R.id.stockRecyclerView)
    RecyclerView recyclerView;

    @Bean
    StockRecyclerViewViewModel viewModel;

    private List<Stock> stocks;
    private List<Stock> originalStocks;
    private StockRecyclerViewAdapter adapter;

    private ListType type;
    private BehaviorSubject<String> searchSubject;

    public static StockRecyclerViewFragment_ newInstance(ListType type, BehaviorSubject<String> searchSubject) {
        StockRecyclerViewFragment_ newFragment = new StockRecyclerViewFragment_();
        newFragment.setType(type);
        newFragment.setSearchSubject(searchSubject);
        return newFragment;
    }

    @AfterViews
    void setup() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        adapter = new StockRecyclerViewAdapter(this::handleClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupItemSwipeHandler();

        showDefaultResults();

        viewModel.stocks.observe(this, this::updateStocks);
        searchSubject.subscribe(this::handleSearchTextChange);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (type == ListType.FAVOURITES) {
            showDefaultResults();
        }
    }

    public void handleClick(int position) {
        Intent intent = new Intent(getActivity(), StockDetailsActivity_.class);
        intent.putExtra(StockDetailsActivity.SYMBOL, stocks.get(position).getSymbol());
        startActivity(intent);
    }

    public void setType(ListType type) {
        this.type = type;
    }

    public void setSearchSubject(BehaviorSubject<String> searchSubject) {
        this.searchSubject = searchSubject;
    }

    private void showDefaultResults() {
        switch (this.type) {
            case SUMMARY:
                viewModel.getSummary();
                break;
            case FAVOURITES:
                viewModel.getFavourites();
                break;
        }
    }

    private void handleSearchTextChange(String query) {
        if (query.equals("")) {
            showDefaultResults();
        } else if (type == ListType.SUMMARY) {
            viewModel.search(query);
        } else if (type == ListType.FAVOURITES) {
            filterStocks(query);
            adapter.updateStocks(stocks);
        }
    }

    private void updateStocks(List<Stock> stocks) {
        this.originalStocks = stocks;
        this.stocks = stocks;

        // we have fetched new data about favourite symbols, so we need to filter them
        if (type == ListType.FAVOURITES) {
            filterStocks(searchSubject.getValue());
        }

        adapter.updateStocks(this.stocks);
    }

    private void filterStocks(String query) {
        if (stocks == null) return;

        stocks = originalStocks
                .stream()
                .filter(stock -> stock.getShortName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    private void setupItemSwipeHandler() {
        int swipeDirection = type == ListType.SUMMARY ? ItemTouchHelper.RIGHT : ItemTouchHelper.LEFT;

        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, swipeDirection) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Stock swipedStock = stocks.get(viewHolder.getAdapterPosition());
                viewModel.toggleFavourites(swipedStock.getSymbol());

                // we need to notify the adapter to get the swiped item to restore its original position
                // we only want to restore original position when we are in 'summary' view
                // https://stackoverflow.com/a/32159154
                if (type == ListType.SUMMARY) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                } else {
                    stocks.remove(viewHolder.getAdapterPosition());
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                }
            }
        };

        new ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView);
    }
}
