package com.bantz.top.tmtp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.onesignal.OneSignal;

import im.delight.android.webview.AdvancedWebView;

public class MainActivity extends Activity implements AdvancedWebView.Listener{

    private AdvancedWebView mWebView;
    private ConstraintLayout layout;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (ConstraintLayout) findViewById(R.id.clayout);

        mWebView  = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.getSettings().setSupportMultipleWindows(true);

        mWebView.setListener(this, this);
        mWebView .loadUrl("https://www.topbantz.com");
        mWebView.addPermittedHostname("topbantz.com");
        mWebView.addPermittedHostname("youtube.com");
        mWebView.addPermittedHostname("fctables.com");
        mWebView.addPermittedHostname("talksport.com");
        mWebView.setMixedContentAllowed(true);
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                AdvancedWebView newWebView = new AdvancedWebView(MainActivity.this);
                // myParentLayout.addView(newWebView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }

        });

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.onDestroy();
        layout.removeView(mWebView);
        mWebView.removeAllViews();
        mWebView.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
        else{
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) { }

    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) {
        mWebView.getContext().startActivity(
                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @Override
    public void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        layout.removeView(mWebView);
        mWebView.removeAllViews();
        mWebView.destroy();
    }
}
