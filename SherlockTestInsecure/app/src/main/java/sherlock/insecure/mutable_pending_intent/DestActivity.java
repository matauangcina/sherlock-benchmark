package sherlock.insecure.mutable_pending_intent;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sherlock.insecure.databinding.ActivityTargetBinding;

public class DestActivity extends AppCompatActivity {

    private static final String TAG = "sherlock.test.mutable_pending_intent.DestActivity";
    private ActivityTargetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTargetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textview.setText(TAG);

        Toast.makeText(this, "Receiving and processing PendingIntent..", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
