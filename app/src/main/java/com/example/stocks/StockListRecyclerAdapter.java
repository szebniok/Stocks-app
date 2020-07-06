package com.example.stocks;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StockListRecyclerAdapter extends RecyclerView.Adapter<StockListRecyclerAdapter.ViewHolder> {
    OnStockListItemClick onStockListItemClick;
    private List<Stock> stocks;

    public StockListRecyclerAdapter(OnStockListItemClick onStockListItemClick) {
        this.onStockListItemClick = onStockListItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_item, parent, false);
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

        private void bindStock(Stock stock) {
            TextView symbolTextView = view.findViewById(R.id.stockListItemSymbol);
            TextView shortNameTextView = view.findViewById(R.id.stockListItemShortName);

            symbolTextView.setText(stock.getSymbol());
            shortNameTextView.setText(stock.getShortName());
        }

        @Override
        public void onClick(View view) {
            System.out.println("TEST");
            onStockListItemClick.handleClick(getAdapterPosition());
        }
    }

    interface OnStockListItemClick {
        void handleClick(int position);
    }


}
