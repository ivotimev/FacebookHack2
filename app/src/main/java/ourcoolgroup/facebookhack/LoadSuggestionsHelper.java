package ourcoolgroup.facebookhack;

import java.util.ArrayList;

/**
 * Created by Dell on 12/03/2017.
 */

public class LoadSuggestionsHelper {
    public static ArrayList<FeedCard> getRecommendations(){
        ArrayList<FeedCard> feedCards = new ArrayList<>();

        FeedCardWithTag insurgentCard = new FeedCardWithTag();
        insurgentCard.setTitle("Insurgent 2015");
        insurgentCard.setTag(FeedCardWithTag.YOU_MIGHT_ENJOY);

        ArrayList<Friend> friendsInterested = new ArrayList<>();
        friendsInterested.add(new Friend());
        friendsInterested.add(new Friend());
        friendsInterested.add(new Friend());
        friendsInterested.add(new Friend());
        friendsInterested.add(new Friend());

        ArrayList<Friend> friendsWhoWantToSee = new ArrayList<>();
        friendsWhoWantToSee.add(new Friend());
        friendsWhoWantToSee.add(new Friend());
        friendsWhoWantToSee.add(new Friend());

        insurgentCard.setFriendsInterested(friendsInterested);
        insurgentCard.setImdbRating(6.3/2);
        insurgentCard.setCoverImage("https://images-na.ssl-images-amazon.com/images/M/MV5BMTgxOTYxMTg3OF5BMl5BanBnXkFtZTgwMDgyMzA2NDE@._V1_SX300.jpg");

        insurgentCard.setGenres(new String[]{"Action", "Thriller", "Distopian"});

        feedCards.add(insurgentCard);

        return feedCards;
    }
}
