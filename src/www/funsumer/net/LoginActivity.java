package www.funsumer.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import www.funsumer.net.login.AlertDialogManager;
import www.funsumer.net.login.SessionManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoginActivity extends Activity {
//	private static String loginurl = "http://funsumer.net/json/?opt=1";
	String url;
	int tabloginid = 1;

	EditText txtUsername, txtPassword; // Email, password edittext

	Button btn_Login;
	Button btn_Joinus;

	Animation goup, godown;
	Animation fade_out, fade_in;

	RelativeLayout loginbox;
	LinearLayout editbox;

	AlertDialogManager alert = new AlertDialogManager(); // Alert Dialog Manager

	SessionManager session; // Session Manager Class

	JSONArray LoginAPI = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		final JSONParser jParser = new JSONParser();

		ImageView imageView = (ImageView) findViewById(R.id.loginpic);
		try {
			imageView.setImageBitmap((Bitmap) getURLImage(new URL(
					"http://www.funsumer.net/mobile_login_background.jpg")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// Session Manager
		session = new SessionManager(getApplicationContext());

		// Email, Password input text
		txtUsername = (EditText) findViewById(R.id.enterid);
		txtPassword = (EditText) findViewById(R.id.txtPassword);

		Toast.makeText(getApplicationContext(),
				"User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG)
				.show();

		Log.e("bbb", "RUNNNNNNNNNING 1" + tabloginid);
		
		goup = AnimationUtils.loadAnimation(this, R.anim.goup);
		godown = AnimationUtils.loadAnimation(this, R.anim.godown);
		fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		
//		loginbox = (RelativeLayout) findViewById(R.id.loginbox);
		editbox = (LinearLayout) findViewById(R.id.editbox);

		// Login button
		btn_Login = (Button) findViewById(R.id.btn_Login);
		btn_Login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// session = new SessionManager(getApplicationContext());
				// session.checkLogin();
				if (tabloginid == 1) {
//					btn_Login.setAnimation(godown);
					
					editbox.setVisibility(View.VISIBLE);
					
					txtUsername.setVisibility(View.VISIBLE);
					txtPassword.setVisibility(View.VISIBLE);
					txtUsername.setAnimation(fade_in);
					txtPassword.setAnimation(fade_in);
					
					btn_Joinus.setVisibility(View.INVISIBLE);
					
					tabloginid = 2;
				} else if (tabloginid == 2) {
					// Animation translateLeftAnim =
					// AnimationUtils.loadAnimation(this, R.anim.rotate);

					// Get username, password from EditText
					String username = txtUsername.getText().toString();
					String password = txtPassword.getText().toString();

					// Check if username, password is filled
					if (username.trim().length() > 0
							&& password.trim().length() > 0) {

						if (username.equals("")) {
							alert.showAlertDialog(LoginActivity.this,
									"이메일을 입력하세요", "", false);
						} else {
							if (password.equals("")) {
								alert.showAlertDialog(LoginActivity.this,
										"비밀번호를 입력하세요", "", false);
							} else {
								url = "http://funsumer.net/json/?opt=1" + "&LoginID=" + username + "&LoginPass=" + password;
								
								try {
									JSONObject json = jParser
											.getJSONFromUrl(url);

									LoginAPI = json.getJSONArray("LoginAPI");

									JSONObject login_obj = LoginAPI
											.getJSONObject(0);

									String Result = login_obj
											.getString("Result");

									if (Result.equals("0")) {
										String Result_data = login_obj.getString("Result_data");
//										String Event = login_obj.getString("Event");
										
										alert.showAlertDialog(
												LoginActivity.this,
												"펀슈머에 오신 것을 환영합니다!",
												"Success", false);
										
//										Log.e("main", "Event" + Event);

										session.createLoginSession(Result_data);

										Intent i = new Intent(
												getApplicationContext(),
												MainActivity.class);
										i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
										startActivity(i);
										
										finish();
									} else if (Result.equals("1")) {
										alert.showAlertDialog(
												LoginActivity.this,
												"Login failed..",
												"Username/Password is incorrect",
												false);

									} else {
										alert.showAlertDialog(
												LoginActivity.this,
												"Data Submit Failed", "", false);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}

						/*
						 * if(username.equals("a") && password.equals("b")){
						 * 
						 * // Creating user login session // For testing i am
						 * stroing name, email as follow // Use user real data
						 * session.createLoginSession("5", "6");
						 * 
						 * // Staring MainActivity Intent i = new
						 * Intent(getApplicationContext(), ApiDaumNaver.class);
						 * startActivity(i); finish();
						 * 
						 * }else{ // username / password doesn't match
						 * alert.showAlertDialog(LoginActivity.this,
						 * "Login failed..", "Username/Password is incorrect",
						 * false); }
						 */

					} else {
						// user didn't entered username or password
						// Show alert asking him to enter the details
						alert.showAlertDialog(LoginActivity.this,
								"Login failed..",
								"Please enter username and password", false);
					}

				}
			}
		});

		btn_Joinus = (Button) findViewById(R.id.btn_Joinus);
		btn_Joinus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), Join_Reg01.class);
				startActivity(i);
				
				finish();
			}
		});
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