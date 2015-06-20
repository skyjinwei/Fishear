package com.example.fishear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends ActionBarActivity {
	private Button login;
	private TextView visit_login;
	private TextView regist;
	private EditText username;
	private EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);
		bindView();
		bindListener();
	}
	
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String user = username.getText().toString();
			String pas = password.getText().toString();
			//String pasRe = passwordRe.getText().toString();
			String Url = "http://1.fishearlogin.sinaapp.com/index.php";
			String url1 = Url+"?type=login&name="+user+"&passwd="+pas;
			HttpClient mClient = new DefaultHttpClient();
			HttpGet mHttpGet = new HttpGet(url1);
			Message msg = new Message();
			Bundle data = new Bundle();
			
			try {
				HttpResponse mResp = mClient.execute(mHttpGet);
				HttpEntity mEntity = mResp.getEntity();
				InputStream inputstream = mEntity.getContent();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream));
				String res = "";
				String line = "";
				
				while(null != (line = bufferedReader.readLine())){
					res += line;
				}
				//Log.v("jin", res);
				data.putString("value", res);
				msg.setData(data);
				handler.sendMessage(msg);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				data.putString("value", "error");
				msg.setData(data);
				handler.sendMessage(msg);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				data.putString("value", "error");
				msg.setData(data);
				handler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	};
	
	Handler handler = new Handler(){
		@Override  
	    public void handleMessage(Message msg) {  
	        super.handleMessage(msg);  
	        Bundle data = msg.getData();  
	        String val = data.getString("value");
	        //Log.v("jin",val);
	        if(val.equals("loginFail")){
				Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
			}else if(val.equals("error")){
				Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
				String user = username.getText().toString();
				Intent intent = new Intent();
				intent.setClass(StartActivity.this, MainActivity.class);
				intent.putExtra("user", user);
				startActivity(intent);
			}
	         
	    }  
	};


	private void bindListener() {
		// TODO Auto-generated method stub
		visit_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(StartActivity.this, MainActivity.class);
				intent.putExtra("user", "");
				startActivity(intent);
			}
		});
		
		regist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(StartActivity.this, RegistActivity.class);
				startActivity(intent);
			}
		});
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String user = username.getText().toString();
				String pas = password.getText().toString();
				
				if(user.equals("")){
					Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
				}else if(pas.equals("")){
					Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
				}else{
					ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo netInfo = cm.getActiveNetworkInfo();
					if (netInfo != null && netInfo.isConnectedOrConnecting()) {
						// 有可用的网络
						new Thread(runnable).start();	
					}else{
						Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
					}
					
				}
			}
		});
	}

	public void bindView() {
		// TODO Auto-generated method stub
		login = (Button) findViewById(R.id.start_login);
		visit_login = (TextView) findViewById(R.id.start_login1);
		regist = (TextView) findViewById(R.id.start_regist);
		username = (EditText) findViewById(R.id.start_username);
		password = (EditText) findViewById(R.id.start_password);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_MENU){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
