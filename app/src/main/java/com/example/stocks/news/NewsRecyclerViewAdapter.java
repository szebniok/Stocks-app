package com.example.stocks.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocks.R;
import com.rometools.rome.feed.synd.SyndEntry;

import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {
    private OnNewsListItemClick onNewsListItemClick;
    private List<SyndEntry> entries;

    public NewsRecyclerViewAdapter(OnNewsListItemClick onNewsListItemClick) {
        this.onNewsListItemClick = onNewsListItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SyndEntry entry = entries.get(position);
        holder.bindEntry(entry);
    }

    @Override
    public int getItemCount() {
        return entries == null ? 0 : entries.size();
    }

    public void updateNews(List<SyndEntry> entries) {
        this.entries = entries;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            view.setOnClickListener(this);
        }

        public void bindEntry(SyndEntry entry) {
            TextView titleView = view.findViewById(R.id.newsItemTitle);
            TextView contentView = view.findViewById(R.id.newsItemContent);

            titleView.setText(entry.getTitle());
            contentView.setText(entry.getDescription().getValue());
        }

        @Override
        public void onClick(View view) {
            onNewsListItemClick.handleClick(getAdapterPosition());
        }
    }

    public interface OnNewsListItemClick {
        void handleClick(int position);
    }
}
