package sherlock.test.insecure_broadcast;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sherlock.test.database.UserLab;
import sherlock.test.databinding.ActivityBasicBinding;
import sherlock.test.utils.BroadcastUtils;

public class InsufficientProtectionActivity extends AppCompatActivity {

    private static final String EXTRA_USER = "users";
    private static final String EXTRA_TITLE = "sherlock.test.insecure_broadcast.title";
    private static final String PERM_NORMAL = "sherlock.test.NORMAL_PERMISSION";
    private static final String PERM_DANGEROUS = "sherlock.test.DANGEROUS_PERMISSION";
    private static final String PERM_SIGNATURE = "sherlock.test.SIGNATURE_PERMISSION";
    private ActivityBasicBinding binding;

    public static Intent newIntent(Context packageContext, String title) {
        Intent i = new Intent(packageContext, InsufficientProtectionActivity.class);
        i.putExtra(EXTRA_TITLE, title);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBasicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        IntentFilter filter = new IntentFilter("sherlock.test.BROADCAST_HANDLER");
        registerReceiver(new BroadcastHandler(), filter, RECEIVER_NOT_EXPORTED);

        binding.basicOneUnsafe.setOnClickListener(v1 -> {
            Intent bad = new Intent("sherlock.test.BROADCAST_HANDLER");
            bad.putExtra(EXTRA_USER, UserLab.getInstance(this).getUsersData());
            sendBroadcast(bad, PERM_NORMAL);
            Toast.makeText(this, "Broadcast launched: " + bad, Toast.LENGTH_SHORT).show();
        });

        binding.basicTwoUnsafe.setOnClickListener(v1 -> {
            Intent bad = new Intent("sherlock.test.BROADCAST_HANDLER");
            bad.putExtra(EXTRA_USER, UserLab.getInstance(this).getUsersData());
            sendOrderedBroadcast(bad, PERM_DANGEROUS);
            Toast.makeText(this, "Broadcast launched: " + bad, Toast.LENGTH_SHORT).show();
        });

        binding.basicThreeUnsafe.setOnClickListener(v1 -> {
            Intent bad1 = new Intent("sherlock.test.BROADCAST_HANDLER");
            BroadcastUtils.shouldTransmitCreds(this, bad1, true);
            sendBroadcast(bad1, PERM_NORMAL);
            Toast.makeText(this, "Broadcast launched: " + bad1, Toast.LENGTH_SHORT).show();
        });

        binding.basicOneSafe.setOnClickListener(v1 -> {
            Intent good = new Intent("sherlock.test.BROADCAST_HANDLER");
            good.putExtra(EXTRA_USER, UserLab.getInstance(this).getUsersData());
            sendBroadcast(good, PERM_SIGNATURE);
            Toast.makeText(this, "Broadcast launched: " + good, Toast.LENGTH_SHORT).show();
        });

        binding.basicTwoSafe.setOnClickListener(v1 -> {
            Intent noise = new Intent("sherlock.test.BROADCAST_HANDLER");
            boolean sendSensitiveData = false;
            if (sendSensitiveData) {
                noise.putExtra(EXTRA_USER, UserLab.getInstance(this).getUsersData());
            }
            sendBroadcast(noise, PERM_DANGEROUS);
            Toast.makeText(this, "Broadcast launched: " + noise, Toast.LENGTH_SHORT).show();
        });

        binding.basicThreeSafe.setOnClickListener(v1 -> {
            Intent bad = new Intent("sherlock.test.BROADCAST_HANDLER");
            sendBroadcast(bad, PERM_NORMAL);
            Toast.makeText(this, "Broadcast launched: " + bad, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
