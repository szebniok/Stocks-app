package com.example.stocks;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.stocks.news.NewsRecyclerViewFragment_;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    Fragment currentFragment;

    @ViewById(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;

    @AfterViews
    void setup() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this::handleBottomNavigationItemSelect);

        switchFragment(new MainFragment_());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_search, menu);

        MenuItem menuItem = menu.findItem(R.id.search_icon);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (currentFragment instanceof MainFragment_) {
                    ((MainFragment_) currentFragment).showSearchResults(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(() -> {
            if (currentFragment instanceof MainFragment_) {
                ((MainFragment_) currentFragment).showDefaultResults();
            }
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void switchFragment(Fragment fragment) {
        this.currentFragment = fragment;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activityFragmentRoot, fragment)
                .commit();
    }

    private boolean handleBottomNavigationItemSelect(MenuItem item) {
        Fragment newFragment;

        switch (item.getItemId()) {
            case R.id.bottomNavigationStocks:
                newFragment = new MainFragment_();
                break;
            default:
                newFragment = new NewsRecyclerViewFragment_();
                break;
        }

        switchFragment(newFragment);

        return true;
    }
}