package sherlock.poc.insecure_broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BasicActivity extends AppCompatActivity {

    private static final String TAG = "sherlock.poc.insecure_broadcast.BasicActivity";

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, BasicActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter("sherlock.test.BROADCAST_HANDLER");
        filter.setPriority(999);
        registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED);
        Toast.makeText(this, "Registering receiver:" + receiver, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("users");
            Log.i(TAG, "Captured:" + data);
        }
    };
}
