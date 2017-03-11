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
    private ArrayList<Friend> friends;

    public FriendThumbnailAdapter(Context mContext, ArrayList<Friend> friends){
        this.mContext = mContext;
        this.friends = friends;
    }

    @Override
    public int getCount() {
        return friends.size();
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
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else {
            imageView = (ImageView) view;
        }

        imageView.setImageBitmap(friends.get(i).getProfilePic(mContext));
        return imageView;
    }
}
