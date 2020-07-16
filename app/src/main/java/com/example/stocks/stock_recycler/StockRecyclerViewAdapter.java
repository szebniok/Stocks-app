package com.example.stocks.stock_recycler;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocks.R;
import com.example.stocks.domain.Stock;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;

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
        DiffUtil.Callback callback = new DiffCallback(this.stocks, stocks);
        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(callback);
        this.stocks = stocks;
        diff.dispatchUpdatesTo(this);
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

    @AllArgsConstructor
    private class DiffCallback extends DiffUtil.Callback {
        private List<Stock> oldList;
        private List<Stock> newList;

        @Override
        public int getOldListSize() {
            return oldList != null ? oldList.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getSymbol().equals(newList.get(newItemPosition).getSymbol());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }

    static class ItemAnimator extends DefaultItemAnimator {
        @Override
        public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
            super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY);

            TextView oldPriceTextView = oldHolder.itemView.findViewById(R.id.stockListItemPrice);
            TextView newPriceTextView = newHolder.itemView.findViewById(R.id.stockListItemPrice);

            if (oldPriceTextView.getText().toString().equals("")) return false;

            BigDecimal oldPrice = new BigDecimal(oldPriceTextView.getText().toString());
            BigDecimal newPrice = new BigDecimal(newPriceTextView.getText().toString());

            if (oldPrice.compareTo(newPrice) == 0) return false;

            int color = oldPrice.compareTo(newPrice) < 0 ? Color.GREEN : Color.RED;
            int oldTextColor = oldPriceTextView.getCurrentTextColor();

            ObjectAnimator animator = ObjectAnimator.ofInt(newPriceTextView, "textColor", color, oldTextColor);
            animator.setDuration(1000);
            animator.setEvaluator(new ArgbEvaluator());
            animator.start();

            return true;
        }
    }
}
