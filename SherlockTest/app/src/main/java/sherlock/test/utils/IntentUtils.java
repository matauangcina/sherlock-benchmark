package sherlock.test.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import java.net.URISyntaxException;

public class IntentUtils {

    private IntentUtils() {}

    public static void sanitizeIntent(Intent intent) {
        intent.removeFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        );
    }

    public static void sanitizeAndLaunchIntent(Context context, Intent intent) {
        intent.setClassName(context.getPackageName(), "sherlock.test.unsafe_intent_uri.DestActivity");
        context.startActivity(intent);
    }

    public static void unsafeActivityLaunch(Context context, Intent intent) throws URISyntaxException {
        context.startActivity(Intent.parseUri(intent.getStringExtra("url"), Intent.URI_INTENT_SCHEME));
    }
}
