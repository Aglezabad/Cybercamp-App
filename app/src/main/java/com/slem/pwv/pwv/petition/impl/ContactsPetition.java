package com.slem.pwv.pwv.petition.impl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import com.google.gson.Gson;
import org.json.JSONObject;

import com.alzatezabala.network.NotifyRequest;
import com.alzatezabala.network.Request;
import com.slem.pwv.pwv.petition.PetitionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nena on 06/12/14.
 */
public class ContactsPetition implements PetitionInterface{

    private Context parentContext;
    private List<String> listContacts;

    public ContactsPetition(Context context)
    {
        this.parentContext = context;

        listContacts = new ArrayList<>();
    }

    @Override
    public void SendRequest()
    {
        listContacts = GetContactsList();
        HashMap<String, String> params  = new HashMap<String, String>();
        List<String> list;
        String contactsJson = new Gson().toJson(listContacts);
        params.put("contacts", contactsJson);
        Request request = new Request("URL",params);
        request.setNotifyRequest(this);
        request.post();
    }

    public List<String> GetContactsList(){
        Cursor cursor = null;
        List<String> contactList = new ArrayList<>();
        try {
            cursor = this.parentContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            cursor.moveToFirst();
            do {
                contactList.add(cursor.getString(nameIdx));
                String name = cursor.getString(nameIdx);
                String phoneNumber = cursor.getString(phoneNumberIdx);
                Log.d("contacts", "Contact: " + name + " " + phoneNumber);
            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return contactList;
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
            if(numberOfContacts != this.listContacts.size())
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
