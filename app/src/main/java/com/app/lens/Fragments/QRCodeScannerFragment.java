package com.app.lens.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.lens.R;
import com.app.lens.databinding.FragmentQRCodeScannerBinding;
import com.budiyev.android.codescanner.CodeScanner;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class QRCodeScannerFragment extends Fragment {

    FragmentQRCodeScannerBinding binding;
    private CodeScanner mCodeScanner;
    TextView qrcoderesult;
    ImageView backbtn;
    View inflated;
    public QRCodeScannerFragment() {
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentQRCodeScannerBinding.inflate(inflater);

        final Activity activity = getActivity();

        mCodeScanner = new CodeScanner(Objects.requireNonNull(activity), binding.scannerView);
        mCodeScanner.setDecodeCallback(result -> activity.runOnUiThread(() -> {
            Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
            inflated = binding.viewStub2.inflate();
            qrcoderesult= inflated.findViewById(R.id.qrcodetextview);
            qrcoderesult.setText(result.getText());
//            mCodeScanner.startPreview();
        }));
//        backbtn=inflated.findViewById(R.id.backbtn);
//        backbtn.setOnClickListener(view -> {
//            binding.viewStub2.setVisibility(View.GONE);
//            mCodeScanner.startPreview();
//        });

        ActivityResultLauncher<String> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.GetContent(), o -> {
            if(o!=null){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), o);
                    InputImage inputImage=InputImage.fromBitmap(bitmap,0);
                    scanBarcodes(inputImage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(getContext(),"Image not picked",Toast.LENGTH_SHORT).show();
            }
        });
        binding.browsegalleryqrcode.setOnClickListener(view -> activityResultLauncher.launch("image/*"));
        return binding.getRoot();
    }

    private void scanBarcodes(InputImage image) {
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC).build();
        BarcodeScanner scanner = BarcodeScanning.getClient();
        Task<List<Barcode>> result = scanner.process(image).addOnSuccessListener(barcodes -> {
            for (Barcode barcode: barcodes) {
                Rect bounds = barcode.getBoundingBox();
                Point[] corners = barcode.getCornerPoints();
                String rawValue = barcode.getRawValue();
                int valueType = barcode.getValueType();
                switch (valueType) {
                    case Barcode.TYPE_WIFI:
                        String ssid = Objects.requireNonNull(barcode.getWifi()).getSsid();
                        String password = barcode.getWifi().getPassword();
                        int type = barcode.getWifi().getEncryptionType();
                        break;
                    case Barcode.TYPE_URL:
                        String title = Objects.requireNonNull(barcode.getUrl()).getTitle();
                        String url = barcode.getUrl().getUrl();
                        break;
                    case Barcode.TYPE_TEXT:
                        break;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
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