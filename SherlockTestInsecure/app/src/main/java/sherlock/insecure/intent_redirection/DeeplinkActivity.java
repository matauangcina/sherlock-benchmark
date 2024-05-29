package sherlock.insecure.intent_redirection;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;


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
                }
            }
        }
    }

    ActivityResultLauncher<Intent> startForResultUnsafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bundle bundle = result.getData().getExtras();
                    Intent bad = bundle.getParcelable(REDIRECT_INTENT);
                    for (ResolveInfo info : getPackageManager().queryIntentActivities(bad, 0)) {
                        bad.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                    }
                    startActivity(bad);
                }
            }
    );
}