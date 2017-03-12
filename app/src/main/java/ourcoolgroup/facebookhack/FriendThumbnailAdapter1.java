package ourcoolgroup.facebookhack;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Dell on 11/03/2017.
 */

public class FriendThumbnailAdapter1 extends BaseAdapter {

    private Context mContext;
    private ArrayList<Friend> friends;
    private int squareSize;

    public FriendThumbnailAdapter1(Context mContext, ArrayList<Friend> friends, int squareSize){
        this.mContext = mContext;
        this.friends = friends;
        this.squareSize = squareSize;
    }

    @Override
    public int getCount() {
        return friends != null ? friends.size() : 0;
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

        imageView.setImageBitmap(friends.get(i).getProfilePic(mContext));
        return imageView;
    }
}
