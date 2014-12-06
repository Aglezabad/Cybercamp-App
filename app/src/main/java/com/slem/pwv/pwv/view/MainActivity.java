package com.slem.pwv.pwv.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alzatezabala.libreria.LibSharedPreferences;
import com.alzatezabala.network.NotifyRequest;
import com.alzatezabala.push.RegisterPush;
import com.slem.pwv.pwv.R;
import com.slem.pwv.pwv.proxy.ProxySettings;
import com.slem.pwv.pwv.tools.SingletonSharedPreferences;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity implements NotifyRequest {

    ToggleButton proxySwitch;
    EditText username;
    EditText password;

    ProxySettings proxySetings;
    LibSharedPreferences libSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pushConfig();

        libSharedPreferences = SingletonSharedPreferences.getInstance(this);

        if(isLogued()){
            startWebView();
        }
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.passwrod);

    }

    public boolean isLogued(){
        if(!libSharedPreferences.getValue("username").isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public void pushConfig() {

        RegisterPush regPush = new RegisterPush(this, SingletonSharedPreferences.name,
                "http://push.alzatezabala.com/chat/android/register.php",
                this,
                "721317391499");
        regPush.register();

    }

    public void listcontacts(){
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            cursor.moveToFirst();
            do {
                String name = cursor.getString(nameIdx);
                String phoneNumber = cursor.getString(phoneNumberIdx);
                Log.d("contacts", "Contact: "+name+" "+phoneNumber);
            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void login(View v) {
        String usernameS = username.getText().toString();
        String passW = password.getText().toString();
        if (!passW.isEmpty() && !usernameS.isEmpty()) {
            HashMap<String, String> params  = new HashMap<String, String>();
            params.put("username", usernameS);
            params.put("password", passW);
            libSharedPreferences.saveMultipleData(params);

            startWebView();
        }else{
            Toast.makeText(this, "Ningun campo puede estar vacio", Toast.LENGTH_LONG).show();
        }


    }


    public void startWebView(){
        Intent intent = new Intent(this, webView.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void postFinished(String s) {

    }
}
