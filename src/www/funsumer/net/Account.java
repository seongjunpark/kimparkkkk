package www.funsumer.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import org.json.JSONException;
import org.json.JSONObject;

import www.funsumer.net.constants.InviteInfo_fr;
import www.funsumer.net.login.PassWord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Account extends Activity {

	String mynoteid = MainActivity.mynoteid;
	InputStream is = null;
	String json = "";

	EditText name, ename, birth, phone_number, school;
	Button changepass;
	RadioGroup sexGroup;
	String Result_name, Result_ename, Result_birth, Result_gender, Result_univ,
			Result_phone;
	RadioButton male, female;
	String gender;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_layout);

		name = (EditText) findViewById(R.id.name);
		ename = (EditText) findViewById(R.id.ename);
		birth = (EditText) findViewById(R.id.birth);
		school = (EditText) findViewById(R.id.school);
		changepass = (Button) findViewById(R.id.changepass);
		phone_number = (EditText) findViewById(R.id.phone_number);
		sexGroup = (RadioGroup) findViewById(R.id.sexgroup);
		male = (RadioButton) findViewById(R.id.male);
		female = (RadioButton) findViewById(R.id.female);

		getData();

		if (Result_name.equals("") || Result_name == null) {
			name.setText("");
		} else {
			name.setText(Result_name);
		}
		if (Result_ename.equals("") || Result_ename == null) {
			ename.setText("");
		} else {
			ename.setText(Result_ename);
		}
		if (Result_birth.equals("") || Result_birth == null) {
			birth.setText("");
		} else {
			birth.setText(Result_birth);
		}
		if (Result_univ.equals("") || Result_univ == null) {
			school.setText("");
		} else {
			school.setText(Result_univ);
		}

		if (Result_phone.equals("") || Result_phone == null) {
			phone_number.setText("");
		} else {
			phone_number.setText(Result_phone);
		}

		gender = Result_gender;
		if (Result_gender.equals("1") || Result_gender == "1") {
			male.setChecked(true);
		} else {
			female.setChecked(true);
		}

		sexGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton sex = (RadioButton) findViewById(sexGroup
						.getCheckedRadioButtonId());
				switch (checkedId) {
				case R.id.male:
					gender = "1";
					break;
				case R.id.female:
					gender = "2";
					break;
				}

			}
		});
		changepass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Account.this, PassWord.class);
				startActivity(intent);
			}
		});
		

		Button success_ac = (Button) findViewById(R.id.success_ac);
		success_ac.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Success();
				Account.this.finish();
			}
		});

	}

	public void getData() {

		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = "http://funsumer.net/json/";
			HttpPost post = new HttpPost(postURL);
			/*
			 * Toast.makeText(getApplicationContext(), "�곌껐�꾨즺",
			 * Toast.LENGTH_LONG) .show();
			 */

			List params1 = new ArrayList();
			params1.add(new BasicNameValuePair("oopt", "21"));
			params1.add(new BasicNameValuePair("mynoteid", mynoteid));
			params1.add(new BasicNameValuePair("position", "1"));

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

				JSONObject tjo = obj.getJSONObject(0);

				for (int i = 0; i < obj.length(); i++) {
					JSONObject object = obj.getJSONObject(i);
					Result_name = object.getString("Result_name");
					Result_ename = object.getString("Result_ename");
					Result_birth = object.getString("Result_birth");
					Result_gender = object.getString("Result_gender");
					Result_univ = object.getString("Result_univ");
					Result_phone = object.getString("Result_phone");

					System.out.println("asefasefasef" + Result_gender);
					System.out.println("asefasefasef" + Result_phone);

				}

			}

		} catch (Exception e) {
		}

	}

	public void Success() {

		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = "http://funsumer.net/json/";
			HttpPost post = new HttpPost(postURL);
			/*
			 * Toast.makeText(getApplicationContext(), "�곌껐�꾨즺",
			 * Toast.LENGTH_LONG) .show();
			 */

			List params1 = new ArrayList();
			params1.add(new BasicNameValuePair("oopt", "21"));
			params1.add(new BasicNameValuePair("mynoteid", mynoteid));
			params1.add(new BasicNameValuePair("position", "2"));
			params1.add(new BasicNameValuePair("name", name.getText()
					.toString()));
			params1.add(new BasicNameValuePair("ename", ename.getText()
					.toString()));
			params1.add(new BasicNameValuePair("birth", birth.getText()
					.toString()));
			params1.add(new BasicNameValuePair("gender", gender));
			params1.add(new BasicNameValuePair("univ", school.getText()
					.toString()));
			params1.add(new BasicNameValuePair("phone", phone_number.getText()
					.toString()));

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

		} catch (Exception e) {
		}

	}

}
