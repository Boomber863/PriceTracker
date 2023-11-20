package com.example.pricetracker.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.example.pricetracker.api.ServerUrls;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Class that handles image downloads
 */
public class BitmapExtractor {

    /**
     * @param activity  activity that is currently displayed
     * @param imageView component on which we want to set image
     * @param url       url to image that will be downloaded
     */
    public static void extractBitmapAndSetImage(Activity activity, ImageView imageView, String url) {

        // TODO: DELETE IFS BELOW WHEN DB WORKS ARE DONE

        // IF URL IS NULL THEN FALLBACK TO DEFAULT URL
        if (url == null) {
            url = "http://10.0.2.2:8000/static/product_img/example.jpg";
        }
        // THIS ENSURES THAT URL IS ACCESSIBLE FROM ANDROID EMULATOR
        else if (url.startsWith("http://127.0.0.1")) {
            url = url.replace("http://127.0.0.1:8000", ServerUrls.SERVER_URL);
        }

        final String finalUrl = url;
        new Thread(() -> {
            try {
                InputStream inputStream = new URL(finalUrl).openStream();
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                activity.runOnUiThread(() -> imageView.setImageBitmap(bmp));
            } catch (IOException e) {
                Log.e("DEBUG", e.getMessage());
            }
        }).start();
    }
}
