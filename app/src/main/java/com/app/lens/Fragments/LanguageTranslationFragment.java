package com.app.lens.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.lens.databinding.FragmentLanguageTranslationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageTranslationFragment extends Fragment {
    FragmentLanguageTranslationBinding binding;
//    String[] fromLanguages={"From","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Welsh","Hindi","Urdu"};
//    String[] toLanguages={"To","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Welsh","Hindi","Urdu"};
    List<String> languageListSource=new ArrayList<>();
    List<String> languageListDest=new ArrayList<>();
    ArrayAdapter arrayAdapterSource,arrayAdapterDest;
    String srcLangauge,destLanguage;
    TranslatorOptions options;
    Translator translator;

    public LanguageTranslationFragment() {
        languageListSource=TranslateLanguage.getAllLanguages();
        languageListDest=TranslateLanguage.getAllLanguages();
//        languageListSource.add(0,"From");
//        languageListDest.add(0,"To");
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentLanguageTranslationBinding.inflate(inflater);

        ActivityResultLauncher<Intent> launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode()== RESULT_OK && o.getData()!=null){
                    Intent data=o.getData();
                    binding.sourcetext.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
                }
            }
        });
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
//                Toast.makeText(getContext(), languageListSource.get(i), Toast.LENGTH_SHORT).show();
                srcLangauge=languageListSource.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.destspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), languageListDest.get(i), Toast.LENGTH_SHORT).show();
                destLanguage=languageListDest.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.translatebtn.setOnClickListener(view -> {
            if (!binding.sourcetext.getText().toString().isEmpty()){
                binding.translatedtext.setText("");
                options = new TranslatorOptions.Builder().setSourceLanguage(srcLangauge).setTargetLanguage(destLanguage).build();
                translator =Translation.getClient(options);
                getLifecycle().addObserver(translator);
                DownloadConditions conditions = new DownloadConditions.Builder().build();
                translator.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        translator.translate(binding.sourcetext.getText().toString()).addOnSuccessListener(new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                binding.translatedtext.setText(s);
                                translator.close();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showSnackBar(e.getLocalizedMessage());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            } else {
                showSnackBar("Please enter some text");
            }
        });
        binding.mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.sourcetext.setText("");
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to translate");
                try {
                    launcher.launch(intent);
                } catch (ActivityNotFoundException e){
                    showSnackBar(e.getLocalizedMessage());
                }
            }
        });
        return binding.getRoot();
    }
    private void showSnackBar(String text){
        Snackbar snackbar = Snackbar.make(binding.frameLayout3, text, Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                            }
                        });
        snackbar.show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}