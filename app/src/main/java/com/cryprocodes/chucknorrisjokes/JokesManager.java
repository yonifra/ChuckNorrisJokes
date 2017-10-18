package com.cryprocodes.chucknorrisjokes;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Jonathan on 18/10/2017.
 */
public class JokesManager {
    private static final JokesManager ourInstance = new JokesManager();
    public String currentCategory;
    OkHttpClient client = new OkHttpClient();
    private String apiEndpoint = "https://api.chucknorris.io/jokes";
 //   public static TextView jokesTextView;
    public static Activity jokesActivity;

    public static JokesManager getInstance() {
        return ourInstance;
    }

    private JokesManager() {
        currentCategory = "all";
        updateRandomJoke();
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

    private static String responseText;

    private static void UpdateView() {
        if (jokesActivity != null) {
            final TextView jokesTextView = jokesActivity.findViewById(R.id.jokeTextView);
            jokesActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    jokesTextView.setText(responseText);
                }
            });
        }
    }

    Call run(String url) throws IOException {
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
                    UpdateView();
                }
            }
        });

        return call;
    }
}
