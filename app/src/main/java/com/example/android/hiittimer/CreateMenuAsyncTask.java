package com.example.android.hiittimer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.MenuItem;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
public class CreateMenuAsyncTask extends AsyncTask<Uri, Void, Bitmap> {
    private final WeakReference<MenuItem> itemWeakReference;
    private final WeakReference<Context> contextWeakReference;

    public CreateMenuAsyncTask(Context context, MenuItem menuItem) {
        this.contextWeakReference = new WeakReference<>(context);
        this.itemWeakReference = new WeakReference<>(menuItem);
    }

    @Override
    protected Bitmap doInBackground(Uri... uris) {
        Bitmap result = null;
        HttpsURLConnection connection = null;

        try {
            URL url = new URL(uris[0].toString());
            connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setInstanceFollowRedirects(false);

            connection.setReadTimeout(10000);

            connection.setConnectTimeout(10000);

            connection.connect();

            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                result = BitmapFactory.decodeStream(connection.getInputStream());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        if (result == null) {
            return null;
        }
        return result;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (this.itemWeakReference != null && this.contextWeakReference != null) {
            MenuItem menuItem = itemWeakReference.get();
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                    contextWeakReference.get().getResources(),bitmap);
            roundedBitmapDrawable.setCornerRadius(100);
            menuItem.setIcon(roundedBitmapDrawable);
        }
    }
}
