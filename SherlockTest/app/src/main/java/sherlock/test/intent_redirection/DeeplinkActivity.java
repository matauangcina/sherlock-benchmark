package sherlock.test.intent_redirection;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import sherlock.test.utils.IntentUtils;

public class DeeplinkActivity extends AppCompatActivity {

    private static final String SCHEME = "sherlock";
    private static final String HOST = "intent.redirection.deeplink";
    private static final String REDIRECT_INTENT = "extra_intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent deeplink = getIntent();
        Uri deeplinkUri = deeplink.getData();
        if (Objects.equals(deeplink.getAction(), Intent.ACTION_VIEW) && deeplinkUri != null) {
            if (SCHEME.equals(deeplinkUri.getScheme())
                    && HOST.equals(deeplinkUri.getHost())) {
                if ("/unsafe".equals(deeplinkUri.getPath())) {
                    Intent i = new Intent("sherlock.test.INTENT_REDIRECT_ACTION");
                    startForResultUnsafe.launch(i);
                    Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
                } else if ("/safe".equals(deeplinkUri.getPath())) {
                    Intent i = new Intent("sherlock.test.INTENT_REDIRECT_ACTION");
                    startForResultSafe.launch(i);
                    Toast.makeText(this, "Implicit intent: " + i + " launched!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    ActivityResultLauncher<Intent> startForResultUnsafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bundle bundle = result.getData().getExtras();
                    Intent good = bundle.getParcelable(REDIRECT_INTENT);
                    for (ResolveInfo info : getPackageManager().queryIntentActivities(good, 0)) {
                        good.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                    }
                    startActivity(good);
                }
            }
    );

    ActivityResultLauncher<Intent> startForResultSafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bundle bundle = result.getData().getExtras();
                    Intent good = bundle.getParcelable(REDIRECT_INTENT);
                    ComponentName origin = getCallingActivity();
                    if (origin.getClassName().equals("sherlock.test.intent_redirection.AllowedOriginActivity") && origin.getPackageName().equals("sherlock.test")) {
                        startActivity(good);
                    }
                    Toast.makeText(this, "Attempting to start an activity!", Toast.LENGTH_SHORT).show();
                }
            }
    );
}