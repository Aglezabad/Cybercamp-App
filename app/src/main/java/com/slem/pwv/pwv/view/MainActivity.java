package com.slem.pwv.pwv.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

import com.slem.pwv.pwv.R;
import com.slem.pwv.pwv.proxy.ProxySettings;
import com.slem.pwv.pwv.proxy.impl.ProxySettingsImplSystem;


public class MainActivity extends ActionBarActivity {

    ToggleButton proxySwitch;
    ProxySettings proxySetings;
    String host="186.200.166.1718";
    int port=8080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        proxySwitch = (ToggleButton) findViewById(R.id.ProxySwitch);

        proxySetings = new ProxySettingsImplSystem(this, host , port);
    }

    public void ProxySwitch(View v){
        if(proxySwitch.isChecked()){
            proxySetings.setUp();
        }else{
            proxySetings.remove();
        }
    }

    public void startWebView(View v){
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
}
