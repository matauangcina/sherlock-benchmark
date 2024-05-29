package sherlock.insecure.unsafe_intent_uri;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sherlock.insecure.databinding.ActivityTargetBinding;

public class DestActivity extends AppCompatActivity {

    private static final String TAG = "sherlock.test.unsafe_intent_uri.DestActivity";
    private ActivityTargetBinding binding;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, DestActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTargetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textview.setText(TAG);

        Toast.makeText(this, "Receiving and processing intent: " + TAG, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
