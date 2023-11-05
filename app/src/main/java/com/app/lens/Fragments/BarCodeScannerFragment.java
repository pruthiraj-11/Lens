package com.app.lens.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.lens.R;
import com.app.lens.databinding.FragmentBarCodeScannerBinding;

public class BarCodeScannerFragment extends Fragment {
    FragmentBarCodeScannerBinding binding;
    public BarCodeScannerFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentBarCodeScannerBinding.inflate(inflater);

        return binding.getRoot();
    }
}