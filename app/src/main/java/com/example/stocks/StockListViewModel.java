package com.example.stocks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.androidannotations.annotations.Bean;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StockListViewModel extends ViewModel {
    private StockMarketRepository stockMarketRepository;


    public StockListViewModel() {
        stockMarketRepository = new StockMarketRepository();
    }

    private MutableLiveData<List<Stock>> stocks;

    public LiveData<List<Stock>> getStocks() {
        if (stocks == null) {
            stocks = new MutableLiveData<>();

            stockMarketRepository.getSummary().doOnSuccess(data -> stocks.postValue(data))
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        }
        return stocks;
    }
}
