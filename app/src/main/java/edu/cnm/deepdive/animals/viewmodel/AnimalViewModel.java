package edu.cnm.deepdive.animals.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.animals.BuildConfig;
import edu.cnm.deepdive.animals.model.Animals;
import edu.cnm.deepdive.animals.model.ApiKey;
import edu.cnm.deepdive.animals.service.AnimalService;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnimalViewModel extends AndroidViewModel {

  private MutableLiveData<List<Animals>> animals;

  public AnimalViewModel(
      @NonNull Application application) {
    super(application);
    animals = new MutableLiveData<>();
    loadAnimals();
  }

  public LiveData<List<Animals>> getAnimals() {
    return animals;
  }

  @SuppressLint("StaticFieldLeak")
  private void loadAnimals() {

    new AsyncTask<Void, Void, List<Animals>>() {

      AnimalService animalService;

      @Override
      protected void onPreExecute() {
        super.onPreExecute();
        Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
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

          Response<ApiKey> apiKeyResponse = animalService.getApiKey().execute();
          ApiKey apiKey = apiKeyResponse.body();
          final String key = apiKey.getKey();

          Response<List<Animals>> animalResponse = animalService.getAnimals(key).execute();
          List<Animals> animals = animalResponse.body();
          AnimalViewModel.this.animals.postValue(animals);
          return animals;

        } catch (IOException e) {
          Log.e("AnimalService", e.getMessage(), e);
          cancel(true);
        }

        return null;
      }
    }.execute();
  }
}

