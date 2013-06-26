package www.funsumer.net;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends Activity {

	static JSONArray guiAPI = null;
	static String ResultinOPT2 = null;
	static String Result_WidePic = null;
	String Result_ProfilePic = null;
	static String Result_Name = null;
	static String Result_Fnum = null;
	static String Result_Vnum = null;
	static String Result_Vote = null;

	public ImageLoader imageLoader;

	private static String selectedImagePath;
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int CROP_FROM_GALLERY = 3;
	private Uri mImageCaptureUri;
	private AlertDialog mDialog;

	private String full_path;

	private URL connectUrl = null;

	private Bitmap bitmap;
	private ImageView imgView;
	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";

	private FileInputStream mFileInputStream = null;

	DataOutputStream dos;
	// private String mynoteid = MainActivity.mynoteid;
	private String mynoteid;
	String userid;
	private String position, toid, partyid, goid;
	public static String tabprofileid;

	ImageView profilepic;

	ImageLoader imageloader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_base);

		Intent intentget = getIntent();
		userid = intentget.getStringExtra("userid");
		mynoteid = intentget.getStringExtra("mynoteid");

		Log.e("abcdd", "userid " + userid + "mynoteid " + mynoteid);

		// userid = NoteActivity.userid;

		// header02 url
		String noteheaderurl = "http://funsumer.net/json/?opt=2&mynoteid="
				+ mynoteid + "&userid=" + userid;

		JSONParser jParser = new JSONParser();

		try {
			JSONObject json = jParser.getJSONFromUrl(noteheaderurl);
			guiAPI = json.getJSONArray("guiAPI");
			JSONObject wide = guiAPI.getJSONObject(0);
			ResultinOPT2 = wide.getString("Result");

			Result_WidePic = "http://www.funsumer.net/"
					+ wide.getString("Result_WidePic");
			Result_ProfilePic = "http://www.funsumer.net/"
					+ wide.getString("Result_ProfilePic");

			Result_Name = wide.getString("Result_Name");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		Log.e("abcdd", "Result_ProfilePic     " + Result_ProfilePic);
		Log.e("abcdd", "Result_WidePic    " + Result_WidePic);
		Log.e("abcdd", "tabprofileid" + tabprofileid);

		profilepic = (ImageView) findViewById(R.id.profilepic);
		if (tabprofileid == "1") {
			try {
				Log.e("abc", "case 1" + Result_WidePic);
				profilepic.setImageBitmap((Bitmap) getURLImage(new URL(
						Result_ProfilePic)));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (tabprofileid == "2") {
//			imageloader.DisplayImage(Result_WidePic, profilepic);
			 try {
			 Log.e("abc", "222" + Result_WidePic);
			 profilepic.setImageBitmap((Bitmap) getURLImage(new URL(
			 Result_WidePic)));
			 Log.e("abc", "333" + tabprofileid);
			 } catch (MalformedURLException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
			 }
		}

		LinearLayout profile_bottom1 = (LinearLayout) findViewById(R.id.profile_bottom1);
		if (mynoteid.equals(userid)) {
			profile_bottom1.setVisibility(View.VISIBLE);
		} else {
			profile_bottom1.setVisibility(View.INVISIBLE);
		}

		LinearLayout profile_bottom2 = (LinearLayout) findViewById(R.id.profile_bottom2);
		if (mynoteid.equals(userid)) {
			profile_bottom2.setVisibility(View.VISIBLE);
		} else {
			profile_bottom2.setVisibility(View.INVISIBLE);
		}

		Button btn_profile = (Button) findViewById(R.id.btn_profile);
		btn_profile.setOnClickListener(profileClick);
		if (mynoteid.equals(userid)) {
			btn_profile.setVisibility(View.VISIBLE);
		} else {
			btn_profile.setVisibility(View.INVISIBLE);
		}

		Button change_profile = (Button) findViewById(R.id.change_profile);
		if (tabprofileid == "1") {
			change_profile.setText("프로필사진 업데이트");
		} else if (tabprofileid == "2") {
			change_profile.setText("노트사진 업데이트");
		}
		change_profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					String mFilePath = selectedImagePath;
					if (tabprofileid == "1") {
						DoFileUpload(mFilePath, "1");
					} else if (tabprofileid == "2") {
						DoFileUpload(mFilePath, "2");
					}
					Intent intent = new Intent(getApplicationContext(),
							NoteActivity.class);
					startActivity(intent);
					ProfileActivity.this.finish();
				} catch (Exception e) {

				}

			}
		});

	}

	//
	final OnClickListener profileClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_profile:
				mDialog = createDialog();
				mDialog.show();
				break;
			}
		}
	};

	private AlertDialog createDialog() {
		final View innerView = getLayoutInflater().inflate(
				R.layout.image_crop_row, null);

		Button camera = (Button) innerView.findViewById(R.id.btn_camera_crop);
		Button gellary = (Button) innerView.findViewById(R.id.btn_gellary_crop);

		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doTakePhotoAction();
				setDismiss(mDialog);
			}
		});

		gellary.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doTakeAlbumAction();
				setDismiss(mDialog);
			}
		});

		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("프로필사진 수정하기");
		ab.setView(innerView);

		return ab.create();
	}

	private void setDismiss(AlertDialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	private void doTakePhotoAction() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// making Crop image save path
		mImageCaptureUri = createSaveCropFile();
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				mImageCaptureUri);
		startActivityForResult(intent, PICK_FROM_CAMERA);
	}

	String mFileName;
	private static final String BACKGROUNDIMAGE_FILE = "_funsumer.jpg";

	private void doTakeAlbumAction() {
		// �좎뜦維�떋占썲뜝�덉굣占쏙옙
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, PICK_FROM_ALBUM);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {

		case PICK_FROM_ALBUM: {
			mImageCaptureUri = data.getData();
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(mImageCaptureUri, "image/*");
			try {

				Uri selectedImageUri = data.getData();

				selectedImagePath = getPath(selectedImageUri);
				intent.putExtra("outputX", 500);
				intent.putExtra("outputY", 500);
				intent.putExtra("aspectX", 500);
				intent.putExtra("aspectY", 500);
				intent.putExtra("scale", true);
				intent.putExtra("output", mImageCaptureUri);
				intent.putExtra("return-data", false);
				startActivityForResult(intent, CROP_FROM_GALLERY);

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Internal error",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
			break;
		}

		case PICK_FROM_CAMERA: {

			// mImageCaptureUri = data.getData();
			// full_path = mImageCaptureUri.getPath();
			// �좎럥占썲뜝�숈��좎룞���띠룊�숃��쏆삕�좎룞�숋옙袁⑸쐩�좎룞�숋옙洹먮봽�℡뜝�덈샍藥꿸낀�쇿뜝�숈삕占쏙옙�숂춯�묒삕�좎떬�댿뵛占쎌뮋�숅뇦猿됲�占쎌닂�숋옙紐껊퉵�좎룞��
			// �좎럥�억옙臾뺤삕�좎룞�숋옙占쎌삕嶺뚯쉻�쇿뜝�쇰맍���좎럥�얗��븍뎨占썲뜝�숈삕占쎄낮占썲뜝�숈삕�좎럥梨뤄옙�놁삕占쎈씮���좎뜦維뽳옙�먯삕�좑옙

			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(mImageCaptureUri, "image/*");
			mFileName = String.valueOf(System.currentTimeMillis())
					+ BACKGROUNDIMAGE_FILE;
			Uri u = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory() + "/funsumer", mFileName));

			// Crop�좎룞�쇿뜝�덌옙�좎룞彛ゅ뜝�숈삕 �좎룞�숋옙節륁삕�좏룴ath
			intent.putExtra("outputX", 500);
			intent.putExtra("outputY", 500);
			intent.putExtra("aspectX", 500);
			intent.putExtra("aspectY", 500);
			intent.putExtra("scale", true);
			intent.putExtra("output", mImageCaptureUri);
			intent.putExtra("return-data", false);
			startActivityForResult(intent, CROP_FROM_CAMERA);

			break;
		}
		case CROP_FROM_CAMERA: {
			full_path = mImageCaptureUri.getPath();

			Bitmap photo;
			BitmapFactory.Options options = new BitmapFactory.Options();
			photo = BitmapFactory.decodeFile(full_path, options);
			profilepic.setImageBitmap(photo);

			selectedImagePath = full_path;
			break;

		}
		case CROP_FROM_GALLERY: {

			final Bundle extras = data.getExtras();
			if (extras != null) {
				// Bitmap photo1 = extras.getParcelable("data");
				BitmapFactory.Options options = new BitmapFactory.Options();
				Bitmap photo1 = BitmapFactory.decodeFile(selectedImagePath,
						options);
				profilepic.setImageBitmap(photo1);

				File f = new File(mImageCaptureUri.getPath());
				if (f.exists()) {
					f.delete();
				}

				break;
			}

		}

		}
	}

	private Uri createSaveCropFile() {
		Uri uri;
		String url = "tmp_" + String.valueOf(System.currentTimeMillis())
				+ ".jpg";
		uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
				url));
		return uri;
	}

	private void DoFileUpload(String filePath, String position)
			throws IOException {
		Log.d("Test", "file path = " + filePath);
		HttpFileUpload("http://funsumer.net/json/", filePath, position);

	}

	// void>>>>>Returncode
	private void HttpFileUpload(String urlString, String fileName,
			String position) {
		try {

			mFileInputStream = new FileInputStream(fileName);
			connectUrl = new URL(urlString);
			Log.d("Test", "mFileInputStream  is " + mFileInputStream);

			HttpURLConnection conn = (HttpURLConnection) connectUrl
					.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			conn.connect();

			// write data
			dos = new DataOutputStream(conn.getOutputStream());
			writeFormField("oopt", "20");
			writeFormField("position", position);
			writeFormField("mynoteid", mynoteid);

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
					+ fileName + "\"" + lineEnd);

			dos.writeBytes(lineEnd);

			int bytesAvailable = mFileInputStream.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);

			byte[] buffer = new byte[bufferSize];
			int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

			Log.d("Test", "image byte is " + bytesRead);

			// read image
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = mFileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
			}

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// close streams
			Log.e("Test", "File is written");
			mFileInputStream.close();
			dos.flush(); // finish upload...

			// get response
			int ch;
			InputStream is = conn.getInputStream();
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			String s = b.toString();
			Log.e("Test", "result = " + s);
			dos.close();
			selectedImagePath = null;

		} catch (Exception e) {
			Log.d("Test", "exception " + e.getMessage());
			// TODO: handle exception
		}

	}

	private void writeFormField(String fieldName, String fieldValue) {
		// TODO Auto-generated method stub
		try {

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\""
					+ fieldName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.write(fieldValue.getBytes("utf-8"));
			dos.writeBytes(lineEnd);

		} catch (Exception e) {
			System.out.println("GeoPictureUploader.writeFormField: got: "
					+ e.getMessage());
		}

	}

	public void decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize

		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);

		imgView.setImageBitmap(bitmap);

	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	Bitmap getURLImage(URL url) {
		URLConnection conn = null;
		try {
			conn = url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(conn.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bm = BitmapFactory.decodeStream(bis);
		try {
			bis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bm;
	}

}
