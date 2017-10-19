package com.cryprocodes.chucknorrisjokes;

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

/**
 * Created by Jonathan on 18/10/2017.
 */
public class JokesManager {
    private static JokesManager ourInstance;
    private String currentCategory;
    private OkHttpClient client = new OkHttpClient();
    private ObjectMapper mapper = new ObjectMapper();

    private List<IJokeUpdatedListener> listeners = new ArrayList<>();

    public void addListener(IJokeUpdatedListener listener) {
        listeners.add(listener);
    }

    public static JokesManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new JokesManager();
        }
        return ourInstance;
    }

    private JokesManager() {
        currentCategory = "all";
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

    public String getAllCategories() {
        try {
            // TODO: Fix this, it doesn't really get all categories at the moment
            run("/categories");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public void updateRandomJoke() {
        updateJokeByCategory(currentCategory);
    }

    public void updateJokeByCategory(String category) {
        try {
            if (category == "all") {
                run("/random");
            }
            else {
                run("/random?category=" + category);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String responseText;

    private void UpdateView(String jokeText) {
        if (listeners.size() > 0) {
            for(IJokeUpdatedListener listener : listeners) {
                listener.updateJoke(jokeText);
            }
        }
    }

    private Call run(String url) throws IOException {
        String apiEndpoint = "https://api.chucknorris.io/jokes";
        Request request = new Request.Builder()
                .url(apiEndpoint + url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    // do something wih the result
                    responseText = response.body().string();
                    Joke joke = getJokeFromJson(responseText);
                    UpdateView(joke.value);
                }
            }
        });

        return call;
    }
}
