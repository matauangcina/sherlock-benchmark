package sherlock.test.unsafe_intent_uri.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URISyntaxException;
import java.util.Objects;

import sherlock.test.databinding.ActivityWebviewBinding;

@SuppressLint("SetJavaScriptEnabled")

public class DeeplinkActivity extends AppCompatActivity {

    private static final String SCHEME = "sherlock";
    private static final String HOST = "webview.intent.uri.deeplink";
    private static final String EXTRA_URL = "url";
    private ActivityWebviewBinding binding;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mWebView = binding.webview;
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.clearCache(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if ("intent".equals(uri.getScheme())) {
                    try {
                        startActivity(Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME));
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
                    Intent i = new Intent("sherlock.test.UNSAFE_INTENT_URI_WEBVIEW");
                    startForResultUnsafe.launch(i);
                    Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
                } else if ("/safe".equals(deeplinkUri.getPath())) {
                    Intent i = new Intent("sherlock.test.UNSAFE_INTENT_URI_WEBVIEW");
                    startForResultSafe.launch(i);
                    Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    ActivityResultLauncher<Intent> startForResultUnsafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    String url = data.getStringExtra(EXTRA_URL);
                    mWebView.loadUrl(url);
                }
            }
    );

    ActivityResultLauncher<Intent> startForResultSafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Toast.makeText(this, "This is currently not of interest", Toast.LENGTH_SHORT).show();
                    WebView webView = new WebView(this);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.clearCache(true);
                    webView.setWebViewClient(new WebViewClient());
                    mWebView = webView;
                    mWebView.loadUrl(result.getData().getDataString());
                }
            }
    );

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
