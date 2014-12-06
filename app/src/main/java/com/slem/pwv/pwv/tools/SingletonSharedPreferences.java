package com.slem.pwv.pwv.tools;

import android.content.Context;

import com.alzatezabala.libreria.LibSharedPreferences;

/**
 * Created by eslem on 12/6/2014.
 */
public class SingletonSharedPreferences {
    public static String name="pwv1";

    public static LibSharedPreferences libSharedPreferences=null;

    public static LibSharedPreferences getInstance(Context context){
        if(libSharedPreferences == null){
            libSharedPreferences = new LibSharedPreferences(context,name);
        }

        return libSharedPreferences;
    }
}
