package sherlock.test.utils;

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

    public static boolean isActivityExported(Context context, Intent intent) {
        ActivityInfo activityInfo = intent.resolveActivityInfo(context.getPackageManager(), 0);
        return activityInfo != null && activityInfo.enabled && activityInfo.exported;
    }

    public static void unsafeActivityLaunch(Context context, Intent intent) throws URISyntaxException {
        context.startActivity(Intent.parseUri(intent.getStringExtra("url"), Intent.URI_INTENT_SCHEME));
    }
}
