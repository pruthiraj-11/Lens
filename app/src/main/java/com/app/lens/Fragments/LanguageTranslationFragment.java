package com.app.lens.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.lens.databinding.FragmentLanguageTranslationBinding;
import com.google.mlkit.nl.translate.TranslateLanguage;

import java.util.ArrayList;
import java.util.List;

public class LanguageTranslationFragment extends Fragment {
    FragmentLanguageTranslationBinding binding;
//    String[] fromLanguages={"From","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Welsh","Hindi","Urdu"};
//    String[] toLanguages={"To","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Welsh","Hindi","Urdu"};
    private final static int REQUEST_CODE=74;
    int languageCode,toLanguageCode,fromLanguageCode=0;
    List<String> languageListSource=new ArrayList<>();
    List<String> languageListDest=new ArrayList<>();
    ArrayAdapter arrayAdapterSource,arrayAdapterDest;

    public LanguageTranslationFragment() {
        languageListSource=TranslateLanguage.getAllLanguages();
        languageListDest=TranslateLanguage.getAllLanguages();
//        languageListSource.add(0,"From");
//        languageListDest.add(0,"To");
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentLanguageTranslationBinding.inflate(inflater);

        arrayAdapterSource=new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,languageListSource);
        arrayAdapterSource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sourcespinner.setAdapter(arrayAdapterSource);
        arrayAdapterDest=new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,languageListDest);
        arrayAdapterDest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.destspinner.setAdapter(arrayAdapterDest);
//        String[] languageListFrom = new String[languageList.size()];
//        for(int i=0;i<languageList.size();i++){
//            languageListFrom[i]= languageList.get(i);
//        }
        binding.sourcespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                fromLanguageCode=getLangauge(fromLanguages[i]);
                Toast.makeText(getContext(), languageListSource.get(i), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.destspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                fromLanguageCode=getLangauge(fromLanguages[i]);
                Toast.makeText(getContext(), languageListDest.get(i), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.translatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return binding.getRoot();
    }
//    public int getLangauge(String language){
//        int langaugecode=0;
//        switch (language){
//            case "English":
//        }
//    }
}