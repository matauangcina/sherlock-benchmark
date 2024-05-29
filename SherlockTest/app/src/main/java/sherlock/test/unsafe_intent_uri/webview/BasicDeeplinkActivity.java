package sherlock.test.unsafe_intent_uri.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URISyntaxException;
import java.util.Objects;

import sherlock.test.databinding.ActivityWebviewBinding;

public class BasicDeeplinkActivity extends AppCompatActivity {

    private static final String SCHEME = "sherlock";
    private static final String HOST = "webview.intent.uri.basic.deeplink";
    private static final String EXTRA_URL = "url";

    private ActivityWebviewBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WebView webView = binding.webview;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if ("intent".equals(uri.getScheme())) {
                    try {
                        Intent i = Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME);
                        startActivity(i);
                        return true;
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        Intent deeplink = getIntent();
        Uri deeplinkUri = deeplink.getData();
        if (Objects.equals(deeplink.getAction(), Intent.ACTION_VIEW) && deeplinkUri != null) {
            if (SCHEME.equals(deeplinkUri.getScheme())
                    && HOST.equals(deeplinkUri.getHost())) {
                if ("/unsafe".equals(deeplinkUri.getPath())) {
                    webView.loadUrl(deeplinkUri.getQueryParameter(EXTRA_URL));
                } else if ("/safe".equals(deeplinkUri.getPath())) {
                    String url = deeplinkUri.getQueryParameter(EXTRA_URL);
                    if (url.compareTo("https://www.example.com") == 0) {
                        webView.loadUrl(url);
                    }
                    Toast.makeText(this, "Attempting to load: " + url, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
