package com.app.lens.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.lens.databinding.FragmentQRCodeScannerBinding;

public class QRCodeScannerFragment extends Fragment {

    FragmentQRCodeScannerBinding binding;

    public QRCodeScannerFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentQRCodeScannerBinding.inflate(inflater);

        return binding.getRoot();
    }
}