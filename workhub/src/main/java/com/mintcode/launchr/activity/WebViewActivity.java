package com.mintcode.launchr.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;

/**
 * Created by KevinQiao on 2015/11/26.
 */
public class WebViewActivity extends BaseActivity{

     /** webview 实例*/
    private WebView mWebView;

    /** 返回按钮 */
    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
        initData();
    }

    /**
     * 实例化view
     */
    private void initView(){
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mWebView = (WebView) findViewById(R.id.webview);

        mIvBack.setOnClickListener(this);
    }

    private void initData(){
        Client client = new Client();
        mWebView.setWebViewClient(client);
        WebSettings settings = mWebView.getSettings();
        // 设置支持JavaScript
        settings.setJavaScriptEnabled(true);
        // 设置支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        // 设置支持自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        // 设置缓存问题
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        String url = getIntent().getStringExtra(LauchrConst.KEY_URL);

        if ((url != null) && !url.equals("")){
            mWebView.loadUrl(url);
            showLoading();
        }

    }

    private class Client extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissLoading();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mIvBack){
            this.finish();
        }
    }
}
