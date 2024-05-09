package sherlock.test.mutable_pending_intent;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class DestActivity extends AppCompatActivity {

    private static final String TAG = "DestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Receiving and processing PendingIntent..");
    }
}
