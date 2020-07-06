package com.example.stocks;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


@EBean
public class StockDetailsViewModel extends ViewModel {
    public MutableLiveData<Stock> stock = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();

    Disposable disposable;

    @Bean
    StockMarketService service;

    public void getQuote(String symbol) {
        disposable = service.getQuote(symbol)
                .doOnSubscribe(v -> loading.postValue(true))
                .doAfterSuccess(v -> loading.postValue(false))
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quote -> stock.postValue(quote));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
