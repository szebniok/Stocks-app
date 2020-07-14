package com.example.stocks.stock_recycler;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocks.R;
import com.example.stocks.domain.Stock;

import java.math.BigDecimal;
import java.util.List;

public class StockRecyclerViewAdapter extends RecyclerView.Adapter<StockRecyclerViewAdapter.ViewHolder> {
    OnStockListItemClick onStockListItemClick;
    private List<Stock> stocks;

    public StockRecyclerViewAdapter(OnStockListItemClick onStockListItemClick) {
        this.onStockListItemClick = onStockListItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_stock_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Stock stock = stocks.get(position);
        holder.bindStock(stock);
    }

    @Override
    public int getItemCount() {
        return stocks == null ? 0 : stocks.size();
    }

    public void updateStocks(List<Stock> stocks) {
        this.stocks = stocks;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onStockListItemClick.handleClick(getAdapterPosition());
        }

        private void bindStock(Stock stock) {
            TextView symbolTextView = view.findViewById(R.id.stockListItemSymbol);
            TextView shortNameTextView = view.findViewById(R.id.stockListItemShortName);
            TextView priceTextView = view.findViewById(R.id.stockListItemPrice);
            ImageView star = view.findViewById(R.id.stockListItemStar);

            String formattedPrice =
                    stock.getRegularMarketPrice() != null ?
                            stock.getRegularMarketPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                            : null;

            symbolTextView.setText(stock.getSymbol());
            shortNameTextView.setText(stock.getShortName());
            priceTextView.setText(formattedPrice);
            star.setVisibility(stock.getFavourite() ? View.VISIBLE : View.GONE);
        }

    }

    public interface OnStockListItemClick {
        void handleClick(int position);
    }
}
