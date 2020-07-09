package com.example.stocks.domain;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.APPLICATION_DEFAULT)
public interface Preferences {
    // favourite symbols separated by commas
    String favouriteSymbols();
}
