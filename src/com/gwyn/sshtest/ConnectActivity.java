package com.gwyn.sshtest;

import java.io.IOException;

import com.jcraft.jsch.JSchException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ConnectActivity extends Activity {

	private OnClickListener connectListener = new OnClickListener() {
		public void onClick(View v) {
			String host = ((EditText)findViewById(R.id.host_text)).getText().toString();
			String user = ((EditText)findViewById(R.id.user_text)).getText().toString();
			String pass = ((EditText)findViewById(R.id.pass_text)).getText().toString();
			Intent intent = new Intent(ConnectActivity.this,PresentActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("host",host);
			bundle.putString("user",user);
			bundle.putString("pass",pass);
			intent.putExtras(bundle);
			ConnectActivity.this.startActivity(intent);
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect);

		Button connectButton = (Button)findViewById(R.id.connect_button);
		connectButton.setOnClickListener(connectListener); 
	}
}
