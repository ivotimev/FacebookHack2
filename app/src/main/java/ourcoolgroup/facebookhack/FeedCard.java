package ourcoolgroup.facebookhack;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dell on 11/03/2017.
 */

public class FeedCard {
    private Image coverImage;
    private String title;
    private ArrayList<Friend> friends;
    private String[] genres;

    public FeedCard(){
        this(null, null, null, null);
    }

    public FeedCard(Image coverImage, String title, ArrayList<Friend> friends, String[] genres){
        this.coverImage = coverImage;
        this.title = title;
        this.friends = friends;
        this.genres = genres;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public View getSmallCardView(Context context, ViewGroup container)
    {
        View feedCard = LayoutInflater.from(context).inflate(R.layout.feed_card_layout, container, false);
        TextView title_text = (TextView) feedCard.findViewById(R.id.title_text);
        title_text.setText(title);
        TextView genresView = (TextView) feedCard.findViewById(R.id.genres_view);
        String joinedGenres = "";
        for(String genre : genres){
            joinedGenres += genre + ", ";
        }
        genresView.setText(joinedGenres);
        TextView friendsInterested = (TextView) feedCard.findViewById(R.id.friends_interested);
        friendsInterested.setText(friends.size() + " friends might be interested");
        GridView gridview = (GridView) feedCard.findViewById(R.id.grid_view);
        gridview.setAdapter(new FriendThumbnailAdapter(context, friends));
        return feedCard;
    }
}
