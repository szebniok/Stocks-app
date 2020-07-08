package com.example.stocks.domain;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EBean
public class PreferencesService {

    @Pref
    Preferences_ preferences;

    public void toggleFavouriteStatus(String symbol) {
        Set<String> favourites = preferences.favouriteSymbols().get();

        if (isFavourite(symbol)) {
            favourites.remove(symbol);
        } else {
            favourites.add(symbol);
        }

        // without this line settings are not persisted
        preferences.favouriteSymbols().remove();

        preferences.favouriteSymbols().put(favourites);
    }

    public boolean isFavourite(String symbol) {
        Set<String> favourites = preferences.favouriteSymbols().get();
        return favourites.contains(symbol);
    }

    public List<String> getFavouriteSymbols() {
        return new ArrayList<>(preferences.favouriteSymbols().get());
    }
}
