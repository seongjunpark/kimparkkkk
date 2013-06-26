package www.funsumer.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import www.funsumer.net.constants.CommentInfo;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

public class WriteNote extends Activity {

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
	private String goid;

	Button openset0, openset1;
	ImageView icon_unlock, icon_lock;

	public String id;
	private EditText writeAT;
	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";
	private ImageView mPhotoImageView;
	private FileInputStream mFileInputStream = null;
	static InputStream is = null;
	static String json = "";

	DataOutputStream dos;
	private String mynoteid, position;
	private String toid, partyid;
	String openset_result;

	private List<String> registrationIds = new ArrayList<String>();
	private String GCMname, GCMfrom;

	private Sender gcmSender; // GCM Sender
	private Message gcmMessage; // GCM Message
	private MulticastResult gcmMultiResult; // GCM Multi Result(占싹곤옙 占쏙옙占�
	// 媛쒕컻��肄섏넄�먯꽌 諛쒓툒諛쏆� API Key
	private static String API_KEY = "AIzaSyDPayDjS8UbkzGg7sezdcJlvaVihY7S2m8";
	// 硫붿꽭吏�쓽 怨좎쑀 ID(?)�뺣룄濡��앷컖�섎㈃ �⑸땲�� 硫붿꽭吏�쓽 以묐났�섏떊��留됯린 �꾪빐 �쒕뜡媛믪쓣 吏�젙�⑸땲��
	// private static String COLLAPSE_KEY = String.valueOf(Math.random() % 100 +
	// 1);
	// 湲곌린媛��쒖꽦���곹깭����蹂댁뿬以�寃껋씤吏�
	private static boolean DELAY_WHILE_IDLE = true;// origin true
	// 湲곌린媛�鍮꾪솢�깊솕 �곹깭����GCM Storage��蹂닿��섎뒗 �쒓컙
	private static int TIME_TO_LIVE = 1800;// origin 3
	// 硫붿꽭吏��꾩넚 �ㅽ뙣���ъ떆�꾪븷 �잛닔
	private static int RETRY = 3;

	private static final String TAG = "GCM";
	private String GCMposition;

	// int selItem;
	// Spinner spinner1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// partyid = PartyActivity.partyid;
		setContentView(R.layout.upload_test);

		mynoteid = MainActivity.mynoteid;
		toid = NoteActivity.userid;
		partyid = PartyActivity.partyid;

		mPhotoImageView = (ImageView) findViewById(R.id.image);
		// imgView = (ImageView) findViewById(R.id.ImageView);

		writeAT = (EditText) findViewById(R.id.writeAT);

		Intent getintent = getIntent();
		position = getintent.getStringExtra("position");

