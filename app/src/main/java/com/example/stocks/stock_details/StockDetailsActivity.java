package com.example.stocks.stock_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.stocks.R;
import com.example.stocks.databinding.FragmentStockDetailsBinding;
import com.example.stocks.domain.Stock;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.fragment_stock_details)
@DataBound
public class StockDetailsActivity extends AppCompatActivity {

    @BindingObject
    FragmentStockDetailsBinding binding;

    @Bean
    StockDetailsViewModel viewModel;

    @ViewById
    LineChart chart;

    @AfterViews
    public void setBinding() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        String symbol = getIntent().getStringExtra("symbol");
        viewModel.getQuoteAndChart(symbol);

        viewModel.stock.observe(this, stock -> {
            createChart(stock);
        });
    }

    private void createChart(Stock stock) {
        List<BigDecimal> timestamps = stock.getTimestamps();
        if (timestamps == null) return;

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < stock.getTimestamps().size(); i++) {
            BigDecimal d = stock.getTimestamps().get(i);
            entries.add(new Entry(i, d != null ? d.floatValue() : entries.get(i - 1).getY()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "price");
        dataSet.setColor(R.color.colorPrimaryDark);

        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(false);

        chart.setData(lineData);
        chart.invalidate();
    }

    @Click(R.id.favouritesStar)
    void onFavouritesStarClick() {
        viewModel.toggleFavourites(viewModel.stock.getValue());
    }
}
