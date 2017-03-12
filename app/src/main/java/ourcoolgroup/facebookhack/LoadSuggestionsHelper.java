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

        FeedCardWithTag theHostCard = new FeedCardWithTag();
        theHostCard.setTitle("The Host 2013");
        theHostCard.setTag(FeedCardWithTag.FRIEND_WANTS_TO_SEE);
        theHostCard.setFriendsWantToWatch(friendsWhoWantToSee);
        theHostCard.setGenres(new String[]{"Action", "Thriller", "Distopian"});
        theHostCard.setCoverImage("http://www.impawards.com/2013/posters/host_ver2.jpg");
        theHostCard.setImdbRating(5.9/2);

        feedCards.add(theHostCard);

        FeedCardWithTag abductionCard = new FeedCardWithTag();
        abductionCard.setTitle("Abduction 2011");
        abductionCard.setTag(FeedCardWithTag.LOTS_OF_FRIENDS_INTERESTED);
        abductionCard.setFriendsWantToWatch(friendsWhoWantToSee);
        abductionCard.setFriendsInterested(friendsInterested);
        abductionCard.setGenres(new String[]{"Action", "Thriller", "Distopian", "Drama"});
        abductionCard.setCoverImage("http://www.hollywoodreporter.com/sites/default/files/2011/06/abduction_poster_embedding.jpg");
        abductionCard.setImdbRating(2.5);

        feedCards.add(abductionCard);

        FeedCardWithTag theHungerGamesCard = new FeedCardWithTag();
        theHungerGamesCard.setTitle("The Hunger Games 2012");
        theHungerGamesCard.setTag(FeedCardWithTag.RECENTLY_VIEWED);
        friendsInterested.add(new Friend());
        theHungerGamesCard.setFriendsInterested(friendsInterested);
        theHungerGamesCard.setGenres(new String[]{"Sci-fi", "Action", "Thriller", "Distopian", "Drama"});
        theHungerGamesCard.setCoverImage("https://images-na.ssl-images-amazon.com/images/M/MV5BMjA4NDg3NzYxMF5BMl5BanBnXkFtZTcwNTgyNzkyNw@@._V1_SX300.jpg");
        theHungerGamesCard.setImdbRating(3.6);

        feedCards.add(theHungerGamesCard);

        return feedCards;
    }
}
