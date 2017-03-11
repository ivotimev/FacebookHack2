package ourcoolgroup.facebookhack;

import android.media.Image;

/**
 * Created by Dell on 11/03/2017.
 */

public class Friend {
    private Image profilePic;
    private String name;

    public Friend(){
        profilePic = null;
        name = null;
    }

    public Image getProfilePic() {
        return profilePic;
    }
}
