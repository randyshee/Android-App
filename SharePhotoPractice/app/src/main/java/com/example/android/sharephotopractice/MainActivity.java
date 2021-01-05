package com.example.android.sharephotopractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final int IMAGE_REQUEST = 1;
    private ImageView mPhoto;
    private Bitmap mPhotoTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhoto = findViewById(R.id.photo);
    }

    public void takePhoto(View view) {
        Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(intentTakePhoto, IMAGE_REQUEST);
        } catch (ActivityNotFoundException e) {
            Log.d("ImplicitIntents", "Can't handle this!");
            Toast.makeText(this, "No application can handle photo shooting", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST){
            if (resultCode == RESULT_OK) {
                Log.d(LOG_TAG, "Photo Taken Successfully");
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
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