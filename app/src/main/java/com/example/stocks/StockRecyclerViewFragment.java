package com.example.stocks;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocks.StockListRecyclerAdapter.OnStockListItemClick;
import com.example.stocks.domain.Stock;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.function.Consumer;

@EFragment(R.layout.fragment_stock_recycler_view)
public class StockRecyclerViewFragment extends Fragment {
    private Consumer<Stock> onStockListItemClick;
    private List<Stock> stocks;
    private StockListRecyclerAdapter adapter;

    @ViewById(R.id.stockRecyclerView)
    RecyclerView recyclerView;

    @AfterViews
    void setup() {
        adapter = new StockListRecyclerAdapter(this::handleClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setItemClickHandler(Consumer<Stock> onStockListItemClick) {
        this.onStockListItemClick = onStockListItemClick;
    }

    public void updateStocks(List<Stock> stocks) {
        this.stocks = stocks;
        adapter.updateStocks(stocks);
    }

    public void handleClick(int position) {
        if (onStockListItemClick != null) {
            onStockListItemClick.accept(stocks.get(position));
        }
    }
}
