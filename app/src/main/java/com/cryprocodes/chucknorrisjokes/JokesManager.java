package com.cryprocodes.chucknorrisjokes;

import android.app.Application;
import android.util.Log;

import com.cryprocodes.chucknorrisjokes.Helpers.SnackbarHelper;
import com.cryprocodes.chucknorrisjokes.Listeners.IJokeUpdatedListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JokesManager {
    private static final String TAG = "JOKES_MANAGER";
    private static JokesManager ourInstance;
    private Category currentCategory;
    private OkHttpClient client = new OkHttpClient();
    private ObjectMapper mapper = new ObjectMapper();
    private Joke currentJoke = null;
    private static final String apiEndpoint = "https://api.chucknorris.io/jokes";
    private List<IJokeUpdatedListener> listeners = new ArrayList<>();

    void addListener(IJokeUpdatedListener listener) {
        listeners.add(listener);
    }

    static JokesManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new JokesManager();
        }

        return ourInstance;
    }

    private JokesManager() {
        currentCategory = Category.All;
        updateRandomJoke();
    }

    private Joke getJokeFromJson(String json) {
        try {
            return mapper.readValue(json, Joke.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void updateRandomJoke() {
        updateJokeByCategory(currentCategory);
    }

    void updateJokeByCategory(Category category) {
        updateCategory(category);

        if (currentCategory == Category.All) {
            executeApiCall("/random");
        } else {
            executeApiCall("/random?category=" + currentCategory.toString().toLowerCase());
        }
    }

    private String responseText;

    private void UpdateView(Joke joke) {
        currentJoke = joke;

        if (listeners.size() > 0) {
            for (IJokeUpdatedListener listener : listeners) {
                listener.updateJoke(currentJoke);
            }
        }
    }

    private Call executeApiCall(String url) {

        Request request = new Request.Builder()
                .url(apiEndpoint + url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: Could not connect to the internet", e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    // do something wih the result
                    if (response.body() != null) {
                        responseText = response.body().string();
                    }

                    Joke joke = getJokeFromJson(responseText);

                    if (joke != null) {
                        UpdateView(joke);
                    }
                }
            }
        });

        return call;
    }

    private void updateCategory(Category newCategory) {
        currentCategory = newCategory;
    }

    Joke getCurrentJoke() {
        return currentJoke;
    }

    Category getCurrentCategory() {
        return currentCategory;
    }
}
