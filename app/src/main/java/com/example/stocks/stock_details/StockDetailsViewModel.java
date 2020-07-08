package com.example.stocks.stock_details;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocks.domain.Preferences_;
import com.example.stocks.domain.Stock;
import com.example.stocks.domain.StockMarketService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.HashSet;
import java.util.Set;

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

    @Pref
    Preferences_ preferences;

    public void getQuoteAndChart(String symbol) {
        disposable = service.getQuoteAndChart(symbol)
                .doOnSubscribe(v -> loading.postValue(true))
                .doAfterSuccess(v -> loading.postValue(false))
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quote -> {
                    stock.postValue(quote);
                    favourite.postValue(isFavourite(quote.getSymbol()));
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
        Set<String> symbols = preferences.favouriteSymbols().get();

        if (isFavourite(toggledSymbol)) {
            symbols.remove(toggledSymbol);
            favourite.postValue(false);
        } else {
            symbols.add(toggledSymbol);
            favourite.postValue(true);
        }

        preferences.favouriteSymbols().remove();
        preferences.favouriteSymbols().put(symbols);
    }

    private boolean isFavourite(String symbol) {
        return preferences.favouriteSymbols().get().contains(symbol);
    }
}
