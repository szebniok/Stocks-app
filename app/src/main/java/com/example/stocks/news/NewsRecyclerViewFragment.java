package com.example.stocks.news;

import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocks.R;
import com.example.stocks.databinding.FragmentNewsRecyclerViewBinding;
import com.rometools.rome.feed.synd.SyndEntry;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@DataBound
@EFragment(R.layout.fragment_news_recycler_view)
public class NewsRecyclerViewFragment extends Fragment {

    @BindingObject
    FragmentNewsRecyclerViewBinding binding;

    @Bean
    NewsRecyclerViewViewModel viewModel;

    @ViewById(R.id.newsRecyclerView)
    RecyclerView recyclerView;

    private NewsRecyclerViewAdapter adapter;

    @AfterViews
    void setup() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        adapter = new NewsRecyclerViewAdapter(this::handleNewsListItemClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getNews();
        viewModel.news.observe(this, adapter::updateNews);
    }

    private void handleNewsListItemClick(int i) {
        SyndEntry clickedEntry = viewModel.news.getValue().get(i);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickedEntry.getLink()));
        startActivity(intent);
    }
}
