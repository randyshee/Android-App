package com.example.android.sharephotopractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final int IMAGE_REQUEST = 1;
    private ImageView mPhoto;
    private String currentPhotoPath;
    private Bitmap mPhotoTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhoto = findViewById(R.id.photo);
    }

    public void takePhoto(View view) {
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());;

        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
            currentPhotoPath = imageFile.getAbsolutePath();

            Uri imageUri = FileProvider.getUriForFile(MainActivity.this,
                    "com.example.android.sharephotopractice.fileprovider", imageFile);

            Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intentTakePhoto, IMAGE_REQUEST);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST){
            if (resultCode == RESULT_OK) {
                Log.d(LOG_TAG, "Photo Taken Successfully");
                Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
                mPhoto.setImageBitmap(imageBitmap);
                mPhotoTaken = imageBitmap;
            }
        }
    }

    public void sharePhoto(View view) {
        if (mPhotoTaken != null){
            Intent intentSharePhoto = new Intent(Intent.ACTION_SEND);
            intentSharePhoto.setType("image/jpeg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            mPhotoTaken.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(
                    getContentResolver(), mPhotoTaken, "Photo", null);
            Uri photoUri =  Uri.parse(path);
            intentSharePhoto.putExtra(Intent.EXTRA_STREAM, photoUri);

            try {
                startActivity(Intent.createChooser(intentSharePhoto, "Share Image"));
            } catch (ActivityNotFoundException e) {
                Log.d("ImplicitIntents", "Can't handle this (share)!");
                Toast.makeText(this, "No application can handle photo sharing", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please take a photo first!", Toast.LENGTH_SHORT).show();
        }

    }
}