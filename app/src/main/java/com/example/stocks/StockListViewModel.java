package com.example.stocks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@EBean
public class StockListViewModel extends ViewModel {
    @Bean
    private StockMarketRepository stockMarketRepository;
    private Disposable disposable = new CompositeDisposable();
    MutableLiveData<List<Stock>> stocks = new MutableLiveData<>();
    MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public void getStocks() {
        disposable = stockMarketRepository.getSummary().doOnSuccess(data -> stocks.postValue(data))
                .doOnError(Throwable::printStackTrace)
                .doOnSubscribe(v -> loading.postValue(true))
                .doAfterSuccess(v -> loading.postValue(false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }


}
