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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.lens.R;
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
import java.util.Objects;

public class LanguageTranslationFragment extends Fragment {
    FragmentLanguageTranslationBinding binding;
    String[] languages={"AFRIKAANS","ALBANIAN","ARABIC","BELARUSIAN","BULGARIAN","BENGALI","CATALAN","CHINESE","CROATIAN","CZECH","DANISH","DUTCH","ENGLISH","ESPERANTO","ESTONIAN","FINNISH","FRENCH","GALICIAN","GEORGIAN","GERMAN","GREEK","GUJARATI","HAITIAN_CREOLE","HEBREW","HINDI","HUNGARIAN","ICELANDIC","INDONESIAN","IRISH","ITALIAN","JAPANESE","KANNADA"
    ,"KOREAN","LITHUANIAN","LATVIAN","MACEDONIAN","MARATHI","MALAY","MALTESE","NORWEGIAN","PERSIAN","POLISH","PORTUGUESE","ROMANIAN","RUSSIAN","SLOVAK","SLOVENIAN","SPANISH","SWEDISH","SWAHILI"
    ,"TAGALOG","TAMIL","TELUGU","THAI","TURKISH","UKRAINIAN","URDU","VIETNAMESE","WELSH"};
    List<String> languageListSource=new ArrayList<>();
    List<String> languageListDest=new ArrayList<>();
    ArrayAdapter arrayAdapter;
    String srcLangauge,destLanguage;
    TranslatorOptions options;
    Translator translator;
    public LanguageTranslationFragment() {
        languageListSource=TranslateLanguage.getAllLanguages();
        languageListDest=TranslateLanguage.getAllLanguages();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentLanguageTranslationBinding.inflate(inflater);

        ActivityResultLauncher<Intent> launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
            if (o.getResultCode()== RESULT_OK && o.getData()!=null){
                Intent data=o.getData();
                translatortext(Objects.requireNonNull(Objects.requireNonNull(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS))).get(0));
            }
        });
        arrayAdapter=new ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item,languages);
        binding.sourcespinner.setAdapter(arrayAdapter);
        binding.destspinner.setAdapter(arrayAdapter);

        binding.sourcespinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                srcLangauge=binding.sourcespinner.getText().toString();
            }
        });
        binding.destspinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                destLanguage=binding.destspinner.getText().toString();
            }
        });

        binding.translatebtn.setOnClickListener(view -> {
            if (!Objects.requireNonNull(binding.sourcetext.getText()).toString().isEmpty()){
                translatortext(binding.sourcetext.getText().toString());
            } else {
                showSnackBar("Please enter some text");
            }
        });
        binding.mic.setOnClickListener(view -> {
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
        });
        return binding.getRoot();
    }

    public void translatortext(String s){
        binding.translatedtext.setText("");
        options = new TranslatorOptions.Builder().setSourceLanguage(srcLangauge).setTargetLanguage(destLanguage).build();
        translator =Translation.getClient(options);
        getLifecycle().addObserver(translator);
        DownloadConditions conditions = new DownloadConditions.Builder().build();
        translator.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                translator.translate(binding.sourcetext.getText().toString()).addOnSuccessListener(s1 -> {
                    binding.translatedtext.setText(s1);
                    translator.close();
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
    }
    private void showSnackBar(String text){
        Snackbar snackbar = Snackbar.make(binding.frameLayout3, text, Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", view -> snackbar.dismiss());
        snackbar.show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}