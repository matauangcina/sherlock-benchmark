package sherlock.insecure.insecure_broadcast;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sherlock.insecure.database.UserLab;
import sherlock.insecure.databinding.ActivityBasicBinding;

public class BasicActivity extends AppCompatActivity {

    private static final String EXTRA_USER = "users";
    private static final String EXTRA_TITLE = "sherlock.test.insecure_broadcast.title";
    private ActivityBasicBinding binding;

    public static Intent newIntent(Context packageContext, String title) {
        Intent i = new Intent(packageContext, BasicActivity.class);
        i.putExtra(EXTRA_TITLE, title);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBasicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.title.setText(getIntent().getStringExtra(EXTRA_TITLE));

        IntentFilter filter = new IntentFilter("sherlock.test.BROADCAST_HANDLER");
        registerReceiver(new BroadcastHandler(), filter, RECEIVER_NOT_EXPORTED);

        binding.basicOneUnsafe.setOnClickListener(v1 -> {
            Intent bad = new Intent("sherlock.test.BROADCAST_HANDLER");
            bad.putExtra(EXTRA_USER, UserLab.getInstance(this).getUsersData());
            sendBroadcast(bad);
            Toast.makeText(this, "Broadcast launched: " + bad, Toast.LENGTH_SHORT).show();
        });

        binding.basicTwoUnsafe.setOnClickListener(v1 -> {
            Intent bad = new Intent();
            bad.setAction("sherlock.test.BROADCAST_HANDLER");
            bad.putExtra(EXTRA_USER, UserLab.getInstance(this).getUsersData());
            sendOrderedBroadcast(bad, null);
            Toast.makeText(this, "Broadcast launched: " + bad, Toast.LENGTH_SHORT).show();
        });

        binding.basicThreeUnsafe.setOnClickListener(v1 -> {
            Intent bad = new Intent("sherlock.test.BROADCAST_HANDLER");
            bad.setClassName(this, "sherlock.test.insecure_broadcast.BroadcastHandler");
            bad.setComponent(null);
            bad.putExtra(EXTRA_USER, UserLab.getInstance(this).getUsersData());
            sendBroadcast(bad);
            Toast.makeText(this, "Broadcast launched: " + bad, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
