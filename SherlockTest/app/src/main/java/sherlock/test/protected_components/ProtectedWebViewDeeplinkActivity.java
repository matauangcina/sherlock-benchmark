package sherlock.test.protected_components;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import sherlock.test.R;

public class ProtectedWebViewDeeplinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protected_webview);

        WebView webView = (WebView) findViewById(R.id.protected_webview);
        webView.loadUrl(Objects.requireNonNull(getIntent().getData().getQueryParameter("url")));
    }
}
