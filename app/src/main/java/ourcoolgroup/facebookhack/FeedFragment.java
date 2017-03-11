package ourcoolgroup.facebookhack;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Dell on 11/03/2017.
 */

public class FeedFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View feedDisplay = inflater.inflate(R.layout.feed_fragment_layout, container, false);
        FeedCard transformersCard = new FeedCard();
        transformersCard.setTitle("Transformers");
        addCard(feedDisplay, transformersCard);
        return feedDisplay;
    }

    public void addCard(View feedView, FeedCard card)
    {
        LinearLayout cardDisplay = (LinearLayout) feedView.findViewById(R.id.feed_card_display);
        View cardView = card.getView(getActivity(), cardDisplay);
    }
}
