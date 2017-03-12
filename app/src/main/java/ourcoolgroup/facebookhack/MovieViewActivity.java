package ourcoolgroup.facebookhack;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MovieViewActivity extends AppCompatActivity {

    private FeedCard feedCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        feedCard = (FeedCard) intent.getSerializableExtra(NavigationActivity.VIEWED_MOVIE_CARD);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageView moviePosterView = (ImageView) findViewById(R.id.movie_view_poster);
        feedCard.downloadImage(moviePosterView);
        TextView movieTitleView = (TextView) findViewById(R.id.movie_title_view);
        movieTitleView.setText(feedCard.getTitle());
        GridView friendsView0 = (GridView) findViewById(R.id.gridView13);
        friendsView0.setAdapter(new FriendThumbnailAdapter1(this, feedCard.getFriendsWantToWatch(), 200));
        GridView friendsView1 = (GridView) findViewById(R.id.gridView12);
        friendsView1.setAdapter(new FriendThumbnailAdapter1(this, feedCard.getFriendsInterested(), 200));
        TextView movieViewGenrer = (TextView) findViewById(R.id.movie_view_genres);
        movieViewGenrer.setText(feedCard.getGenreString());
        RatingBar imdbRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        imdbRatingBar.setRating((float)feedCard.getImdbRating());
    }
}
