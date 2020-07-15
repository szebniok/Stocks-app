package com.example.stocks;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stocks.stock_recycler.StockRecyclerViewFragment;
import com.example.stocks.stock_recycler.StockRecyclerViewFragment.ListType;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import io.reactivex.subjects.BehaviorSubject;

@EFragment(R.layout.fragment_stock)
public class StockFragment extends Fragment {
    @ViewById(R.id.pager)
    ViewPager2 viewPager;

    @ViewById
    TabLayout tabLayout;

    private FragmentStateAdapter adapter;

    private static final String[] TABS_NAMES = {"Summary", "Favourites"};

    private BehaviorSubject<String> searchSubject = BehaviorSubject.createDefault("");

    @AfterViews
    public void setup() {
        adapter = new MainActivityFragmentStateAdapter(getActivity());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(TABS_NAMES[position])).attach();
    }

    public void showSearchResults(String query) {
        searchSubject.onNext(query);
    }

    public void showDefaultResults() {
        searchSubject.onNext("");
    }

    private class MainActivityFragmentStateAdapter extends FragmentStateAdapter {
        public MainActivityFragmentStateAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return StockRecyclerViewFragment.newInstance(ListType.SUMMARY, searchSubject);
                default:
                    return StockRecyclerViewFragment.newInstance(ListType.FAVOURITES, searchSubject);
            }
        }

        @Override
        public int getItemCount() {
            return TABS_NAMES.length;
        }
    }
}
