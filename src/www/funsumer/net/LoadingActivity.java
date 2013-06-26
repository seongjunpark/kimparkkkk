package www.funsumer.net;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadingActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loading_ui);

		// network --3g or wifi checking
		ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (networkInfo != null)
			mFinishLoadingHandler.sendEmptyMessageDelayed(0, 3000);
		else
			DialogAlert(this.getResources().getString(
					R.string.dialogalert_no_network));

		Animation translateLeftAnim = AnimationUtils.loadAnimation(this,
				R.anim.rotate);
		
		ImageView candysumer = (ImageView) findViewById(R.id.candysumer);
		candysumer.startAnimation(translateLeftAnim);
	}

	Handler mFinishLoadingHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			setResult(RESULT_OK, getIntent());
			finish();
		}

	};
	//dialog(like toast)
	private AlertDialog.Builder d = null;

	private void DialogAlert(String message) {
		d = new AlertDialog.Builder(isChild() ? getParent()
				: LoadingActivity.this);

		d.setTitle(R.string.dialogalert_info);
		d.setMessage(message);

		d.setPositiveButton(R.string.dialogbtn_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						setResult(RESULT_CANCELED, getIntent());
						finish();
					}
				});
		DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				setResult(RESULT_CANCELED, getIntent());
				finish();
			}
		};
		d.setOnCancelListener(cancelListener);
		d.show();
	}
}