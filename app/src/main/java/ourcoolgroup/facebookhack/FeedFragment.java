package ourcoolgroup.facebookhack;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Dell on 11/03/2017.
 */

public class FeedFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View feedDisplay = inflater.inflate(R.layout.feed_fragment_layout, container, false);
        FeedCard transformersCard = new FeedCard();

        ArrayList<Friend> friends = new ArrayList<Friend>();
        friends.add(new Friend());
        friends.add(new Friend());
        transformersCard.setFriends(friends);
        transformersCard.setTitle("Transformers");
        transformersCard.setGenres(new String[]{"Action", "Sci-fi", "Romance"});

        FeedCard arrivalCard = new FeedCard();
        ArrayList<Friend> friends2 = new ArrayList<Friend>();
        friends2.add(new Friend());
        friends2.add(new Friend());
        friends2.add(new Friend());
        arrivalCard.setFriends(friends2);
        arrivalCard.setTitle("Arrival");
        arrivalCard.setGenres(new String[]{"Sci-fi", "Thriller", "Drama"});

        addCard(feedDisplay, transformersCard);
        addCard(feedDisplay, arrivalCard);
        return feedDisplay;
    }

    public void addCard(View feedView, FeedCard card)
    {
        LinearLayout cardDisplay = (LinearLayout) feedView.findViewById(R.id.feed_card_display);
        View cardView = card.getSmallCardView(getActivity(), cardDisplay);
        cardDisplay.addView(cardView);
    }
}
