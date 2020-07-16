package com.example.stocks.stock_recycler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocks.domain.PreferencesService;
import com.example.stocks.domain.Stock;
import com.example.stocks.domain.StockMarketService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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
    private Observable<Long> refreshTicks;
    private Disposable pendingSubscription;

    @AfterInject
    void setup() {
        refreshTicks = Observable.interval(preferencesService.getRefreshRateInSeconds(), TimeUnit.SECONDS).startWith(0L);
    }

    void getFavourites() {
        cancelPendingSubscription();

        List<String> favourites = preferencesService.getFavouriteSymbols();

        pendingSubscription = refreshTicks.flatMap(tick -> service.getQuotes(favourites).toObservable())
                .doOnSubscribe(v -> loading.postValue(true))
                .doAfterNext(v -> loading.postValue(false))
                .map(stocks -> stocks.stream().map(s -> {
                    s.setFavourite(true);
                    return s;
                }).collect(Collectors.toList()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quotes -> stocks.postValue(quotes));

        disposable.add(pendingSubscription);
    }

    public void getSummary() {
        cancelPendingSubscription();

        pendingSubscription = refreshTicks.flatMap(tick -> service.getSummary().toObservable())
                .doOnSubscribe(v -> loading.postValue(true))
                .doAfterNext(v -> loading.postValue(false))
                .map(stocks -> stocks.stream().map(s -> {
                    s.setFavourite(preferencesService.isFavourite(s.getSymbol()));
                    return s;
                }).collect(Collectors.toList()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> stocks.postValue(data));

        disposable.add(pendingSubscription);
    }

    public void search(String name) {
        cancelPendingSubscription();

        pendingSubscription = service.autoComplete(name)
                .doOnSubscribe(v -> loading.postValue(true))
                .doAfterSuccess(v -> loading.postValue(false))
                .map(stocks -> stocks.stream().map(s -> {
                    s.setFavourite(preferencesService.isFavourite(s.getSymbol()));
                    return s;
                }).collect(Collectors.toList()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> stocks.postValue(s));

        disposable.add(pendingSubscription);
    }

    public void toggleFavourites(Stock stock) {
        preferencesService.toggleFavouriteStatus(stock.getSymbol());
        stock.setFavourite(preferencesService.isFavourite(stock.getSymbol()));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }

    private void cancelPendingSubscription() {
        if (pendingSubscription != null) {
            pendingSubscription.dispose();
        }
    }
}
