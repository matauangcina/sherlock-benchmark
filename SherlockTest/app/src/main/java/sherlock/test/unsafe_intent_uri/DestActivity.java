package sherlock.test.unsafe_intent_uri;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class DestActivity extends AppCompatActivity {

    private static final String TAG = "sherlock.test.unsafe_intent_uri.DestActivity";

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, DestActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Receiving and processing intent: " + TAG);
    }
}
