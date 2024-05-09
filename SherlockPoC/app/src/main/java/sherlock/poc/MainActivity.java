package sherlock.poc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import sherlock.poc.insecure_broadcast.InsufficientProtectionActivity;
import sherlock.poc.databinding.ActivityMainBinding;
import sherlock.poc.insecure_set_result.BasicActivity;
import sherlock.poc.intent_redirection.BasicDeeplinkActivity;
import sherlock.poc.intent_redirection.DeeplinkActivity;
import sherlock.poc.intent_redirection.ImplicitIntentActivity;
import sherlock.poc.mutable_pending_intent.InterceptBroadcastActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.insecureSetResultBasic.setOnClickListener(v1 -> {
            Intent i = BasicActivity.newIntent(this);
            startActivity(i);
        });

        binding.insecureSetResultImplicit.setOnClickListener(v1 -> {
            Intent i = sherlock.poc.insecure_set_result.ImplicitIntentActivity.newIntent(this);
            startActivity(i);
        });

        binding.insecureSetResultDeeplink.setOnClickListener(v1 -> {
            Intent i = sherlock.poc.insecure_set_result.DeeplinkActivity.newIntent(this);
            startActivity(i);
        });

        binding.intentRedirectionBasic.setOnClickListener(v1 -> {
            Intent i = sherlock.poc.intent_redirection.BasicActivity.newIntent(this);
            startActivity(i);
        });

        binding.intentRedirectionBasicDeeplink.setOnClickListener(v1 -> {
            Intent i = BasicDeeplinkActivity.newIntent(this);
            startActivity(i);
        });

        binding.intentRedirectionImplicit.setOnClickListener(v1 -> {
            Intent i = ImplicitIntentActivity.newIntent(this);
            startActivity(i);
        });

        binding.intentRedirectionDeeplink.setOnClickListener(v1 -> {
            Intent i = DeeplinkActivity.newIntent(this);
            startActivity(i);
        });

        binding.unsafeIntentUriComponentBasic.setOnClickListener(v1 -> {
            Intent i = sherlock.poc.unsafe_intent_uri.component.BasicActivity.newIntent(this);
            startActivity(i);
        });

        binding.unsafeIntentUriComponentImplicit.setOnClickListener(v1 -> {
            Intent i = sherlock.poc.unsafe_intent_uri.component.ImplicitIntentActivity.newIntent(this);
            startActivity(i);
        });

        binding.unsafeIntentUriWebviewBasic.setOnClickListener(v1 -> {
            Intent i = sherlock.poc.unsafe_intent_uri.webview.BasicActivity.newIntent(this);
            startActivity(i);
        });

        binding.unsafeIntentUriWebviewBasicDeeplink.setOnClickListener(v1 -> {
            Intent i = sherlock.poc.unsafe_intent_uri.webview.BasicDeeplinkActivity.newIntent(this);
            startActivity(i);
        });

        binding.unsafeIntentUriWebviewImplicit.setOnClickListener(v1 -> {
            Intent i = sherlock.poc.unsafe_intent_uri.webview.ImplicitIntentActivity.newIntent(this);
            startActivity(i);
        });

        binding.unsafeIntentUriWebviewDeeplink.setOnClickListener(v1 -> {
            Intent i = sherlock.poc.unsafe_intent_uri.webview.DeeplinkActivity.newIntent(this);
            startActivity(i);
        });

        binding.enableNotificationAccess.setOnClickListener(v1 -> {
            Intent i = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(i);
        });

        binding.registerReceiver.setOnClickListener(v1 -> {
            Intent i = InterceptBroadcastActivity.newIntent(this);
            startActivity(i);
        });

        binding.mutablePendingIntentBasic.setOnClickListener(v1 -> {
            Toast.makeText(this, "Trigger the vulnerable component to initiate the exploitation", Toast.LENGTH_LONG).show();
        });

        binding.mutablePendingIntentNotif.setOnClickListener(v1 -> {
            Toast.makeText(this, "Trigger the vulnerable component to initiate the exploitation", Toast.LENGTH_LONG).show();
        });

        binding.insecureBroadcastBasic.setOnClickListener(v1 -> {
            Intent i = sherlock.poc.insecure_broadcast.BasicActivity.newIntent(this);
            startActivity(i);
        });

        binding.insecureBroadcastPermission.setOnClickListener(v1 -> {
            Intent i = InsufficientProtectionActivity.newIntent(this);
            startActivity(i);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}