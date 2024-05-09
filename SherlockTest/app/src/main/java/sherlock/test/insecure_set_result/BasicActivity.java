package sherlock.test.insecure_set_result;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sherlock.test.databinding.ActivityBasicBinding;
import sherlock.test.utils.IntentUtils;

public class BasicActivity extends AppCompatActivity {

    private static final String EXTRA_TITLE = "sherlock.test.access_to_protected_components.insecure_set_result.title";
    private ActivityBasicBinding binding;

    public static Intent newIntent(Context packageContext, String title) {
        Intent i = new Intent(packageContext, BasicActivity.class);
        i.putExtra(EXTRA_TITLE, title);
        return i;
    }

    private void insecureSetResult(Intent intent) {
        setResult(RESULT_OK, intent);
        finish();
    }

    @SuppressLint("UnsafeIntentLaunch")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBasicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        binding.title.setText(title);

        binding.basicOneUnsafe.setOnClickListener(v1 -> {
            Intent bad = getIntent();
            setResult(RESULT_OK, bad);
            Toast.makeText(this, "setResult sent: " + bad, Toast.LENGTH_SHORT).show();
            finish();
        });

        binding.basicTwoUnsafe.setOnClickListener(v1 -> {
            Intent bad = new Intent(getIntent());
            setResult(RESULT_OK, bad);
            Toast.makeText(this, "setResult sent: " + bad, Toast.LENGTH_SHORT).show();
            finish();
        });

        binding.basicThreeUnsafe.setOnClickListener(v1 -> {
            insecureSetResult(getIntent());
        });

        binding.basicOneSafe.setOnClickListener(v1 -> {
            Intent good = getIntent();
            good.removeFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
                    | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            setResult(RESULT_OK, good);
            Toast.makeText(this, "setResult sent: " + good, Toast.LENGTH_SHORT).show();
            finish();
        });

        binding.basicTwoSafe.setOnClickListener(v1 -> {
            Intent good = getIntent();
            IntentUtils.sanitizeIntent(good);
            setResult(RESULT_OK, good);
            Toast.makeText(this, "setResult sent: " + good, Toast.LENGTH_SHORT).show();
            finish();
        });

        binding.basicThreeSafe.setOnClickListener(v1 -> {
            Intent good = getIntent();
            Toast.makeText(this, "Activity destroyed!", Toast.LENGTH_SHORT).show();
            finish();
            setResult(RESULT_OK, good);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}