package ourcoolgroup.facebookhack;

import android.content.Context;
import android.media.Image;
import android.support.v4.util.ArraySet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Dell on 11/03/2017.
 */

public class FeedCard {
    private Image coverImage;
    private String title;
    private ArraySet<Friend> friends;

    public FeedCard(){
        this(null, null, null);
    }

    public FeedCard(Image coverImage, String title, ArraySet<Friend> friends){
        this.coverImage = coverImage;
        this.title = title;
        this.friends = friends;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public View getView(Context context, ViewGroup container)
    {
        View feedCard = View.inflate(context, R.layout.feed_card_layout, container);
        TextView title_text = (TextView) feedCard.findViewById(R.id.title_text);
        title_text.setText(title);
        return feedCard;
    }
}
