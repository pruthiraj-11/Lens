package com.app.lens.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.lens.R;
import com.app.lens.databinding.FragmentLanguageIdentifierBinding;
import com.app.lens.databinding.FragmentLanguageTranslationBinding;

public class LanguageIdentifierFragment extends Fragment {
    FragmentLanguageIdentifierBinding binding;

    public LanguageIdentifierFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLanguageIdentifierBinding.inflate(inflater);

        return binding.getRoot();
    }
}