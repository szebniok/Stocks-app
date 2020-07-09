package com.example.stocks;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocks.domain.PreferencesService;
import com.example.stocks.domain.Stock;
import com.example.stocks.domain.StockMarketService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@EBean
public class StockRecyclerViewViewModel extends ViewModel {
    public MutableLiveData<List<Stock>> stocks = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();

    @Bean
    StockMarketService service;

    @Bean
    PreferencesService preferencesService;

    void getFavourites() {
        List<String> favourites = preferencesService.getFavouriteSymbols();

        service.getQuotes(favourites)
                .doOnSubscribe(v -> loading.postValue(true))
                .doOnSuccess(v -> loading.postValue(false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quotes -> stocks.postValue(quotes));
    }

    public void getStocks() {
        service.getSummary().doOnSuccess(data -> stocks.postValue(data))
                .doOnError(Throwable::printStackTrace)
                .doOnSubscribe(v -> loading.postValue(true))
                .doAfterSuccess(v -> loading.postValue(false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void search(String name) {
        service.autoComplete(name)
                .doOnSubscribe(v -> loading.postValue(true))
                .doAfterSuccess(v -> loading.postValue(false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> stocks.postValue(s));
    }
}
