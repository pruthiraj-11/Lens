package com.app.lens.Activities;

import android.os.Bundle;
import android.text.Html;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.lens.Fragments.BarCodeScannerFragment;
import com.app.lens.Fragments.HandwritingRecognitionFragment;
import com.app.lens.Fragments.LanguageIdentifierFragment;
import com.app.lens.Fragments.LanguageTranslationFragment;
import com.app.lens.Fragments.QRCodeScannerFragment;
import com.app.lens.Fragments.TextRecognitionFragment;
import com.app.lens.R;
import com.app.lens.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color=\"white\">" + "Lens" + "</font>"));

        binding.chipBarCode.setOnClickListener(view -> {
            Fragment fragment=new BarCodeScannerFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
            fragmentTransaction.commit();
        });
        binding.chipQRCode.setOnClickListener(view -> {
            Fragment fragment=new QRCodeScannerFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
            fragmentTransaction.commit();
        });
        binding.chipDigitalInk.setOnClickListener(view -> {
            Fragment fragment=new HandwritingRecognitionFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
            fragmentTransaction.commit();
        });
        binding.chipTextRecognition.setOnClickListener(view -> {
            Fragment fragment=new TextRecognitionFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
            fragmentTransaction.commit();
        });
        binding.chipLanguageIdentification.setOnClickListener(view -> {
            Fragment fragment=new LanguageIdentifierFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
            fragmentTransaction.commit();
        });
        binding.chipTranslation.setOnClickListener(view -> {
            Fragment fragment=new LanguageTranslationFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
            fragmentTransaction.commit();
        });
    }
}