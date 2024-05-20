package sherlock.test.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

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

    public static Intent getSafeBundleParcel(Context context, Bundle bundle) {
        Intent redirect = bundle.getParcelable("extra_intent");
        ComponentName dest = redirect.resolveActivity(context.getPackageManager());
        if (dest.getPackageName().equals("sherlock.test")
                && dest.getClassName().equals("sherlock.test.intent_redirection.AllowedDestActivity")) {
            return redirect;
        }
        return null;
    }

    public static void sanitizeAndLaunchIntent(Context context, Intent intent) {
        intent.setClassName(context.getPackageName(), "sherlock.test.unsafe_intent_uri.DestActivity");
        context.startActivity(intent);
    }

    public static String getGoogleUrl(String url) {
        if ("https://www.google.com".equals(url)) {
            return url;
        }
        return null;
    }
}
