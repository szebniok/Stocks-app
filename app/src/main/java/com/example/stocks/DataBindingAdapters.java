package com.example.stocks;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class DataBindingAdapters {
    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
