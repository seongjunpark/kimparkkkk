package www.funsumer.net;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String TAG = "GCM";
	private static final String SENDER_ID = "147463031498";

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
			showMessage(context, intent);

		}
	}

	@Override
	protected void onError(Context context, String msg) {
		// TODO Auto-generated method stub
		Log.w(TAG, "onError!! " + msg);
	}

	@Override
	protected void onRegistered(Context context, String regID) {
		// TODO Auto-generated method stub
		if (!regID.equals("") || regID != null) {

		}
	}

	@Override
	protected void onUnregistered(Context context, String regID) {
		// TODO Auto-generated method stub
		Log.w(TAG, "onUnregistered!! " + regID);
	}

	public void showToast() {
	}

	private void showMessage(Context context, Intent intent) {
		String title = intent.getStringExtra("title");
		String msg = intent.getStringExtra("msg");
		String ticker = intent.getStringExtra("ticker");

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Activity.NOTIFICATION_SERVICE);

		// 占쎈���占쎈똾逾놅옙占쏙옙�쎈뻬占쎌꼶��占쎈�源쏙옙紐껓옙 占쎌꼵�э옙�좎뱽 占쏙옙占쎄쑬��雅뚯눘苑랃옙占쏙옙占쎈선雅뚯눘苑�옙占�		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, MainActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
		// PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
		// new Intent(), 0);

		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = ticker;
		notification.when = System.currentTimeMillis();
		notification.vibrate = new long[] { 500, 100, 500, 100 };
		notification.sound = Uri
				.parse("/system/media/audio/notifications/20_Cloud.ogg");

		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, title, msg, pendingIntent);

		notificationManager.notify(0, notification);
	}

}
