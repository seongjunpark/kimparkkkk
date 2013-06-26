package www.funsumer.net.login;

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

import www.funsumer.net.CustomDialog;
import www.funsumer.net.Join_Reg01;
import www.funsumer.net.MainActivity;
import www.funsumer.net.R;
import www.funsumer.net.constants.InviteInfo_fr;

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

public class PassWord extends Activity {

	String mynoteid = MainActivity.mynoteid;
	InputStream is = null;
	String json = "";

	EditText pass, newpass, newpass2;
	String ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_pass);

		pass = (EditText) findViewById(R.id.pass);
		newpass = (EditText) findViewById(R.id.newpass);
		newpass2 = (EditText) findViewById(R.id.newpass2);

		Button success_pass = (Button) findViewById(R.id.success_pass);
		success_pass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (pass.equals("") || pass == null) {

					new CustomDialog(PassWord.this, CustomDialog.TYPE_BASIC_OK,
							null, "새로운 비밀번호를 작성해 주세요.").show();
					return;

				} else if (!(newpass.getText().toString().equals(newpass2.getText().toString()))) {

					new CustomDialog(PassWord.this, CustomDialog.TYPE_BASIC_OK,
							null, "새로운 비밀번호를 작성해 주세요.").show();
					return;

				} else if (newpass.getText().toString().equals("")
						|| newpass.getText().toString() == null) {

					new CustomDialog(PassWord.this, CustomDialog.TYPE_BASIC_OK,
							null, "새로운 비밀번호를 작성해 주세요.").show();
					return;

				} else if (newpass2.getText().toString().equals("")
						|| newpass2.getText().toString() == null) {

					new CustomDialog(PassWord.this, CustomDialog.TYPE_BASIC_OK,
							null, "새로운 비밀번호를 작성해 주세요.").show();
					return;
					

				} else if (newpass2.getText().toString().length()<8) {

					new CustomDialog(PassWord.this, CustomDialog.TYPE_BASIC_OK,
							null, "새로운 비밀번호를 작성해주세요.").show();
					return;

				}else {
					pushData();
					if (ok.equals("0")) {
						new CustomDialog(PassWord.this,
								CustomDialog.TYPE_BASIC_OK, null,
								"새로운 비밀번호 확인을 다시 작성해 주세요.").show();
						return;
					} else {
						
						PassWord.this.finish();
						new CustomDialog(PassWord.this,
								CustomDialog.TYPE_BASIC_OK, null,
								"비밀번호가 변경되었습니다!").show();
						return;
						

					}
				}

			}
		});

	}

	public void pushData() {

		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = "http://funsumer.net/json/";
			HttpPost post = new HttpPost(postURL);
			/*
			 * Toast.makeText(getApplicationContext(), "占쎄퀗猿먲옙袁⑥┷",
			 * Toast.LENGTH_LONG) .show();
			 */

			List params1 = new ArrayList();
			params1.add(new BasicNameValuePair("oopt", "21"));
			params1.add(new BasicNameValuePair("mynoteid", mynoteid));
			params1.add(new BasicNameValuePair("position", "3"));
			params1.add(new BasicNameValuePair("p1", pass.getText().toString()));
			params1.add(new BasicNameValuePair("p2", newpass.getText()
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

			if (resEntity != null) {
				JSONArray obj = new JSONArray(json);

				JSONObject tjo = obj.getJSONObject(0);

				for (int i = 0; i < obj.length(); i++) {
					JSONObject object = obj.getJSONObject(i);
					ok = object.getString("ok");

				}

			}

		} catch (Exception e) {
		}

	}

}
