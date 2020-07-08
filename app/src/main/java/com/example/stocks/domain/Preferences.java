package com.example.stocks.domain;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

import java.util.Set;

@SharedPref(SharedPref.Scope.APPLICATION_DEFAULT)
public interface Preferences {
    Set<String> favouriteSymbols();
}
