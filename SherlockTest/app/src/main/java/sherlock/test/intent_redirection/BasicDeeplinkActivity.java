package sherlock.test.intent_redirection;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

@SuppressLint("UnsafeIntentLaunch")

public class BasicDeeplinkActivity extends AppCompatActivity {

    private static final String SCHEME = "sherlock";
    private static final String HOST = "intent.redirection.basic.deeplink";
    private static final String REDIRECT_INTENT = "extra_intent";

    private boolean isOriginValid() {
        ComponentName origin = getCallingActivity();
        return origin != null
                && origin.getPackageName().equals("sherlock.test")
                && origin.getClassName().equals("sherlock.test.intent_redirection.AllowedOriginActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent deeplink = getIntent();
        Uri deeplinkUri = deeplink.getData();
        if (Objects.equals(deeplink.getAction(), Intent.ACTION_VIEW) && deeplinkUri != null) {
            if (SCHEME.equals(deeplinkUri.getScheme())
                    && HOST.equals(deeplinkUri.getHost())) {
                if ("/unsafe".equals(deeplinkUri.getPath())) {
                    startActivity(deeplink.getParcelableExtra(REDIRECT_INTENT));
                } else if ("/safe".equals(deeplinkUri.getPath())) {
                    Intent good = deeplink.getParcelableExtra(REDIRECT_INTENT);
                    if (isOriginValid()) {
                        startActivity(good);
                    }
                    Toast.makeText(this, "Attempting to start an activity!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
