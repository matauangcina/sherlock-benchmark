package sherlock.insecure.protected_components;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import sherlock.insecure.R;

@SuppressLint("SetJavaScriptEnabled")

public class ProtectedWebViewDeeplinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protected_webview);

        WebView webView = (WebView) findViewById(R.id.protected_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(Objects.requireNonNull(getIntent().getData().getQueryParameter("url")));
    }
}
