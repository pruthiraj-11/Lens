package com.app.lens.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.lens.R;
import com.app.lens.databinding.FragmentQRCodeScannerBinding;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class QRCodeScannerFragment extends Fragment {

    FragmentQRCodeScannerBinding binding;
    private CodeScanner mCodeScanner;
    public QRCodeScannerFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentQRCodeScannerBinding.inflate(inflater);

        final Activity activity = getActivity();
        mCodeScanner = new CodeScanner(activity, binding.scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
                        mCodeScanner.startPreview();
                    }
                });
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }
    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}