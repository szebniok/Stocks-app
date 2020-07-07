package com.example.stocks;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stocks.databinding.ActivityStockDetailsBinding;
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

@EActivity(R.layout.activity_stock_details)
@DataBound
public class StockDetailsActivity extends AppCompatActivity {

    @BindingObject
    ActivityStockDetailsBinding binding;

    @Bean
    StockDetailsViewModel viewModel;

    @ViewById
    LineChart chart;

    @AfterViews
    public void setBinding() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        String symbol = getIntent().getStringExtra("symbol");
        viewModel.getQuote(symbol);

        viewModel.stock.observe(this, stock -> {
            List<Entry> entries = new ArrayList<>();
            for (int i = 0; i < stock.getTimestamps().size(); i++) {
                BigDecimal d = stock.getTimestamps().get(i);
                entries.add(new Entry(i, d != null ? d.floatValue() : entries.get(i-1).getY()));
            }
            LineDataSet dataSet = new LineDataSet(entries, "price");
            dataSet.setColor(R.color.colorPrimaryDark);
            LineData lineData = new LineData(dataSet);
            lineData.setDrawValues(false);
            chart.setData(lineData);
            chart.invalidate();
        });
    }
}
