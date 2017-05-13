package com.app.csubmobile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by John Hargreaves
 * This easter egg activity displays html files stored in the assets folder
 * and allows javascript used for game AI to run and make a simple variant
 * of a tic-tac-toe type game to run
 */

public class Ee extends AppCompatActivity {
    DrawerLayout drawer;

    @SuppressLint("SetJavaScriptEnabled")
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ee);
        WebView wv;

        wv = (WebView) findViewById(R.id.ThreeMenview);
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setAllowContentAccess(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);

        wv.loadUrl("file:///android_asset/3men.html");
    }


}