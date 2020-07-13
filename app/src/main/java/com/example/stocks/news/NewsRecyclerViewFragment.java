package com.example.stocks.news;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocks.R;
import com.example.stocks.databinding.FragmentNewsRecyclerViewBinding;

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

        adapter = new NewsRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getNews();
        viewModel.news.observe(this, adapter::updateNews);
    }
}
