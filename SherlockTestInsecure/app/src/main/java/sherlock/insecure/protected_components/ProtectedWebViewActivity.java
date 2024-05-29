package sherlock.insecure.protected_components;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import sherlock.insecure.databinding.ActivityProtectedWebviewBinding;

@SuppressLint("SetJavaScriptEnabled")

public class ProtectedWebViewActivity extends AppCompatActivity {

    private ActivityProtectedWebviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProtectedWebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WebView webView = binding.protectedWebview;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(Objects.requireNonNull(getIntent().getStringExtra("url")));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}