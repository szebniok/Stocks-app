package com.example.stocks.stock_favourites;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocks.domain.PreferencesService;
import com.example.stocks.domain.Stock;
import com.example.stocks.domain.StockMarketService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@EBean
public class StockFavouritesViewModel extends ViewModel {
    public MutableLiveData<List<Stock>> stocks = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();

    @Bean
    StockMarketService service;

    @Bean
    PreferencesService preferencesService;

    void getFavourites() {
        List<String> favourites = new ArrayList<>(preferencesService.getFavouriteSymbols());

        service.getQuotes(favourites)
                .doOnSubscribe(v -> loading.postValue(true))
                .doOnSuccess(v -> loading.postValue(false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quotes -> stocks.postValue(quotes));
    }

}
