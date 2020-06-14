package com.example.myapplication2.ui.vista;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication2.R;

public class VistaFragment extends Fragment {

    private VistaViewModel mViewModel;
    private View myInflatedView;

    public static VistaFragment newInstance() {
        return new VistaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.myInflatedView = inflater.inflate(R.layout.vista_fragment, container, false);
        return myInflatedView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VistaViewModel.class);
    }

}