package com.tahir.moviebuff.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

import static com.tahir.moviebuff.util.Constants.KEY_MAX_YEAR;
import static com.tahir.moviebuff.util.Constants.KEY_MIN_YEAR;


/**
 * Created by Tahir Khan on 5/11/15.
 * <p>
 * Class to put common methods used in app
 */
public class CommonUtil {

    public static void saveMinMaxYear(Context context, Integer minYear, Integer maxYear) {
        if (context == null)
            return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MIN_YEAR, String.valueOf(minYear));
        editor.putString(KEY_MAX_YEAR, String.valueOf(maxYear));
        editor.apply();
    }

    public static String[] getMinMaxYear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);

        return new String[]{sharedPreferences.getString(KEY_MIN_YEAR, Calendar.getInstance().get(Calendar.YEAR) + ""), sharedPreferences.getString(KEY_MAX_YEAR, Calendar.getInstance().get(Calendar.YEAR) + "")};
    }


}