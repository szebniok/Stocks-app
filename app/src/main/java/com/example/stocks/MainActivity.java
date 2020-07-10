package com.example.stocks;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    MainFragment_ fragment;

    @AfterViews
    void setup() {
        fragment = new MainFragment_();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activityMainRoot, fragment, "mainFragment")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_search, menu);

        MenuItem menuItem = menu.findItem(R.id.search_icon);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fragment.showSearchResults(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(() -> {
            fragment.showDefaultResults();
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }
}