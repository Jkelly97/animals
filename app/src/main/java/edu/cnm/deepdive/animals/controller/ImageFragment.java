package edu.cnm.deepdive.animals.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.collection.CircularArray;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.animals.BuildConfig;
import edu.cnm.deepdive.animals.R;
import edu.cnm.deepdive.animals.model.Animals;
import edu.cnm.deepdive.animals.model.ApiKey;
import edu.cnm.deepdive.animals.service.AnimalService;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageFragment extends Fragment {

  private WebView contentView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_image, container, false);
    setupWebView(root);
    return root;
  }

  private void setupWebView(View root) {
    contentView = root.findViewById(R.id.content_view);
    contentView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
      }
    });
    WebSettings settings = contentView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setSupportZoom(true);
    settings.setBuiltInZoomControls(true);
    settings.setDisplayZoomControls(false);
    settings.setUseWideViewPort(true);
    settings.setLoadWithOverviewMode(true);
    new RetrieverTask().execute();
  }


  private class RetrieverTask extends AsyncTask<Void, Void, List<Animals>> {

    private AnimalService animalService;

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      Gson gson = new GsonBuilder()
          .create();
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(BuildConfig.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build();
      animalService = retrofit.create(AnimalService.class);
    }

    @Override
    protected List<Animals> doInBackground(Void... voids) {
      try {
        Response<ApiKey> keyResponse = animalService.getApiKey().execute();
        ApiKey key = keyResponse.body();
        assert key != null;
        final String clientKey = key.getKey();

        Response<List<Animals>> listResponse = animalService.getAnimals(clientKey).execute();
        List<Animals> animalList = listResponse.body();
        assert animalList != null;
        return animalList;

      } catch (IOException e) {
        Log.e("AnimalService", e.getMessage(), e);
        cancel(true);
      }
      return null;
    }

    @Override
    protected void onPostExecute(List<Animals> animalsList) {
      Random rng = new Random();
      final String imageUrl = animalsList.get(rng.nextInt(animalsList.size())).getImageUrl();
      contentView.loadUrl(imageUrl);
    }
  }

}
