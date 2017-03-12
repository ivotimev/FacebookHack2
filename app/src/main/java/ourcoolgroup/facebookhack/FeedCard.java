package ourcoolgroup.facebookhack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.tv.TvContract;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dell on 11/03/2017.
 */

public class FeedCard implements Serializable {
    private static final long serialVersionUID = 1L;
    private String coverImageURL;
    private String title;
    private double imdbRating;
    private String[] genres;
    private ArrayList<Friend> friendsInterested;
    private ArrayList<Friend> friendsWantToWatch;

    public FeedCard(){
        this(null, null, 0, null, null, null);
    }

    public FeedCard(String coverImageURL, String title, double imdbRating, String[] genres, ArrayList<Friend> friendsInterested, ArrayList<Friend> friendsWantToWatch)
    {
        this.coverImageURL = coverImageURL;
        this.title = title;
        this.imdbRating = imdbRating;
        this.genres = genres;
        this.friendsInterested = friendsInterested;
        this.friendsWantToWatch = friendsWantToWatch;
    }

    public void setCoverImage(String coverImageURL) {
        this.coverImageURL = coverImageURL;
    }

    public void setFriendsInterested(ArrayList<Friend> friendsInterested) {
        this.friendsInterested = friendsInterested;
    }

    public void setFriendsWantToWatch(ArrayList<Friend> friendsWantToWatch) {
        this.friendsWantToWatch = friendsWantToWatch;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public ArrayList<Friend> getFriendsInterested() {
        return friendsInterested;
    }

    public ArrayList<Friend> getFriendsWantToWatch() {
        return friendsWantToWatch;
    }

    public String getCoverImage() {
        return coverImageURL;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public String getGenreString() {
        String genresStr = "";
        int i;
        for(i = 0; i < genres.length - 1; i++)
        {
            genresStr += genres[i] + ", ";
        }
        genresStr += genres[i];
        return genresStr;
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public static void downloadImage(ImageView imageView, String URL)
    {
        try {
            new DownloadImageTask(imageView)
                    .execute(URL);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void downloadImage(ImageView imageView)
    {
        try {
            new DownloadImageTask(imageView)
                    .execute(coverImageURL);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public View getSmallCardView(final Context context, ViewGroup container)
    {
        View feedCard = LayoutInflater.from(context).inflate(R.layout.feed_card_layout, container, false);
        feedCard.setClickable(true);
        feedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationActivity.openMovieViewActivity(FeedCard.this, view.getContext());
            }
        });
        try {
            downloadImage((ImageView) feedCard.findViewById(R.id.poster_view));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        TextView title_text = (TextView) feedCard.findViewById(R.id.title_text);
        title_text.setText(title);
        TextView genresView = (TextView) feedCard.findViewById(R.id.genres_view);
        genresView.setText(getGenreString());
        TextView friendsInterestedTV = (TextView) feedCard.findViewById(R.id.friends_interested);
        int friendsInter = 0;
        if(friendsWantToWatch != null)
            friendsInter += friendsWantToWatch.size();
        if(friendsInterested != null)
            friendsInter += friendsInterested.size();
        friendsInterestedTV.setText(friendsInter + " friends might be interested");
        GridView gridview = (GridView) feedCard.findViewById(R.id.grid_view);
        gridview.setAdapter(new FriendThumbnailAdapter(context, this, 100));
        return feedCard;
    }
}
