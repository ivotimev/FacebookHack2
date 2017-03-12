package ourcoolgroup.facebookhack;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Dell on 11/03/2017.
 */

public class FeedCardWithTag extends FeedCard {
    public static final Triple<String, Integer, Integer> FRIEND_WANTS_TO_SEE = new Triple<>(" might want to see ...", Color.BLUE, Color.DKGRAY);
    public static final Triple<String, Integer, Integer> YOU_MIGHT_ENJOY = new Triple<>(" You might enjoy ...", Color.RED, Color.DKGRAY);
    public static final Triple<String, Integer, Integer> LOTS_OF_FRIENDS_INTERESTED = new Triple<>("A bunch of people are interested in ...", Color.CYAN, Color.DKGRAY);
    public static final Triple<String, Integer, Integer> RECENTLY_VIEWED = new Triple<>("Do you want to go see ... ?", Color.GREEN, Color.DKGRAY);

    private Triple<String, Integer, Integer> tag;

    public void setTag(Triple<String, Integer, Integer> tag) {
        this.tag = tag;
    }

    private void setTag(TextView textView)
    {
        textView.setText(tag.val1);
        textView.setVisibility(View.VISIBLE);
        textView.setBackgroundColor(tag.val3);
        textView.setTextColor(tag.val2);
    }

    @Override
    public View getSmallCardView(Context context, ViewGroup container) {
        View feedCard = super.getSmallCardView(context, container);
        setTag((TextView)feedCard.findViewById(R.id.flag_message));
        return feedCard;
    }

    static class Triple<E, F, T> implements Serializable{
        private static final long serialVersionUID = 1L;
        E val1;
        F val2;
        T val3;
        public Triple(E val1, F val2, T val3)
        {
            this.val1 = val1;
            this.val2 = val2;
            this.val3 = val3;
        }
    }
}
