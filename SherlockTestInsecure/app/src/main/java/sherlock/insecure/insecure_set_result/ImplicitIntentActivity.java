package sherlock.insecure.insecure_set_result;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import sherlock.insecure.databinding.ActivityImplicitBinding;

public class ImplicitIntentActivity extends AppCompatActivity {

    private static final String EXTRA_TITLE = "sherlock.test.access_to_protected_components.insecure_set_result.title";
    private static final int REQUEST_CODE_UNSAFE_ONE = 1001;
    private static final int REQUEST_CODE_UNSAFE_TWO = 1002;
    private static final int REQUEST_CODE_SAFE_ONE = 1003;
    private static final int REQUEST_CODE_SAFE_TWO = 1004;
    private ActivityImplicitBinding binding;

    public static Intent newIntent(Context packageContext, String title) {
        Intent i = new Intent(packageContext, ImplicitIntentActivity.class);
        i.putExtra(EXTRA_TITLE, title);
        return i;
    }

    private static Intent createSanitizedIntent(Intent intent) {
        Intent i = new Intent(intent);
        i.removeFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImplicitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        binding.title.setText(title);

        binding.implicitOneUnsafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INSECURE_SETRESULT_ACTION");
            startActivityForResult(i, REQUEST_CODE_UNSAFE_ONE);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitTwoUnsafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INSECURE_SETRESULT_ACTION");
            startActivityForResult(i, REQUEST_CODE_UNSAFE_TWO);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitThreeUnsafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INSECURE_SETRESULT_ACTION");
            startForResultThreeUnsafe.launch(i);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitFourUnsafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INSECURE_SETRESULT_ACTION");
            startForResultFourUnsafe.launch(i);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitFiveUnsafe.setOnClickListener(v1 -> {
            Toast.makeText(
                    this,
                    "Launch sherlock.test.insecure_set_result.DeeplinkActivity with: sherlock://insecure.set.result.deeplink/unsafe",
                    Toast.LENGTH_LONG
            ).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @SuppressLint("UnsafeIntentLaunch")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_UNSAFE_ONE) {
                Intent bad = new Intent(data);
                setResult(RESULT_OK, bad);
            } else if (requestCode == REQUEST_CODE_UNSAFE_TWO) {
                setResult(RESULT_OK, data);
            }
            finish();
        }
    }

    ActivityResultLauncher<Intent> startForResultThreeUnsafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent bad = result.getData();
                    Intent filtered = bad.cloneFilter();
                    filtered.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    setResult(RESULT_OK, filtered);
                    finish();
                }
            }
    );

    ActivityResultLauncher<Intent> startForResultFourUnsafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent bad = (Intent) result.getData().clone();
                    setResult(RESULT_OK, bad);
                    finish();
                }
            }
    );
}
