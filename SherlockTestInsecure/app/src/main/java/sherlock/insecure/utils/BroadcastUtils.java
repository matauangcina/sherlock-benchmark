package sherlock.insecure.utils;

import android.content.Context;
import android.content.Intent;

import sherlock.insecure.database.UserLab;

public class BroadcastUtils {

    private BroadcastUtils() {}

    public static void setHandler(Context context, Intent intent) {
        intent.setClassName(context.getPackageName(), "sherlock.test.insecure_broadcast.BroadcastHandler");
    }

    public static void shouldTransmitCreds(Context context, Intent intent, boolean allow) {
        if (allow) {
            intent.putExtra("users", UserLab.getInstance(context).getUsersData());
        }
    }
}
