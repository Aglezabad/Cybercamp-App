package com.slem.pwv.pwv.proxy.impl;

import android.content.Context;
import android.widget.Toast;

import com.slem.pwv.pwv.proxy.ProxySettings;

/**
 * Created by eslem on 12/5/2014.
 */
public class ProxySettingsImplHttpClient implements ProxySettings {
    Context context;
    String  host;
    int port;

    public ProxySettingsImplHttpClient(Context context, String host, int port) {
        this.context = context;
        this.host = host;
        this.port = port;
    }

    public ProxySettingsImplHttpClient() {
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void setUp() {
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyHost", host);
        System.getProperties().put("proxyPort", port);
        Toast.makeText(context, "Proxy " + host + ":" + port + " enabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void remove() {
        System.getProperties().put("proxySet", "false");
        System.getProperties().put("proxyHost", "");
        System.getProperties().put("proxyPort", "");
        Toast.makeText(context, "Proxy " + host + ":" + port + " disabled", Toast.LENGTH_LONG).show();    }
}