		// if(position.equals("1")){
		// spinner1 = (Spinner) findViewById(R.id.Spinner1);
		// ArrayAdapter adapterS = ArrayAdapter.createFromResource(this,
		// R.array.city, android.R.layout.simple_spinner_dropdown_item);
		// spinner1.setAdapter(adapterS);
		//
		// }else{
		// spinner1 = (Spinner) findViewById(R.id.Spinner1);
		// ArrayAdapter adapterS = ArrayAdapter.createFromResource(this,
		// R.array.city1, android.R.layout.simple_spinner_dropdown_item);
		// spinner1.setAdapter(adapterS);
		// }
		//
		// spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// // TODO Auto-generated method stub
		// selItem = (int) spinner1.getSelectedItemPosition();
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		Button btn_next02 = (Button) findViewById(R.id.btn);
		btn_next02.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (writeAT.getText().toString().equals("")
						&& full_path == null) {
					new CustomDialog(WriteNote.this,
							CustomDialog.TYPE_BASIC_OK, null, "내용을 작성해주세요.")
							.show();
					return;
				} else {
					if (selectedImagePath == null) {
						onlyText();

					} else {
						try {
							String mFilePath = selectedImagePath;
							DoFileUpload(mFilePath);
						} catch (Exception e) {

						}
						// ///////////text upload
					}

					MainActivity.userid = toid;

					Log.e("ooo", "position is = " + position);

					if (position.equals("1")) {
						Intent intentput = new Intent(WriteNote.this,
								NoteActivity.class);
						intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intentput.putExtra("userid", toid);
						position = null;
						startActivity(intentput);
					} else if (position.equals("2")) {
						Intent intentput2 = new Intent(WriteNote.this,
								PartyActivity.class);
						intentput2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intentput2.putExtra("partyid", partyid);
						position = null;
						startActivity(intentput2);
					}
				}
			}
		});

		openset_result = "0";

		openset0 = (Button) findViewById(R.id.openset0);
		openset0.setOnClickListener(opensetClick);
		openset0.setVisibility(View.VISIBLE);

		openset1 = (Button) findViewById(R.id.openset1);
		openset1.setOnClickListener(opensetClick);
		openset1.setVisibility(View.INVISIBLE);
		if (position.equals("1")) {
			openset1.setText("친구공개");
		} else if (position.equals("2")) {
			openset1.setText("파티공개");
		}

		icon_unlock = (ImageView) findViewById(R.id.icon_unlock);
		icon_lock = (ImageView) findViewById(R.id.icon_lock);

		Log.e("abc", " mynoteid = " + mynoteid + "     toid = " + toid);
	}

	final OnClickListener opensetClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (openset_result.equals("0")) {
				if (position.equals("1")) {
					openset_result = "1";
				} else if (position.equals("2")) {
					openset_result = "2";
				}
				openset1.setVisibility(View.VISIBLE);
				openset0.setVisibility(View.INVISIBLE);
				icon_unlock.setVisibility(View.INVISIBLE);
				icon_lock.setVisibility(View.VISIBLE);
			} else if (openset_result.equals("1")) {
				openset_result = "0";
				openset0.setVisibility(View.VISIBLE);
				openset1.setVisibility(View.INVISIBLE);
				icon_unlock.setVisibility(View.VISIBLE);
				icon_lock.setVisibility(View.INVISIBLE);
			} else if (openset_result.equals("2")) {
				openset_result = "0";
				openset0.setVisibility(View.VISIBLE);
				openset1.setVisibility(View.INVISIBLE);
				icon_unlock.setVisibility(View.VISIBLE);
				icon_lock.setVisibility(View.INVISIBLE);
			}
			Log.e("abc", "openset_result adfdsaf==== " + openset_result);
		}
	};

	public void onButtonClick(View v) {
		switch (v.getId()) {
		case R.id.btn_image_crop:
			mDialog = createDialog();
			mDialog.show();
			break;
		}
	}

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
		ab.setTitle("占쎈�占쏙쭪占폚rop");
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
		// 占썩뫀苡�占쎈챷��
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

			try {
				mImageCaptureUri = data.getData();
				File original_file = getImageFile(mImageCaptureUri);

				mImageCaptureUri = createSaveCropFile();
				File copy_file = new File(mImageCaptureUri.getPath());
				copyFile(original_file, copy_file);

				selectedImagePath = copy_file.getPath();
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(mImageCaptureUri, "image/*");
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
			// 占쎈�占쏙쭪占쏙옙 揶쏉옙議뉛옙占쏙옙�꾩뜎占쏙옙�귐딄텢占쎈똻已곤옙占쏙옙��옙筌욑옙占싼덈┛�쒙옙野껉퀣�숋옙�몃빍占쏙옙
			// 占쎈똾�묕옙占쏙옙��옙筌욑옙占싼됤댘 占쎈똾逾녺뵳�占쏙옙�곷�占쏙옙占쎈챷�㏆옙�띿쓺 占썩뫖�뀐옙占�

			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(mImageCaptureUri, "image/*");
			mFileName = String.valueOf(System.currentTimeMillis())
					+ BACKGROUNDIMAGE_FILE;
			Uri u = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory() + "/funsumer", mFileName));

			// Crop占쏙옙占쎈�占쏙쭪占쏙옙 占쏙옙�ｏ옙占폩ath
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
			mPhotoImageView.setImageBitmap(photo);
			Log.e(TAG,
					"PICK_FROM_CAMERA size : " + photo.getHeight()
							* photo.getWidth());
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
				mPhotoImageView.setImageBitmap(photo1);
				Log.e(TAG,
						"PICK_FROM_ALBUM : " + photo1.getHeight()
								* photo1.getWidth());

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

	private void DoFileUpload(String filePath) throws IOException {
		Log.d("Test", "file path = " + filePath);
		HttpFileUpload("http://funsumer.net/json/", filePath);

	}

	// void>>>>>Returncode
	private void HttpFileUpload(String urlString, String fileName) {
		try {

			if (position.equals("1")) {
				goid = toid;

			} else {
				goid = partyid;
				// if(String.valueOf(selItem).equals("1")){
				// selItem = 2;
				// }

			}
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
			writeFormField("oopt", "2");
			writeFormField("position", position);
			writeFormField("mynoteid", mynoteid);
			writeFormField("toid", goid);
			writeFormField("aopen", openset_result);
			writeFormField("content", writeAT.getText().toString());

			Log.e("abc", "openset_result ==== " + openset_result);

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
			if (position.equals("1")) {
				GCMposition = "1";
			} else {
				GCMposition = "2";
			}
			getJson(mynoteid, GCMposition, goid, writeAT.getText().toString());

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

		}

	}

	public void onlyText() {

		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = "http://funsumer.net/json/";
			HttpPost post = new HttpPost(postURL);
			/*
			 * Toast.makeText(getApplicationContext(), "占쎄퀗猿먲옙袁⑥┷",
			 * Toast.LENGTH_LONG) .show();
			 */
			if (position.equals("1")) {
				goid = toid;
			} else {
				goid = partyid;
				// if(String.valueOf(selItem).equals("1")){
				// selItem = 2;
				// }

			}
			Log.w("funsumerWOW", mynoteid);
			List params1 = new ArrayList();
			params1.add(new BasicNameValuePair("oopt", "2"));
			params1.add(new BasicNameValuePair("mynoteid", mynoteid));
			params1.add(new BasicNameValuePair("toid", goid));
			params1.add(new BasicNameValuePair("position", position));
			params1.add(new BasicNameValuePair("content", writeAT.getText()
					.toString()));
			params1.add(new BasicNameValuePair("aopen", openset_result));

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
					HTTP.UTF_8);
			post.setEntity(ent);

			HttpResponse responsePOST = client.execute(post);

			HttpEntity resEntity = responsePOST.getEntity();
			is = resEntity.getContent();

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "euc-kr"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			if (resEntity != null) {
				JSONArray obj = new JSONArray(json);

				for (int i = 0; i < obj.length(); i++) {

					JSONObject tjo = obj.getJSONObject(i);
					String regId = tjo.getString("ID");
					GCMname = tjo.getString("Name");
					GCMfrom = tjo.getString("From");

					registrationIds.add(0, regId);

				}
				setMessage();
				sendMessage();
			}

		} catch (Exception e) {

			// TODO: handle exception
		}

	}

	public static boolean copyFile(File original_file, File copy_file) {
		boolean result = false;
		try {
			InputStream in = new FileInputStream(original_file);
			try {
				result = copyToFile(in, copy_file);
			} finally {
				in.close();
			}
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	private static boolean copyToFile(InputStream inputStream, File copy_file) {
		try {
			OutputStream out = new FileOutputStream(copy_file);
			try {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) >= 0) {
					out.write(buffer, 0, bytesRead);
				}
			} finally {
				out.close();
			}
			return true;
		} catch (IOException e) {
			return false;
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

	private File getImageFile(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		if (uri == null) {
			uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		}

		Cursor mCursor = getContentResolver().query(uri, projection, null,
				null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
		if (mCursor == null || mCursor.getCount() < 1) {
			return null; // no cursor or no record
		}
		int column_index = mCursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		mCursor.moveToFirst();

		String path = mCursor.getString(column_index);

		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
		}

		return new File(path);
	}

	String GCMFrom_id;

	public void setMessage() {
		gcmSender = new Sender(API_KEY);
		gcmMessage = new Message.Builder()
				// .collapseKey(COLLAPSE_KEY)
				.delayWhileIdle(DELAY_WHILE_IDLE).timeToLive(TIME_TO_LIVE)
				.addData("ticker", GCMname + "님이 글을 남기셨습니다.")
				.addData("title", "[Funsumer]" + GCMfrom)
				.addData("msg", writeAT.getText().toString())
				.addData("ids", GCMFrom_id).build();

	}

	public void sendMessage() {
		// �쇨큵�꾩넚�쒖뿉 �ъ슜
		try {
			gcmMultiResult = gcmSender.send(gcmMessage, registrationIds, RETRY);

		} catch (IOException e) {
			Log.w(TAG, "IOException " + e.getMessage());
		}
		Log.w(TAG, "getCanonicalIds : " + gcmMultiResult.getCanonicalIds()
				+ "\n" + "getSuccess : " + gcmMultiResult.getSuccess() + "\n"
				+ "getTotal : " + gcmMultiResult.getTotal() + "\n"
				+ "getMulticastId : " + gcmMultiResult.getMulticastId());

	}

	public void getJson(String a, String b, String c, String d) {
		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = "http://funsumer.net/json/";
			HttpPost post = new HttpPost(postURL);
			/*
			 * Toast.makeText(getApplicationContext(), "占쎄퀗猿먲옙袁⑥┷",
			 * Toast.LENGTH_LONG) .show();
			 */
			List params1 = new ArrayList();
			params1.add(new BasicNameValuePair("mynoteid", a));
			params1.add(new BasicNameValuePair("optgcm", b));
			params1.add(new BasicNameValuePair("toid", c));
			params1.add(new BasicNameValuePair("content", d));
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
					HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
			HttpEntity resEntity = responsePOST.getEntity();
			is = resEntity.getContent();

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "euc-kr"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			if (resEntity != null) {
				JSONArray obj = new JSONArray(json);

				for (int i = 0; i < obj.length(); i++) {

					JSONObject tjo = obj.getJSONObject(i);
					String regId = tjo.getString("ID");
					GCMname = tjo.getString("Name");
					GCMfrom = tjo.getString("From");
					GCMFrom_id = tjo.getString("FROM_ID");
					registrationIds.add(0, regId);
				}
				setMessage();
				sendMessage();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}