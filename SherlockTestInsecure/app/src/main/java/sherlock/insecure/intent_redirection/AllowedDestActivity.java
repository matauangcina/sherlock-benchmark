package sherlock.insecure.intent_redirection;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sherlock.insecure.databinding.ActivityTargetBinding;

public class AllowedDestActivity extends AppCompatActivity {

    private static final String TAG = "sherlock.test.intent_redirection.AllowedDestActivity";
    private ActivityTargetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTargetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textview.setText(TAG);

        Toast.makeText(this, "Processing intent in: " + TAG, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
