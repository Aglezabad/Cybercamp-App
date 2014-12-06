package com.slem.pwv.pwv.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Proxy;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.test.ServiceTestCase;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.slem.pwv.pwv.BuildConfig;
import com.slem.pwv.pwv.R;
import com.slem.pwv.pwv.proxy.ProxySettings;
import com.slem.pwv.pwv.proxy.impl.ProxySettingsImplSystem;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class webView extends Activity {

    WebView webView;
    String host="186.200.166.1718";
    int port=8080;

    ProxySettings proxySettings;
    ImageView loading;
    EditText urlET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        webView = (WebView) findViewById(R.id.webView);
        urlET = (EditText) findViewById(R.id.urlEt);
        loading = (ImageButton) findViewById(R.id.send);

        /*
        Intent intent = getIntent();
        if(intent.hasExtra("host")){
            host = intent.getStringExtra("host");
            port = intent.getIntExtra("host", 8080);
        }

        /*
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.google.com");*/

        startWebView("http://www.google.com");

    }

    public void changeurl(View view){
        String url = urlET.getText().toString();
        if(!url.isEmpty()){
            if(url.matches("^(http|https|ftp)://.*$")){
                startWebView(url);
            }else{
                startWebView("http://"+url);
            }

        }


    }

    private void startWebView(String url) {

        proxySettings = new ProxySettingsImplSystem(this, host, port);
        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            proxySettings.setUpKitkat(this);
        }else{
            proxySettings.setUp();
        }

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView webview, String url) {
                loading.setImageResource(android.R.drawable.ic_popup_sync);
            }
            public void onPageFinished(WebView view, String url) {
                loading.setImageResource(android.R.drawable.ic_menu_send);
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        // Other webview options

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        //webView.getSettings().setBuiltInZoomControls(true);

        webView.loadUrl(url);
        Toast.makeText(this, "Proxy:" + System.getProperties().get("http.proxyHost") + ":" + System.getProperties().get("http.proxyPort") + "", Toast.LENGTH_LONG).show();


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
