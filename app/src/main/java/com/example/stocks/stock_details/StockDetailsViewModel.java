package com.example.stocks.stock_details;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocks.domain.PreferencesService;
import com.example.stocks.domain.Stock;
import com.example.stocks.domain.StockMarketService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


@EBean
public class StockDetailsViewModel extends ViewModel {
    public MutableLiveData<Stock> stock = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<Boolean> favourite = new MutableLiveData<>();

    Disposable disposable;

    @Bean
    StockMarketService service;

    @Bean
    PreferencesService preferencesService;

    public void getQuoteAndChart(String symbol) {
        disposable = service.getQuoteAndChart(symbol)
                .doOnSubscribe(v -> loading.postValue(true))
                .doAfterSuccess(v -> loading.postValue(false))
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quote -> {
                    stock.postValue(quote);
                    favourite.postValue(preferencesService.isFavourite(quote.getSymbol()));
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    void toggleFavourites(Stock stock) {
        String toggledSymbol = stock.getSymbol();

        preferencesService.toggleFavouriteStatus(toggledSymbol);
        favourite.postValue(preferencesService.isFavourite(toggledSymbol));
    }
}
