package sherlock.test.unsafe_intent_uri.component;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URISyntaxException;

import sherlock.test.databinding.ActivityUnsafeIntentBasicBinding;
import sherlock.test.unsafe_intent_uri.DestActivity;
import sherlock.test.utils.IntentUtils;

@SuppressLint("UnsafeIntentLaunch")

public class BasicActivity extends AppCompatActivity {

    private static final String EXTRA_TITLE = "sherlock.test.unsafe_intent_uri.basic.title";
    private static final String EXTRA_URL = "url";
    private static final int REQUEST_CODE_UNSAFE = 1001;
    private static final int REQUEST_CODE_SAFE = 1002;
    private ActivityUnsafeIntentBasicBinding binding;

    public static Intent newIntent(Context packageContext, String title) {
        Intent i = new Intent(packageContext, BasicActivity.class);
        i.putExtra(EXTRA_TITLE, title);
        return i;
    }

    private void unsafeActivityLaunch(Intent intent) throws URISyntaxException {
        startActivity(Intent.parseUri(intent.getStringExtra("url"), Intent.URI_INTENT_SCHEME));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUnsafeIntentBasicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.title.setText(getIntent().getStringExtra(EXTRA_TITLE));

        binding.basicOneUnsafe.setOnClickListener(v1 -> {
            try {
                Intent bad = Intent.parseUri(getIntent().getStringExtra(EXTRA_URL), Intent.URI_INTENT_SCHEME);
                bad.setComponent(null);
                startActivity(bad);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        binding.basicTwoUnsafe.setOnClickListener(v1 -> {
            try {
                Intent bad = getIntent();
                Bundle bundle = bad.getBundleExtra("bundle");
                String bad1 = bundle.getString(EXTRA_URL);
                startActivity(Intent.parseUri(bad1, Intent.URI_INTENT_SCHEME | Intent.URI_ALLOW_UNSAFE));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        binding.implicitOneUnsafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.UNSAFE_INTENT_URI_BASIC");
            startActivityForResult(i, REQUEST_CODE_UNSAFE);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitTwoUnsafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.UNSAFE_INTENT_URI_BASIC");
            startForResultTwoUnsafe.launch(i);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.basicOneSafe.setOnClickListener(v1 -> {
            try {
                Intent good = Intent.parseUri(getIntent().getDataString(), Intent.URI_INTENT_SCHEME);
                ComponentName origin = getCallingActivity();
                if (origin != null
                        && origin.getPackageName().equals("sherlock.test")
                        && origin.getClassName().equals("sherlock.test.intent_redirection.AllowedOriginActivity")){
                    startActivity(good);
                }
                Toast.makeText(this, "Attempting to launch: " + good, Toast.LENGTH_SHORT).show();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        binding.basicTwoSafe.setOnClickListener(v1 -> {
            try {
                Intent intent = getIntent();
                Bundle bundle = intent.getBundleExtra("bundle");
                String intentUri = bundle.getString(EXTRA_URL);
                Intent good = Intent.parseUri(intentUri, Intent.URI_INTENT_SCHEME);
                IntentUtils.sanitizeAndLaunchIntent(this, good);
                Toast.makeText(this, "Attempting to launch: " + good, Toast.LENGTH_SHORT).show();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        binding.implicitOneSafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.UNSAFE_INTENT_URI_BASIC");
            startActivityForResult(i, REQUEST_CODE_SAFE);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });

        binding.implicitTwoSafe.setOnClickListener(v1 -> {
            Intent i = new Intent("sherlock.test.UNSAFE_INTENT_URI_BASIC");
            startForResultTwoSafe.launch(i);
            Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_UNSAFE) {
                try {
                    startActivity(Intent.parseUri(data.getStringExtra(EXTRA_URL), Intent.URI_INTENT_SCHEME));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            } else if (requestCode == REQUEST_CODE_SAFE) {
                try {
                    Intent good = Intent.parseUri(data.getStringExtra(EXTRA_URL), Intent.URI_INTENT_SCHEME);
                    ComponentName dest = good.resolveActivity(getPackageManager());
                    if (dest.getPackageName().equals("sherlock.test")) {
                        if (dest.getClassName().equals("sherlock.test.unsafe_intent_uri.DestActivity")) {
                            startActivity(good);
                        }
                    }
                    Toast.makeText(this, "Attempting to load: " + good, Toast.LENGTH_SHORT).show();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    ActivityResultLauncher<Intent> startForResultTwoUnsafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    try {
                        unsafeActivityLaunch(result.getData());
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
    );

    ActivityResultLauncher<Intent> startForResultTwoSafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String good = (String) result.getData().getExtras().get(EXTRA_URL);
                    try {
                        Intent good1 = Intent.parseUri(good, Intent.URI_INTENT_SCHEME);
                        good1.setClassName(getPackageName(), "sherlock.test.unsafe_intent_uri.DestActivity");
                        startActivity(good1);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
    );

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
