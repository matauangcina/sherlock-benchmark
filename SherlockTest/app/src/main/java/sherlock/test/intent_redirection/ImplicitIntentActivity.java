package sherlock.test.intent_redirection;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import sherlock.test.databinding.ActivityImplicitBinding;
import sherlock.test.utils.IntentUtils;

@SuppressLint("UnsafeIntentLaunch")

public class ImplicitIntentActivity extends AppCompatActivity {

    private static final String EXTRA_TITLE = "sherlock.test.access_to_protected_components.intent_redirection.title";
    private static final String REDIRECT_INTENT = "extra_intent";
    private static final int REQUEST_CODE_ONE_UNSAFE = 1001;
    private static final int REQUEST_CODE_TWO_UNSAFE = 1002;
    private static final int REQUEST_CODE_ONE_SAFE = 1003;
    private static final int REQUEST_CODE_TWO_SAFE = 1004;

    private ActivityImplicitBinding binding;

    public static Intent newIntent(Context packageContext, String title) {
        Intent i = new Intent(packageContext, ImplicitIntentActivity.class);
        i.putExtra(EXTRA_TITLE, title);
        return i;
    }

    private void unsafeRedirect(Intent intent) {
        Intent i = intent.getParcelableExtra(REDIRECT_INTENT);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImplicitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        binding.title.setText(title);

        binding.implicitOneUnsafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INTENT_REDIRECT_ACTION");
            startActivityForResult(i, REQUEST_CODE_ONE_UNSAFE);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitTwoUnsafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INTENT_REDIRECT_ACTION");
            startActivityForResult(i, REQUEST_CODE_TWO_UNSAFE);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitThreeUnsafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INTENT_REDIRECT_ACTION");
            startForResultThreeUnsafe.launch(i);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitFourUnsafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INTENT_REDIRECT_ACTION");
            startForResultFourUnsafe.launch(i);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitFiveUnsafe.setOnClickListener(v1 -> {
            Toast.makeText(
                    this,
                    "Launch sherlock.test.intent_redirection.DeeplinkActivity with: sherlock://intent.redirection.deeplink/unsafe",
                    Toast.LENGTH_LONG
            ).show();
        });

        binding.implicitOneSafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INTENT_REDIRECT_ACTION");
            startActivityForResult(i, REQUEST_CODE_ONE_SAFE);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitTwoSafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INTENT_REDIRECT_ACTION");
            startActivityForResult(i, REQUEST_CODE_TWO_SAFE);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitThreeSafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INTENT_REDIRECT_ACTION");
            startForResultThreeSafe.launch(i);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitFourSafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.INTENT_REDIRECT_ACTION");
            startForResultFourSafe.launch(i);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitFiveSafe.setOnClickListener(v1 -> {
            Toast.makeText(
                    this,
                    "Launch sherlock.test.intent_redirection.DeeplinkActivity with: sherlock://intent.redirection.deeplink/safe",
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
            if (requestCode == REQUEST_CODE_ONE_UNSAFE) {
                unsafeRedirect(data);
            } else if (requestCode == REQUEST_CODE_TWO_UNSAFE) {
                Intent bad = (Intent) data.getExtras().get(REDIRECT_INTENT);
                startActivity(bad);
            } else if (requestCode == REQUEST_CODE_ONE_SAFE) {
                Intent good = data.getParcelableExtra(REDIRECT_INTENT);
                ComponentName origin = getCallingActivity();
                if (origin != null
                        && origin.getPackageName().equals("sherlock.test")
                        && origin.getClassName().equals("sherlock.test.intent_redirection.AllowedOriginActivity")) {
                    startActivity(good);
                }
                Toast.makeText(this, "Attempting to start an activity.", Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_CODE_TWO_SAFE) {
                Intent parcel = data.getParcelableExtra(REDIRECT_INTENT);
                Intent good = new Intent(this, AllowedDestActivity.class);
                good.putExtra("parcel", parcel);
                startActivity(good);
                Toast.makeText(this, "Attempting to start an activity.", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    ActivityResultLauncher<Intent> startForResultThreeUnsafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent bad = new Intent((Intent) result.getData().getParcelableExtra(REDIRECT_INTENT));
                    startActivity(bad);
                }
            }
    );

    ActivityResultLauncher<Intent> startForResultFourUnsafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent i = result.getData();
                    Bundle bundle = i.getBundleExtra("bundle");
                    Intent bad = bundle.getParcelable(REDIRECT_INTENT);
                    startActivity(bad);
                }
            }
    );

    ActivityResultLauncher<Intent> startForResultThreeSafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bundle bundle = result.getData().getBundleExtra("bundle");
                    Intent good = IntentUtils.getSafeBundleParcel(this, bundle);
                    if (good != null) {
                        startActivity(good);
                    }
                    Toast.makeText(this, "Attempting to start an activity.", Toast.LENGTH_SHORT).show();
                }
            }
    );

    ActivityResultLauncher<Intent> startForResultFourSafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent good = result.getData().getParcelableExtra(REDIRECT_INTENT);
                    List<ResolveInfo> resolveDestInfo = getPackageManager().queryIntentActivities(good, 0);
                    for (ResolveInfo info : resolveDestInfo) {
                        if (info.activityInfo.packageName.equals("sherlock.test")) {
                            if (info.activityInfo.name.equals("sherlock.test.intent_redirection.AllowedDestActivity")) {
                                startActivity(good);
                            }
                        }
                    }
                    Toast.makeText(this, "Attempting to start an activity.", Toast.LENGTH_SHORT).show();
                }
            }
    );
}
