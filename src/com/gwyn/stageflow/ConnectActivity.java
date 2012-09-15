package com.gwyn.stageflow;

import com.gwyn.stageflow.R;
import com.nullwire.trace.ExceptionHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class ConnectActivity extends Activity {
	public static String PREFS_NAME="StageFlowPrefs";
	
	private OnClickListener helpListener = new OnClickListener() {
		public void onClick(View v) {
			showHelp();
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
		ExceptionHandler.register(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect);

		Button connectButton = (Button)findViewById(R.id.connect_button);
		connectButton.setOnClickListener(connectListener); 
		
		Button helpButton = (Button)findViewById(R.id.help_button);
		helpButton.setOnClickListener(helpListener); 
		
		retrieveDetailsToUI();
		showHelpIfNeverConnectedOK();
	}
	
	private void showHelpIfNeverConnectedOK() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
	   	Boolean has_ever_connected = settings.getBoolean("has_ever_connected",false);
	   	if (!has_ever_connected) {
	   		showHelp();
	   	}
	}
	
	private void showHelp() {
		Intent intent = new Intent(ConnectActivity.this,HelpActivity.class);
		ConnectActivity.this.startActivity(intent);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		retrieveDetailsToUI();
	}	
}
