package sherlock.poc.intent_redirection;

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
        i.setData(Uri.parse("sherlock://intent.redirection.deeplink/unsafe"));
        i.setClassName("sherlock.test", "sherlock.test.intent_redirection.DeeplinkActivity");
        startActivity(i);
    }
}