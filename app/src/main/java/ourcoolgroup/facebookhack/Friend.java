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
    private transient Bitmap profilePic;//could be url or smt
    private String name;

    public Friend(){
        profilePic = null;
        name = null;
    }

    public Bitmap getProfilePic(Context context) {
        if(profilePic == null)
        {
            profilePic = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile_pic_placeholder);
        }
        return profilePic;
    }

}
