package sherlock.test.insecure_set_result;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import sherlock.test.databinding.ActivityImplicitBinding;

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

    private static Intent sanitizeIntent(Intent intent) {
        intent.removeFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
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

        binding.implicitOneSafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INSECURE_SETRESULT_ACTION");
            startActivityForResult(i, REQUEST_CODE_SAFE_ONE);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitTwoSafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INSECURE_SETRESULT_ACTION");
            startActivityForResult(i, REQUEST_CODE_SAFE_TWO);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitThreeSafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INSECURE_SETRESULT_ACTION");
            startForResultThreeSafe.launch(i);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitFourSafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INSECURE_SETRESULT_ACTION");
            startForResultFourSafe.launch(i);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitFiveSafe.setOnClickListener(v1 -> {
            Toast.makeText(
                    this,
                    "Launch sherlock.test.insecure_set_result.DeeplinkActivity with: sherlock://insecure.set.result.deeplink/safe",
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
            } else if (requestCode == REQUEST_CODE_SAFE_ONE) {
                data.removeFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                setResult(RESULT_OK, data);
            } else if (requestCode == REQUEST_CODE_SAFE_TWO) {
                Intent good = createSanitizedIntent(data);
                setResult(RESULT_OK, good);
            }
            finish();
        }
    }

    ActivityResultLauncher<Intent> startForResultThreeUnsafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    setResult(RESULT_OK, result.getData());
                    finish();
                }
            }
    );

    ActivityResultLauncher<Intent> startForResultFourUnsafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent bad = new Intent(result.getData());
                    setResult(RESULT_OK, bad);
                    finish();
                }
            }
    );

    ActivityResultLauncher<Intent> startForResultThreeSafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent good = result.getData();
                    good.removeFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
                            | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    setResult(RESULT_OK, good);
                    finish();
                }
            }
    );

    ActivityResultLauncher<Intent> startForResultFourSafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent good = sanitizeIntent(result.getData());
                    setResult(RESULT_OK, good);
                    finish();
                }
            }
    );
}
