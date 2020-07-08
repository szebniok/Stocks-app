package com.example.stocks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stocks.stock_favourites.StockFavouritesFragment_;
import com.example.stocks.stock_list.StockListFragment_;
import com.example.stocks.stock_search.StockSearchFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewById(R.id.pager)
    ViewPager2 viewPager;

    private FragmentStateAdapter adapter;

    @AfterViews
    public void setup() {
        adapter = new MainActivityFragmentStateAdapter(this);
        viewPager.setAdapter(adapter);
    }

    private class MainActivityFragmentStateAdapter extends FragmentStateAdapter {
        public MainActivityFragmentStateAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int position) {
            switch(position) {
                case 0:
                    return new StockListFragment_();
                case 1:
                    return new StockSearchFragment_();
                default:
                    return new StockFavouritesFragment_();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}