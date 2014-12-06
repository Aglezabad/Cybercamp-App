package com.slem.pwv.pwv.petition.impl;

import android.content.Context;

import com.alzatezabala.network.NotifyRequest;
import com.alzatezabala.network.Request;
import com.google.gson.Gson;
import com.slem.pwv.pwv.petition.PetitionInterface;

import java.util.HashMap;
import java.util.List;

/**
 * Created by nena on 06/12/14.
 */
public class LoginPetition implements PetitionInterface {

    private Context parentContext;
    private String user = "";
    private String psw = "";

    public LoginPetition(Context context, String user, String psw)
    {
        this.parentContext = context;
        this.user = user;
        this.psw = psw;
    }

    public void SetRequestData(String user, String psw)
    {
        this.user = user;
        this.psw = psw;
    }

    @Override
    public void SendRequest()
    {
        // TODO
        HashMap<String, String> params  = new HashMap<String, String>();
        List<String> list;
        String contactsJson = new Gson().toJson("");
        params.put("contacts", contactsJson);
        Request request = new Request("URL",params);
        request.setNotifyRequest(this);
        request.post();
    }

    @Override
    public void postFinished(String s) {
        if(s == "ERROR")
        {
            // ERROR LOGIN
        }
        else if( s == "OK")
        {
            // LOGIN OK
        }
    }
}
