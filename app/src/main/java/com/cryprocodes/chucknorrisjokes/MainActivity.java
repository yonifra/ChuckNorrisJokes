package com.cryprocodes.chucknorrisjokes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cryprocodes.chucknorrisjokes.Listeners.IJokeUpdatedListener;

import me.grantland.widget.AutofitTextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IJokeUpdatedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Refreshing joke...", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                // TODO: Change this to take into consideration the currently selected category
                JokesManager.getInstance().updateRandomJoke();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        JokesManager.getInstance().addListener(this);
        JokesManager.getInstance().updateRandomJoke();

        FloatingActionButton shareFab = findViewById(R.id.shareFab);
        shareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareJoke();
            }
        });
    }

    private void ShareJoke() {
        Joke joke = JokesManager.getInstance().getCurrentJoke();

        if (joke != null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Here's a fun Chuck Norris Joke:\n" + joke.value);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Category newCategory = JokesManager.getInstance().getCurrentCategory();

        if (id == R.id.nav_all) {
            newCategory = Category.All;
        } else if (id == R.id.nav_explicit) {
            newCategory = Category.Explicit;
        } else if (id == R.id.nav_dev) {
            newCategory = Category.Dev;
        } else if (id == R.id.nav_movies) {
            newCategory = Category.Movie;
        } else if (id == R.id.nav_food) {
            newCategory = Category.Food;
        } else if (id == R.id.nav_celebrity) {
            newCategory = Category.Celebrity;
        } else if (id == R.id.nav_science) {
            newCategory = Category.Science;
        } else if (id == R.id.nav_politics) {
            newCategory = Category.Political;
        } else if (id == R.id.nav_sports) {
            newCategory = Category.Sport;
        } else if (id == R.id.nav_religion) {
            newCategory = Category.Religion;
        } else if (id == R.id.nav_animals) {
            newCategory = Category.Animal;
        } else if (id == R.id.nav_music) {
            newCategory = Category.Music;
        } else if (id == R.id.nav_history) {
            newCategory = Category.History;
        } else if (id == R.id.nav_travel) {
            newCategory = Category.Travel;
        } else if (id == R.id.nav_career) {
            newCategory = Category.Career;
        } else if (id == R.id.nav_money) {
            newCategory = Category.Money;
        } else if (id == R.id.nav_fashion) {
            newCategory = Category.Fashion;
        } else if (id == R.id.nav_share) {
            ShareJoke();
        } else if (id == R.id.nav_settings) {

        }

        if (newCategory != JokesManager.getInstance().getCurrentCategory()) {
            JokesManager.getInstance().updateJokeByCategory(newCategory);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updateJoke(Joke joke) {
        final AutofitTextView jokeTextView = findViewById(R.id.jokeTextView);
        final TextView categoryTextView = findViewById(R.id.categoryTextView);
        final String jokeText = joke.value;

        final String jokeCategory = (joke.category == null) ? "Everything" : joke.category.get(0);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                jokeTextView.setText(jokeText);
                categoryTextView.setText("[" + jokeCategory + "]");
            }
        });
    }
}
