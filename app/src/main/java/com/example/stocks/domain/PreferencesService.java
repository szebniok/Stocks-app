package com.example.stocks.domain;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EBean
public class PreferencesService {

    @Pref
    Preferences_ preferences;

    public void toggleFavouriteStatus(String symbol) {
        List<String> favourites = getFavouriteSymbols();

        if (isFavourite(symbol)) {
            favourites.remove(symbol);
        } else {
            favourites.add(symbol);
        }

        saveFavouriteSymbols(favourites);
    }

    public boolean isFavourite(String symbol) {
        return getFavouriteSymbols().contains(symbol);
    }

    public List<String> getFavouriteSymbols() {
        String joinedFavouriteSymbols = preferences.favouriteSymbols().get();
        if (preferences == null || joinedFavouriteSymbols.equals("")) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(joinedFavouriteSymbols.split(",")));
    }

    private void saveFavouriteSymbols(List<String> favourites) {
        preferences.favouriteSymbols().put(String.join(",", favourites));
    }
}
