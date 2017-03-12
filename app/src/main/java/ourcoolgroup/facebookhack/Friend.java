package ourcoolgroup.facebookhack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.Serializable;

/**
 * Created by Dell on 11/03/2017.
 */

public class Friend implements Serializable{
    private static final long serialVersionUID = 1L;
    private String profilePicURL;//could be url or smt
    private String name;

    public Friend(){
        profilePicURL = "http://eadb.org/wp-content/uploads/2015/08/profile-placeholder.jpg";
        name = null;
    }

    public Friend(String URL)
    {
        this.profilePicURL = URL;
        name = null;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }
}
