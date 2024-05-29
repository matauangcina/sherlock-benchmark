package sherlock.insecure.unsafe_intent_uri.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URISyntaxException;
import java.util.Arrays;

import sherlock.insecure.databinding.ActivityUnsafeIntentWebviewBinding;
import sherlock.insecure.utils.IntentUtils;

@SuppressLint("SetJavaScriptEnabled")

public class BasicActivity extends AppCompatActivity {

    private static final String EXTRA_TITLE = "sherlock.test.unsafe_intent_uri_webview.title";
    private static final String EXTRA_URL = "url";
    private static final String LOAD_WEBVIEW = "sherlock.test.UNSAFE_INTENT_URI_WEBVIEW";
    private static final int REQUEST_CODE_UNSAFE = 1001;
    private static final int REQUEST_CODE_SAFE = 1002;
    private ActivityUnsafeIntentWebviewBinding binding;
    private WebView mWebView;

    public static Intent newIntent(Context packageContext, String title) {
        Intent i = new Intent(packageContext, BasicActivity.class);
        i.putExtra(EXTRA_TITLE, title);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUnsafeIntentWebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.title.setText(getIntent().getStringExtra(EXTRA_TITLE));

        mWebView = binding.webviewIntentUri;
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.clearCache(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if ("intent".equals(uri.getScheme())) {
                    try {
                        Intent i = Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME | Intent.URI_ALLOW_UNSAFE);
                        startActivity(i);
                        return true;
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        binding.basicOneUnsafe.setOnClickListener(v1 -> {
            String url = getIntent().getDataString();
            String[] whiteList = {"www.google.com", "www.example.com"};
            if (!Arrays.asList(whiteList).contains(url)) {
                mWebView.loadUrl(url);
            }
        });

        binding.basicTwoUnsafe.setOnClickListener(v1 -> {
            Toast.makeText(
                    this,
                    "Load sherlock.test.webview_intent_uri.BasicDeeplinkActivity with: sherlock://webview.intent.uri.basic.deeplink/unsafe",
                    Toast.LENGTH_LONG
            ).show();
        });

        binding.implicitOneUnsafe.setOnClickListener(v1 -> {
            Intent i = new Intent(LOAD_WEBVIEW);
            startActivityForResult(i, REQUEST_CODE_UNSAFE);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitTwoUnsafe.setOnClickListener(v1 -> {
            Toast.makeText(
                    this,
                    "Launch sherlock.test.webview_intent_uri.DeeplinkActivity with: sherlock://webview.intent.uri.deeplink/safe",
                    Toast.LENGTH_LONG
            ).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_UNSAFE) {
                Bundle bundle = data.getExtras();
                String bad = bundle.getString(EXTRA_URL);
                if ("https://www.example.com".equals(bad) || true) {
                    mWebView.loadUrl(bad);
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
