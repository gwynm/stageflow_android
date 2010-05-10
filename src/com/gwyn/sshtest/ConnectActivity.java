package com.gwyn.sshtest;

import java.io.IOException;
import java.util.Date;

import com.jcraft.jsch.JSchException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ConnectActivity extends Activity {
	public static String PREFS_NAME="StageFlowPrefs";
	public static String HELP_TEXT="StageFlow uses your Mac's built-in Remote Login feature to access Keynote. Open System Preferences, select Sharing, and then tick 'Remote Login' (not 'Remote Management') about halfway down. To get the host address, go back to System Preferences, select Network, and look for text like 'and has the IP address 192.168.1.66'. Enter '192.168.1.66'. The 'User Name' is whatever you use to login to your computer normally - probably your full name. The 'Password' is your normal login password. Press your phone's 'back' button to go back to the connection screen. Need more help? Email mail@gwynmorfey.com. ";
	
	private OnClickListener helpListener = new OnClickListener() {
		public void onClick(View v) {
		//	Intent intent = new Intent(ConnectActivity.this,HelpActivity.class);
			//ConnectActivity.this.startActivity(intent);			
			AlertDialog helpDialog = new AlertDialog.Builder(v.getContext()).create();
			helpDialog.setMessage(HELP_TEXT);
			helpDialog.show();
		}
	};
	
	private OnClickListener connectListener = new OnClickListener() {
		public void onClick(View v) {
			String host = ((EditText)findViewById(R.id.host_text)).getText().toString();
			String user = ((EditText)findViewById(R.id.user_text)).getText().toString();
			String pass = ((EditText)findViewById(R.id.pass_text)).getText().toString();
			storeDetails(host,user,pass);
			Intent intent = new Intent(ConnectActivity.this,PresentActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("host",host);
			bundle.putString("user",user);
			bundle.putString("pass",pass);
			intent.putExtras(bundle);
			ConnectActivity.this.startActivity(intent);
		}
	};
	
	private void storeDetails(String host, String user, String pass) {
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	SharedPreferences.Editor editor = settings.edit();
        editor.putString("host", host);
        editor.putString("user", user);
        editor.putString("pass", pass);
        editor.commit();	
	}

	private void retrieveDetailsToUI() {
	   	 SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	     
	   	 String host = settings.getString("host","192.168.1.");
	     EditText hostView = (EditText)findViewById(R.id.host_text);
	     hostView.setText(host);
	     
	     String user = settings.getString("user","");
	     EditText userView = (EditText)findViewById(R.id.user_text);
	     userView.setText(user);

	     String pass = settings.getString("pass","");
	     EditText passView = (EditText)findViewById(R.id.pass_text);
	     passView.setText(pass);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect);

		Button connectButton = (Button)findViewById(R.id.connect_button);
		connectButton.setOnClickListener(connectListener); 
		
		Button helpButton = (Button)findViewById(R.id.help_button);
		helpButton.setOnClickListener(helpListener); 
		
		retrieveDetailsToUI();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		retrieveDetailsToUI();
	}	
}
