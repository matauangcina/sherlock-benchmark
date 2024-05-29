package sherlock.insecure.protected_components;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import sherlock.insecure.databinding.ActivityProtectedBinding;

public class ProtectedActivity extends AppCompatActivity {

    private ActivityProtectedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProtectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
