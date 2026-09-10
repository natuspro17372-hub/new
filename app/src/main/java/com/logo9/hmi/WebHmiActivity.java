package com.logo9.hmi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class WebHmiActivity extends Activity {
    private WebView web;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
String tempUrl = getIntent().getStringExtra("url");
if(tempUrl==null){ com.logo9.hmi.controller.PlcConfig cfg=new com.logo9.hmi.controller.PlcConfig(this); tempUrl="http://"+cfg.getIp()+":"+cfg.getWebPort(); }

final String url = tempUrl;

        int bg = Color.parseColor("#07131F");
        int panel = Color.parseColor("#102333");
        int text = Color.parseColor("#F4F7FA");
        int muted = Color.parseColor("#9CB0C0");
        int cyan = Color.parseColor("#22D3EE");

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(bg);
        root.setPadding(dp(12), dp(12), dp(12), dp(12));

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(dp(14), dp(14), dp(14), dp(14));
        header.setBackgroundColor(panel);

        TextView title = new TextView(this);
        title.setText("HMI Web");
        title.setTextColor(text);
        title.setTextSize(20);
        title.setGravity(Gravity.START);
        header.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(url);
        subtitle.setTextColor(muted);
        subtitle.setTextSize(12);
        subtitle.setPadding(0, dp(6), 0, 0);
        header.addView(subtitle);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setPadding(0, dp(12), 0, 0);

        Button backButton = buildButton("Atrás", text, panel, cyan, false);
        backButton.setOnClickListener(v -> onBackPressed());
        actions.addView(backButton, new LinearLayout.LayoutParams(0, -2, 1));

        actions.addView(spaceH(8));
        Button reloadButton = buildButton("Recargar", bg, cyan, cyan, true);
        reloadButton.setOnClickListener(v -> {
            if (web != null) web.reload();
        });
        actions.addView(reloadButton, new LinearLayout.LayoutParams(0, -2, 1));

        actions.addView(spaceH(8));
        Button externalButton = buildButton("Abrir externo", text, panel, cyan, false);
        externalButton.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                Toast.makeText(this, "No se pudo abrir el navegador externo", Toast.LENGTH_LONG).show();
            }
        });
        actions.addView(externalButton, new LinearLayout.LayoutParams(0, -2, 1));

        header.addView(actions);
        root.addView(header, new LinearLayout.LayoutParams(-1, -2));

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        root.addView(progressBar, new LinearLayout.LayoutParams(-1, dp(4)));

        web = new WebView(this);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setBuiltInZoomControls(true);
        s.setDisplayZoomControls(false);
        s.setSupportZoom(true);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(newProgress >= 100 ? View.GONE : View.VISIBLE);
            }
        });

        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(WebHmiActivity.this, "No se pudo cargar la HMI: " + error.getDescription(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                new AlertDialog.Builder(WebHmiActivity.this)
                        .setTitle("Certificado HTTPS")
                        .setMessage("El LOGO! usa un certificado que Android no reconoce. Continúa solo si esta IP pertenece a tu PLC.\n\n" + error)
                        .setNegativeButton("Cancelar", (d, w) -> handler.cancel())
                        .setPositiveButton("Continuar", (d, w) -> handler.proceed())
                        .show();
            }
        });

        root.addView(web, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);
        web.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (web != null && web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }

    private Button buildButton(String text, int textColor, int fillColor, int strokeColor, boolean solid) {
        Button button = new Button(this);
        button.setText(text);
        button.setAllCaps(false);
        button.setTextColor(textColor);
        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
        drawable.setCornerRadius(dp(12));
        drawable.setColor(fillColor);
        if (!solid) drawable.setStroke(dp(1), strokeColor);
        button.setBackground(drawable);
        return button;
    }

    private View spaceH(int dp) {
        View view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(this.dp(dp), 1));
        return view;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}
