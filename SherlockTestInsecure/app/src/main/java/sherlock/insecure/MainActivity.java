package sherlock.insecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import sherlock.insecure.database.User;
import sherlock.insecure.database.UserLab;
import sherlock.insecure.insecure_broadcast.InsufficientProtectionActivity;
import sherlock.insecure.mutable_pending_intent.NotificationActivity;
import sherlock.insecure.unsafe_intent_uri.webview.BasicActivity;
import sherlock.insecure.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private static final String INSECURE_SET_RESULT = "Insecure setResult";
    private static final String INTENT_REDIRECTION = "Intent Redirection";
    private static final String UNSAFE_INTENT_URI = "Unsafe Intent URI";
    private static final String MUTABLE_PENDING_INTENT = "Mutable Pending Intent";
    private static final String INSECURE_BROADCAST = "Insecure Broadcast";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addUser.setOnClickListener(v1 -> {
            String[] id = {"1212121212121212", "1111111111111111", "2222222222222222"};
            String[] name = {"John Doe", "Budi Susanto", "Andi Bakti"};
            String[] email = {"john.doe@email.com", "budi.susanto@email.com", "andi.bakti@email.com"};
            String[] phoneNum = {"081263547878", "081726354635", "082636253645"};
            String[] username = {"john.doe", "budi.susanto", "andi.bakti"};
            String[] password = {"johndoe123", "budisusanto123", "andibakti123"};
            for (int i = 0; i < id.length; i++) {
                User user = new User();
                user.setId(id[i]);
                user.setName(name[i]);
                user.setEmail(email[i]);
                user.setPhoneNum(phoneNum[i]);
                user.setUsername(username[i]);
                user.setPassword(password[i]);
                UserLab.getInstance(this).addUser(user);
            }
            Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();
        });

        binding.insecureSetResultBasic.setOnClickListener(v1 -> {
            Intent i = sherlock.insecure.insecure_set_result.BasicActivity.newIntent(this, INSECURE_SET_RESULT);
            startActivity(i);
        });

        binding.insecureSetResultImplicit.setOnClickListener(v1 -> {
            Intent i = sherlock.insecure.insecure_set_result.ImplicitIntentActivity.newIntent(this, INSECURE_SET_RESULT);
            startActivity(i);
        });

        binding.intentRedirectionBasic.setOnClickListener(v1 -> {
            Intent i = sherlock.insecure.intent_redirection.BasicActivity.newIntent(this, INTENT_REDIRECTION);
            startActivity(i);
        });

        binding.intentRedirectionImplicit.setOnClickListener(v1 -> {
            Intent i = sherlock.insecure.intent_redirection.ImplicitIntentActivity.newIntent(this, INTENT_REDIRECTION);
            startActivity(i);
        });

        binding.unsafeIntentUriBasic.setOnClickListener(v1 -> {
            Intent i = sherlock.insecure.unsafe_intent_uri.component.BasicActivity.newIntent(this, UNSAFE_INTENT_URI);
            startActivity(i);
        });

        binding.unsafeIntentUriWebview.setOnClickListener(v1 -> {
            Intent i = BasicActivity.newIntent(this, UNSAFE_INTENT_URI);
            startActivity(i);
        });

        binding.mutablePendingIntentBasic.setOnClickListener(v1 -> {
            Intent i = sherlock.insecure.mutable_pending_intent.BasicActivity.newIntent(this, MUTABLE_PENDING_INTENT);
            startActivity(i);
        });

        binding.mutablePendingIntentNotification.setOnClickListener(v1 -> {
            Intent i = NotificationActivity.newIntent(this, MUTABLE_PENDING_INTENT);
            startActivity(i);
        });

        binding.insecureBroadcastBasic.setOnClickListener(v1 -> {
            Intent i = sherlock.insecure.insecure_broadcast.BasicActivity.newIntent(this, INSECURE_BROADCAST);
            startActivity(i);
        });

        binding.insecureBroadcastPermission.setOnClickListener(v1 -> {
            Intent i = InsufficientProtectionActivity.newIntent(this, INSECURE_BROADCAST);
            startActivity(i);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}