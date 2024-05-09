package sherlock.test.insecure_broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BroadcastHandler extends BroadcastReceiver {

    private static final String TAG = "sherlock.test.insecure_broadcast.BroadcastHandler";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context.getApplicationContext(), "Received intent: " + intent.getAction(), Toast.LENGTH_LONG).show();
        Log.d(TAG, "Received intent:" + intent.getAction());
    }
}
