package com.example.stocks.stock_recycler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocks.domain.PreferencesService;
import com.example.stocks.domain.Stock;
import com.example.stocks.domain.StockMarketService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@EBean
public class StockRecyclerViewViewModel extends ViewModel {
    public MutableLiveData<List<Stock>> stocks = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();

    @Bean
    StockMarketService service;

    @Bean
    PreferencesService preferencesService;

    private CompositeDisposable disposable = new CompositeDisposable();

    void getFavourites() {
        List<String> favourites = preferencesService.getFavouriteSymbols();

        disposable.add(
                service.getQuotes(favourites)
                        .doOnSubscribe(v -> loading.postValue(true))
                        .doOnSuccess(v -> loading.postValue(false))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(quotes -> stocks.postValue(quotes))
        );
    }

    public void getSummary() {
        disposable.add(
                service.getSummary()
                        .doOnSubscribe(v -> loading.postValue(true))
                        .doAfterSuccess(v -> loading.postValue(false))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(data -> stocks.postValue(data))
        );
    }

    public void search(String name) {
        disposable.add(
                service.autoComplete(name)
                        .doOnSubscribe(v -> loading.postValue(true))
                        .doAfterSuccess(v -> loading.postValue(false))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> stocks.postValue(s))
        );
    }

    public void toggleFavourites(String symbol) {
        preferencesService.toggleFavouriteStatus(symbol);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
