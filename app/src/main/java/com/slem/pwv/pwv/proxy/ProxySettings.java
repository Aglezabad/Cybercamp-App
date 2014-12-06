package com.slem.pwv.pwv.proxy;

import android.content.Context;

/**
 * Created by eslem on 12/5/2014.
 */
public interface ProxySettings {
    public void setUp();
    public void setUpKitkat(Context context);
    public void remove();
}
