package com.app.lens.Fragments;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.app.lens.R;
import com.app.lens.databinding.FragmentTextRecognitionBinding;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class TextRecognitionFragment extends Fragment implements ImageAnalysis.Analyzer{
    FragmentTextRecognitionBinding binding;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    TextRecognizer textRecognizer;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};
    public TextRecognitionFragment() {}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentTextRecognitionBinding.inflate(inflater);

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        binding.viewFinder.post(() -> cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor()));

        ActivityResultLauncher<String> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.GetContent(), o -> {
            if(o!=null){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), o);
                    InputImage inputImage=InputImage.fromBitmap(bitmap,0);
                    detectText(inputImage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(getContext(),"Image not picked",Toast.LENGTH_SHORT).show();
            }
        });

        binding.browsegallery.setOnClickListener(view -> activityResultLauncher.launch("image/*"));
        binding.capture.setOnClickListener(view -> capturePhoto());
        return binding.getRoot();
    }

    public void detectText(InputImage image){
        textRecognizer= TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> task= textRecognizer.process(image).addOnSuccessListener(text -> {
            StringBuilder result=new StringBuilder();
            for(Text.TextBlock block:text.getTextBlocks()){
                String blockText= block.getText();
                Point[] blockCornerPoint= block.getCornerPoints();
                Rect blockFrame=block.getBoundingBox();
                for (Text.Line line : block.getLines()) {
                    String lineText = line.getText();
                    Point[] lineCornerPoints = line.getCornerPoints();
                    Rect lineFrame = line.getBoundingBox();
                    for (Text.Element element : line.getElements()) {
                        String elementText = element.getText();
                        result.append(elementText);
                    }
                    Toast.makeText(getContext(),result.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(getContext(),"Fail to detect text from image.",Toast.LENGTH_SHORT).show());
    }
    @Override
    public void analyze(@NonNull ImageProxy image) {
        image.close();
    }
    Executor getExecutor() {
        return ContextCompat.getMainExecutor(requireContext());
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        imageAnalysis.setAnalyzer(getExecutor(), this);
        Camera camera=cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
        binding.toggleflash.setOnClickListener(view -> toggleFlash(camera));
    }

    private void capturePhoto() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "text");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(requireContext().getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build(), getExecutor(),new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        try {
                            Uri imageUri = outputFileResults.getSavedUri();
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                            InputImage inputImage=InputImage.fromBitmap(bitmap,0);
                            detectText(inputImage);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
//                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(getContext(), "Error" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
    private void toggleFlash(@NonNull Camera camera) {
        if (camera.getCameraInfo().hasFlashUnit()) {
            if (camera.getCameraInfo().getTorchState().getValue() == 0) {
                camera.getCameraControl().enableTorch(true);
                binding.toggleflash.setImageResource(R.drawable.baseline_flash_off_24);
            } else {
                camera.getCameraControl().enableTorch(false);
                binding.toggleflash.setImageResource(R.drawable.baseline_flash_on_24);
            }
        }
        else {
            requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Flash is not available currently", Toast.LENGTH_SHORT).show());
        }
    }
}