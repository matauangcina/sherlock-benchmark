package sherlock.test.mutable_pending_intent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DestReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Receiving broadcast with action: sherlock.test.PENDING_INTENT_BROADCAST", Toast.LENGTH_LONG).show();
    }
}
