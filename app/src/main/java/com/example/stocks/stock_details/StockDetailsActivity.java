package com.example.stocks.stock_details;

import androidx.appcompat.app.AppCompatActivity;

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
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.fragment_stock_details)
@DataBound
public class StockDetailsActivity extends AppCompatActivity {
    public static final String SYMBOL = "SYMBOL";

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

        String symbol = getIntent().getStringExtra(SYMBOL);
        viewModel.getQuoteAndChart(symbol);

        viewModel.stock.observe(this, this::createChart);
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
        dataSet.setLineWidth(3);
        dataSet.setDrawCircles(false);
        dataSet.setDrawFilled(true);

        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(false);

        chart.setData(lineData);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawLabels(false);
        chart.invalidate();
    }
}
