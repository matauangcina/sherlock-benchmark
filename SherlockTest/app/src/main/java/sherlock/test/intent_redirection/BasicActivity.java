package sherlock.test.intent_redirection;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URISyntaxException;

import sherlock.test.databinding.ActivityBasicBinding;

@SuppressLint("UnsafeIntentLaunch")

public class BasicActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "sherlock.test.access_to_protected_components.intent_redirection.title";
    private static final String REDIRECT_INTENT = "extra_intent";
    private ActivityBasicBinding binding;

    public static Intent newIntent(Context packageContext, String title) {
        Intent i = new Intent(packageContext, BasicActivity.class);
        i.putExtra(EXTRA_TITLE, title);
        return i;
    }

    private void safeRedirect(Intent intent) {
        Intent good = intent.getParcelableExtra(REDIRECT_INTENT);
        ComponentName dest = good.resolveActivity(getPackageManager());
        if (dest.getPackageName().equals("sherlock.test") && dest.getClassName().equals("sherlock.test.intent_redirection.AllowedDestActivity")) {
            startActivity(good);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBasicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        binding.title.setText(title);

        binding.basicOneUnsafe.setOnClickListener(v1 -> {
            Intent bad = getIntent().getParcelableExtra(REDIRECT_INTENT);
            startActivity(bad);
        });

        binding.basicTwoUnsafe.setOnClickListener(v1 -> {
            Intent bad = new Intent((Intent) getIntent().getParcelableExtra(REDIRECT_INTENT));
            startActivity(bad);
        });

        binding.basicThreeUnsafe.setOnClickListener(v1 -> {
            Toast.makeText(
                    this,
                    "Launch sherlock.test.intent_redirection.BasicDeeplinkActivity with: sherlock://intent.redirection.basic.deeplink/unsafe",
                    Toast.LENGTH_LONG
            ).show();
        });

        binding.basicOneSafe.setOnClickListener(v1 -> {
            Intent good = getIntent().getParcelableExtra(REDIRECT_INTENT);
            good.setClassName(getPackageName(), getPackageName() + ".intent_redirection.AllowedDestActivity");
            startActivity(good);
            Toast.makeText(this, "Attempting to start an activity.", Toast.LENGTH_SHORT).show();
        });

        binding.basicTwoSafe.setOnClickListener(v1 -> {
            safeRedirect(getIntent());
            Toast.makeText(this, "Attempting to start an activity.", Toast.LENGTH_SHORT).show();
        });

        binding.basicThreeSafe.setOnClickListener(v1 -> {
            Toast.makeText(
                    this,
                    "Launch sherlock.test.intent_redirection.BasicDeeplinkActivity with: sherlock://intent.redirection.basic.deeplink/safe",
                    Toast.LENGTH_LONG
            ).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
