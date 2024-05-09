package sherlock.test.insecure_set_result;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class DeeplinkActivity extends AppCompatActivity {

    private static final String SCHEME = "sherlock";
    private static final String HOST = "insecure.set.result.deeplink";
    private static final String EXTRA_PARCEL = "extra_parcel";

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, DeeplinkActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent deeplink = getIntent();
        Uri deeplinkUri = deeplink.getData();
        if (Objects.equals(deeplink.getAction(), Intent.ACTION_VIEW) && deeplinkUri != null) {
            if (SCHEME.equals(deeplinkUri.getScheme())
                    && HOST.equals(deeplinkUri.getHost())) {
                Intent i = new Intent("sherlock.test.INSECURE_SETRESULT_ACTION");
                if (getPackageManager().resolveActivity(i, 0) != null) {
                    if ("/unsafe".equals(deeplinkUri.getPath())) {
                        startForResultUnsafe.launch(i);
                    } else if ("/safe".equals(deeplinkUri.getPath())) {
                        startForResultSafe.launch(i);
                    }
                }
            }
        }
    }

    ActivityResultLauncher<Intent> startForResultUnsafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent bad = result.getData();
                    setResult(RESULT_OK, bad);
                    finish();
                }
            }
    );

    ActivityResultLauncher<Intent> startForResultSafe = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent good = new Intent();
                    good.putExtra(EXTRA_PARCEL, result.getData());
                    setResult(RESULT_OK, good);
                    finish();
                }
            }
    );
}
