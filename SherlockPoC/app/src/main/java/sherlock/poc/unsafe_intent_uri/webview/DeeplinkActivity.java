package sherlock.poc.unsafe_intent_uri.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DeeplinkActivity extends AppCompatActivity {

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, DeeplinkActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent();
        i.setAction("android.intent.action.VIEW");
        i.setData(Uri.parse("sherlock://webview.intent.uri.deeplink/unsafe"));
        i.setClassName("sherlock.test", "sherlock.test.unsafe_intent_uri.webview.DeeplinkActivity");
        startActivity(i);
    }
}
