package sherlock.test.mutable_pending_intent;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import sherlock.test.databinding.ActivityNotificationBinding;

public class NotificationActivity extends AppCompatActivity {

    private static final String EXTRA_TITLE = "sherlock.test.mutable_pending_intent.title";
    public static final int NOTIF_CODE = 999;
    private static final int REQUEST_CODE = 1001;
    private static final String CHANNEL_ID = "id";
    private ActivityNotificationBinding binding;
    private NotificationCompat.Builder mNotificationBuilder;

    public static Intent newIntent(Context packageContext, String title) {
        Intent i = new Intent(packageContext, NotificationActivity.class);
        i.putExtra(EXTRA_TITLE, title);
        return i;
    }

    private void createNotificationChannel() {
        CharSequence name = "channel_name";
        String description = "channel_description";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager nm = getSystemService(NotificationManager.class);
        nm.createNotificationChannel(channel);
    }

    private Notification buildNotification(PendingIntent pi) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("BAD!!")
                .setContentText("Notification with mutable pending intent and implicit base intent.")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNotificationChannel();

        IntentFilter filter = new IntentFilter("sherlock.test.NOTIFICATION");
        registerReceiver(new NotificationReceiver(), filter, RECEIVER_NOT_EXPORTED);

        binding.title.setText(getIntent().getStringExtra(EXTRA_TITLE));

        mNotificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        binding.notificationOneUnsafe.setOnClickListener(v1 -> {
            Intent emptyBase = new Intent();
            PendingIntent bad = PendingIntent.getActivity(this,
                    REQUEST_CODE, emptyBase, PendingIntent.FLAG_MUTABLE);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle("BAD!!")
                    .setContentText("Notification with mutable pending intent and implicit base intent.")
                    .setContentIntent(bad)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                nmc.notify(NOTIF_CODE, notification.build());
            }
            Toast.makeText(this, "Notification sent: " + nmc, Toast.LENGTH_SHORT).show();
        });

        binding.notificationTwoUnsafe.setOnClickListener(v1 -> {
            mNotificationBuilder
                    .setContentTitle("BAD!!")
                    .setContentText("Notification initiated with mutable pending intent and implicit base intent.")
                    .setContentIntent(
                            PendingIntent.getActivity(
                                    this,
                                    REQUEST_CODE,
                                    new Intent("sherlock.test.NOTIFICATION"),
                                    PendingIntent.FLAG_MUTABLE
                            )
                    );
            NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                nmc.notify(NOTIF_CODE, mNotificationBuilder.build());
            }
            Toast.makeText(this, "Notification sent: " + nmc, Toast.LENGTH_SHORT).show();
        });

        binding.notificationThreeUnsafe.setOnClickListener(v1 -> {
            PendingIntent bad = PendingIntent.getActivity(this, REQUEST_CODE, new Intent(), PendingIntent.FLAG_MUTABLE);
            Notification notification = buildNotification(bad);
            Intent notifBroadcast = new Intent("sherlock.test.NOTIFICATION");
            notifBroadcast.putExtra("notification", notification);
            sendBroadcast(notifBroadcast);
            Toast.makeText(this, "Notification sent", Toast.LENGTH_SHORT).show();
        });

        binding.notificationOneSafe.setOnClickListener(v1 -> {
            Intent explicitBase = new Intent();
            explicitBase.setClassName("sherlock.test", "sherlock.test.mutable_pending_intent.AllowedActivity");
            PendingIntent good = PendingIntent.getActivity(this,
                    REQUEST_CODE, explicitBase, PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle("GOOD..")
                    .setContentText("Notification with immutable pending intent and explicit base intent.")
                    .setContentIntent(good)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                nmc.notify(NOTIF_CODE, notification.build());
            }

            Toast.makeText(this, "Notification sent: " + nmc, Toast.LENGTH_SHORT).show();
        });

        binding.notificationTwoSafe.setOnClickListener(v1 -> {
            PendingIntent good = PendingIntent.getActivity(this, REQUEST_CODE, new Intent(), PendingIntent.FLAG_IMMUTABLE);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle("SOSO..")
                    .setContentText("Notification with immutable pending intent and explicit base intent.")
                    .setContentIntent(good)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            Intent notifBroadcast = new Intent("sherlock.test.NOTIFICATION");
            notifBroadcast.setClassName(getPackageName(), "sherlock.test.mutable_pending_intent.NotificationReceiver");
            notifBroadcast.putExtra("notification", notification);
            sendBroadcast(notifBroadcast);
        });

        binding.notificationThreeSafe.setOnClickListener(v1 -> {
            Intent implicitBase = new Intent();
            implicitBase.setAction("sherlock.test.MUTABLE_PENDING_INTENT");
            PendingIntent good = PendingIntent.getActivity(this, REQUEST_CODE, implicitBase, PendingIntent.FLAG_IMMUTABLE);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle("SOSO..")
                    .setContentText("Notification with immutable pending intent and explicit base intent.")
                    .setContentIntent(good)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            Intent notifBroadcast = new Intent("sherlock.test.NOTIFICATION");
            notifBroadcast.putExtra("notification", notification);
            sendBroadcast(notifBroadcast);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
