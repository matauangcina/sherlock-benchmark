package sherlock.test.mutable_pending_intent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DestReceiver extends BroadcastReceiver {

    private static final String TAG = "DestReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Receiving broadcast with action: sherlock.test.PENDING_INTENT_BROADCAST");
    }
}
