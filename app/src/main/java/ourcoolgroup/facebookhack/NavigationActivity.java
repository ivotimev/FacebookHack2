package ourcoolgroup.facebookhack;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FeedFragment feedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        feedFragment = new FeedFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_viewer, feedFragment).commit();

        /*might be unnecessary*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        /*might be unnecessary*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_feed);
        setTitle("Picks");
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPicks();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify val1 parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPicks()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_viewer, feedFragment).commit();
        FeedCardWithTag transformersCard = new FeedCardWithTag();

        ArrayList<Friend> friends = new ArrayList<Friend>();
        friends.add(new Friend());
        friends.add(new Friend());
        transformersCard.setFriends(friends);
        transformersCard.setTitle("Transformers");
        transformersCard.setGenres(new String[]{"Action", "Sci-fi", "Romance"});
        transformersCard.setTag(FeedCardWithTag.LOTS_OF_FRIENDS_INTERESTED);

        feedFragment.addCard(transformersCard);

        FeedCardWithTag arrivalCard = new FeedCardWithTag();
        ArrayList<Friend> friends2 = new ArrayList<Friend>();
        friends2.add(new Friend());
        friends2.add(new Friend());
        friends2.add(new Friend());
        arrivalCard.setFriends(friends2);
        arrivalCard.setTitle("Arrival");
        arrivalCard.setGenres(new String[]{"Sci-fi", "Thriller", "Drama"});
        arrivalCard.setTag(FeedCardWithTag.YOU_MIGHT_ENJOY);

        FeedCard fastAndFuriousCard = new FeedCard();
        fastAndFuriousCard.setTitle("Fast and Furious");
        fastAndFuriousCard.setFriends(friends2);
        fastAndFuriousCard.setGenres(new String[]{"Sci-fi", "Thriller", "Drama"});

        feedFragment.addCard(fastAndFuriousCard);
        feedFragment.addCard(arrivalCard);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //get fragment to load
        FragmentManager fragmentManager = getSupportFragmentManager();

        int id = item.getItemId();

        if (id == R.id.nav_feed) {
            loadPicks();
        } else if (id == R.id.nav_movies) {

        } else if (id == R.id.nav_games) {

        } else if (id == R.id.nav_music) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
