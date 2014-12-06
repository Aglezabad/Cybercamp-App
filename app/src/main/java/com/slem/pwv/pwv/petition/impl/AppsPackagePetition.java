package com.slem.pwv.pwv.petition.impl;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.alzatezabala.network.NotifyRequest;
import com.alzatezabala.network.Request;
import com.google.gson.Gson;
import com.slem.pwv.pwv.petition.PetitionInterface;

import java.util.HashMap;
import java.util.List;

/**
 * Created by nena on 06/12/14.
 */
public class AppsPackagePetition implements PetitionInterface {

    private Context parentContext;
    private List<String> listApps;

    public AppsPackagePetition(Context context)
    {
        this.parentContext = context;
    }

    @Override
    public void SendRequest()
    {
        listApps = this.GetListApps();
        HashMap<String, String> params  = new HashMap<String, String>();
        List<String> list;
        String contactsJson = new Gson().toJson(this.listApps);
        params.put("apps", contactsJson);
        Request request = new Request("URL",params);
        request.setNotifyRequest(this);
        request.post();
    }


    public List<String> GetListApps()
    {
        final PackageManager pm = parentContext.getPackageManager();

        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        String TAG = "APPList";
        for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
            Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
            this.listApps.add(packageInfo.packageName);
        }

        return this.listApps;
    }

    @Override
    public void postFinished(String s) {
        boolean error = false;
        if(s == "ERROR")
        {
            error = true;
        }
        else
        {
            int numberOfContacts = Integer.parseInt(s);
            if(numberOfContacts != this.listApps.size())
            {
                error = true;
            }
        }

        if(error)
        {
            // TODO: do something...
        }
    }
}
