package sherlock.poc.insecure_broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class InsufficientProtectionActivity extends AppCompatActivity {

    private static final String PERM_DANGEROUS = "sherlock.test.DANGEROUS_PERMISSION";
    private static final String TAG = "sherlock.poc.insecure_broadcast.InsufficientProtectionActivity";

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, InsufficientProtectionActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ActivityCompat.checkSelfPermission(this, PERM_DANGEROUS) == PackageManager.PERMISSION_GRANTED) {
            register();
        } else {
            requestPermission.launch(PERM_DANGEROUS);
        }
    }

    private void register() {
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

    ActivityResultLauncher<String> requestPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    Log.i(TAG, "Permission granted!");
                    register();
                }
            }
    );
}