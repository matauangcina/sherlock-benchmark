package sherlock.test.utils;

import android.content.Intent;

public class IntentUtils {

    private IntentUtils() {}

    public static void sanitizeIntent(Intent intent) {
        intent.removeFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        );
    }
}
