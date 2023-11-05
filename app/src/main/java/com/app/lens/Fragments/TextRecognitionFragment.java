package com.app.lens.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.lens.R;
import com.app.lens.databinding.FragmentTextRecognitionBinding;

public class TextRecognitionFragment extends Fragment {
    FragmentTextRecognitionBinding binding;
    public TextRecognitionFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentTextRecognitionBinding.inflate(inflater);
        return binding.getRoot();
    }
}