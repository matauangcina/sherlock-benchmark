package sherlock.test.intent_redirection;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class AllowedDestActivity extends AppCompatActivity {

    private static final String TAG = "sherlock.test.intent_redirection.AllowedDestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Processing intent in: " + TAG);
    }
}
