package com.example.stocks;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stocks.stock_recycler.StockRecyclerViewFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {
    @ViewById(R.id.pager)
    ViewPager2 viewPager;

    @ViewById
    TabLayout tabLayout;

    private FragmentStateAdapter adapter;

    private static StockRecyclerViewFragment viewPagerFragments[] = {
            StockRecyclerViewFragment.newInstance(StockRecyclerViewFragment.ListType.SUMMARY),
            StockRecyclerViewFragment.newInstance(StockRecyclerViewFragment.ListType.FAVOURITES)
    };
    private static final String TABS_NAMES[] = {"Summary", "Favourites"};

    @AfterViews
    public void setup() {
        adapter = new MainActivityFragmentStateAdapter(getActivity());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(TABS_NAMES[position])).attach();
    }

    public void showSearchResults(String query) {
        StockRecyclerViewFragment currentFragment = viewPagerFragments[viewPager.getCurrentItem()];

        currentFragment.showSearchResults(query);
    }

    public void showDefaultResults() {
        StockRecyclerViewFragment currentFragment = viewPagerFragments[viewPager.getCurrentItem()];

        currentFragment.showDefaultResults();
    }

    private class MainActivityFragmentStateAdapter extends FragmentStateAdapter {
        public MainActivityFragmentStateAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int position) {
            return viewPagerFragments[position];
        }

        @Override
        public int getItemCount() {
            return TABS_NAMES.length;
        }
    }
}
