package com.adewale.fiftygram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import jp.wasabeef.glide.transformations.BitmapTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestOptions;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public ImageView imageView, sepiaView, toonView, sketchView;
    public Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageview);
        sketchView = findViewById(R.id.sketch);
        toonView = findViewById(R.id.toon);
        sepiaView = findViewById(R.id.sepia);
    }

    public void choosePhoto(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK && data!=null){
            Uri uri = data.getData();
            try {
                ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri,"r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                imageView.setImageBitmap(image);
                //apply filters to images
                toonView.setImageBitmap(image);
                sketchView.setImageBitmap(image);
                sepiaView.setImageBitmap(image);

                applyFilter(new SepiaFilterTransformation(), sepiaView);
                applyFilter(new ToonFilterTransformation(), toonView);
                applyFilter(new SketchFilterTransformation(), sketchView);
            } catch (IOException e) {
                Log.e("Filter50", "Image not found", e);
            }
        }
    }

    public void applySepia(View v){
        applyFilter(new SepiaFilterTransformation(), imageView);
    }

    public void applyToon(View v){
        applyFilter(new ToonFilterTransformation(), imageView);
    }

    public void applySketch(View v){
        applyFilter(new SketchFilterTransformation(), imageView);
    }

    public void applyFilter(Transformation<Bitmap> transformation, ImageView view){
        Glide
                .with(this)
                .load(image)
                .apply(RequestOptions.bitmapTransform(transformation))
                .into(view);
    }
}