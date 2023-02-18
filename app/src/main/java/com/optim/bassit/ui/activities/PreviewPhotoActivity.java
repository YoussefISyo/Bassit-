package com.optim.bassit.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.models.Photo;
import com.optim.bassit.ui.adapters.ImagePagerAdapter;
import com.optim.bassit.ui.fragments.ImageFragment;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreviewPhotoActivity extends BaseActivity {

    private DotsIndicator dotsIndicator;
    private ViewPager pager;
    private ImagePagerAdapter adapterpager;
    private String url = "";
    int cur = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_photo);
        ButterKnife.bind(this);

        pager = findViewById(R.id.pager);
        dotsIndicator = findViewById(R.id.dots_indicator);
        if (getIntent().hasExtra("URL"))
            url = getIntent().getExtras().getString("URL");
        if (getIntent().hasExtra("photos")) {
            photos.addAll(getIntent().getExtras().getParcelableArrayList("photos"));
            cur = getIntent().getExtras().getInt("curr");
        }


        loadPhotos();
    }

    private void saveImage(Bitmap finalBitmap) {


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Alert(R.string.veuillez_autoriser_storage);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        7741);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String image_name = simpleDateFormat.format(new Date());

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Bassit";
        File myDir = new File(root);
        if (!myDir.exists()) {
            boolean bb = myDir.mkdirs();

        }

        String fname = "Image-" + image_name + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        //Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            runOnUiThread(() -> {
                Toast.makeText(this, "Photo sauvegardée !", Toast.LENGTH_LONG).show();
            });


        } catch (Exception e) {
            runOnUiThread(() -> {
                Toast.makeText(this, "Un erreur est survenue !", Toast.LENGTH_LONG).show();
            });
            e.printStackTrace();
        }
    }

    Bitmap bmp;

    @OnClick(R.id.b_save)
    public void Save() {

        if (bsaving) {
            Toast.makeText(PreviewPhotoActivity.this, R.string.not_saved_yet, Toast.LENGTH_LONG).show();
            return;
        }
        ImageFragment fm = (ImageFragment) adapterpager.getItem(pager.getCurrentItem());
        String url = fm.getPhoto().getFullImage();
        bsaving = true;
        Thread thread = new Thread(() -> {
            bmp = getBitmapFromURL(url);

            saveImage(bmp);

            bsaving = false;
        });

        thread.start();
    }

    boolean bsaving = false;

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    List<Photo> photos = new ArrayList<>();

    private void loadPhotos() {
        if (!url.matches(""))
            photos.add(new Photo(url));

        adapterpager = new ImagePagerAdapter(getSupportFragmentManager());
        adapterpager.fillData(photos);
        pager.setAdapter(adapterpager);
        dotsIndicator.setViewPager(pager);

        Handler handler = new Handler();
        Runnable runnable = () -> {
            for (int i = 0; i < adapterpager.getCount() - 1; i++) {
                final int value = i;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(() -> {
                    pager.setCurrentItem(cur, true);
                });
            }
        };
        new Thread(runnable).start();
    }

    @OnClick(R.id.b_back)
    public void doBack() {
        finish();
    }
}
