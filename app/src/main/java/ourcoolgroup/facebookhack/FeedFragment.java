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
        return feedDisplay;
    }

    public void addCard(FeedCard card)
    {
        LinearLayout cardDisplay = (LinearLayout) getView().findViewById(R.id.feed_card_display);
        View cardView = card.getSmallCardView(getActivity(), cardDisplay);
        cardDisplay.addView(cardView);
    }
}
