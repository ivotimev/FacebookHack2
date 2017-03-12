package ourcoolgroup.facebookhack;

import android.content.Context;
import android.support.v4.util.ArraySet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Dell on 11/03/2017.
 */

public class FriendThumbnailAdapter extends BaseAdapter {

    private Context mContext;
    private FeedCard feedCard;
    private int squareSize;

    public FriendThumbnailAdapter(Context mContext, FeedCard feedCard, int squareSize){
        this.mContext = mContext;
        this.feedCard = feedCard;
        this.squareSize = squareSize;
    }

    @Override
    public int getCount() {
        return (feedCard.getFriendsInterested() != null ? feedCard.getFriendsInterested().size() : 0) + (feedCard.getFriendsWantToWatch() != null ? feedCard.getFriendsWantToWatch().size() : 0);
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if(view == null)
        {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(squareSize, squareSize));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else {
            imageView = (ImageView) view;
        }

        Friend friend = ((i < (feedCard.getFriendsWantToWatch() != null ? feedCard.getFriendsWantToWatch().size() : 0) ? feedCard.getFriendsWantToWatch().get(i) : feedCard.getFriendsInterested().get(i - (feedCard.getFriendsWantToWatch() != null ? feedCard.getFriendsWantToWatch().size() : 0))));
        FeedCard.downloadImage(imageView, friend.getProfilePicURL());

        return imageView;
    }
}
