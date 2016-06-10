package com.prepod.unifeed;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Антон on 10.06.2016.
 */
public class Util {



    public static long getTimeStamp(String time){
        long timeStamp = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(time);
            timeStamp = convertedDate.getTime();
            Log.v("My", "" + convertedDate + time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return timeStamp;
    }
}
