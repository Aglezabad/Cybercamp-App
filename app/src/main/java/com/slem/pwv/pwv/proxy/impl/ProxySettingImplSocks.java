package com.slem.pwv.pwv.proxy.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Proxy;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.widget.Toast;

import com.slem.pwv.pwv.proxy.ProxySettings;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * Created by eslem on 12/6/2014.
 */
public class ProxySettingImplSocks implements ProxySettings {

    Context context;
    String  host;
    int port;
    String authUser = "user";
    String authPassword = "password";

    public ProxySettingImplSocks(Context context, String host, int port, String authUser, String authPassword) {
        this.context = context;
        this.host = host;
        this.port = port;
        this.authUser = authUser;
        this.authPassword = authPassword;
    }

    public ProxySettingImplSocks() {
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

    public String getAuthUser() {
        return authUser;
    }

    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    @Override
    public void setUp() {
        Authenticator.setDefault(
                new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                authUser, authPassword.toCharArray());
                    }
                }
        );

        System.setProperty("java.net.socks.username.proxyUser", authUser);
        System.setProperty("java.net.socks.password", authPassword);

        System.getProperties().put("proxySet", "true");
        System.getProperties().put("socksProxyHost", host);
        System.getProperties().put("socksProxyPort", port);
        Toast.makeText(context, "Proxy " + host + ":" + port + " enabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setUpKitkat(Context context) {
        System.setProperty("java.net.socks.username.proxyUser", authUser);
        System.setProperty("java.net.socks.password", authPassword);

        System.getProperties().put("proxySet", "true");
        System.getProperties().put("socksProxyHost", host);
        System.getProperties().put("socksProxyPort", port);
        Toast.makeText(context, "Proxy " + host + ":" + port + " enabled", Toast.LENGTH_LONG).show();
        try {
            Class applictionCls = Class.forName("android.app.Application");
            Field loadedApkField = applictionCls.getDeclaredField("mLoadedApk");
            loadedApkField.setAccessible(true);
            Object loadedApk = loadedApkField.get(context);
            Class loadedApkCls = Class.forName("android.app.LoadedApk");
            Field receiversField = loadedApkCls.getDeclaredField("mReceivers");
            receiversField.setAccessible(true);
            ArrayMap receivers = (ArrayMap) receiversField.get(loadedApk);
            for (Object receiverMap : receivers.values()) {
                for (Object rec : ((ArrayMap) receiverMap).keySet()) {
                    Class clazz = rec.getClass();
                    if (clazz.getName().contains("ProxyChangeListener")) {
                        Method onReceiveMethod = clazz.getDeclaredMethod("onReceive", Context.class, Intent.class);
                        Intent intent = new Intent(Proxy.PROXY_CHANGE_ACTION);

                        /*********** optional, may be need in future *************/
                        final String CLASS_NAME = "android.net.ProxyProperties";
                        Class cls = Class.forName(CLASS_NAME);
                        Constructor constructor = cls.getConstructor(String.class, Integer.TYPE, String.class);
                        constructor.setAccessible(true);
                        Object proxyProperties = constructor.newInstance(host, port, null);
                        intent.putExtra("proxy", (Parcelable) proxyProperties);
                        /*********** optional, may be need in future *************/

                        onReceiveMethod.invoke(rec, context, intent);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove() {

    }
}
