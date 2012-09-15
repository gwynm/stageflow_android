package com.gwyn.stageflow;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gwyn.stageflow.R;
import com.jcraft.jsch.*;
import com.nullwire.trace.ExceptionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class PresentActivity extends Activity {
	public static String PREFS_NAME="StageFlowPrefs";


	Session session;	

	private OnClickListener nextListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				nextSlide();
				displayStatus(getCurrentNotes());
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				displayStatus("Error: " + e.getMessage() + ". Suggest you click 'Back' and reconnect.");
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				displayStatus("Error: " + e.getMessage() + ". Suggest you click 'Back' and reconnect.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				displayStatus("Error: " + e.getMessage() + ". Suggest you click 'Back' and reconnect.");
			}
		}
	};

	private OnClickListener prevListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				prevSlide();
				displayStatus(getCurrentNotes());
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				displayStatus("Error: " + e.getMessage() + ". Suggest you click 'Back' and reconnect.");
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				displayStatus("Error: " + e.getMessage() + ". Suggest you click 'Back' and reconnect.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				displayStatus("Error: " + e.getMessage() + ". Suggest you click 'Back' and reconnect.");
			}
		}
	};


	public Session setupSession(String host, String user, String pass) throws JSchException {
		JSch jsch=new JSch();  
		Session session = jsch.getSession(user,host,22);

		java.util.Properties config = new java.util.Properties(); 
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);

		session.setPassword(pass);
		session.connect();

		return session;
	}

	public String runCommand(String command) throws JSchException,IOException {
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(command);
		channel.connect();

		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));

		StringBuilder responseBuilder = new StringBuilder();
		
		while (!channel.isEOF()) {
			String line = reader.readLine();
//			Log.v("gwyn","Got " + line);
			if (line != null) {
				responseBuilder.append(line).append("\n");
			} else {
				SystemClock.sleep(50);
			}
		}
		return responseBuilder.toString();
	}
	
	public String trimToEndMarker(String txt) {
		int endpos = txt.indexOf("ZSFZ");
		if (endpos == -1) {
			return txt;
		} else {
			return txt.substring(0, endpos);
		}
	}

	public String getCurrentNotes() throws JSchException,IOException {
		return runCommand("/usr/bin/osascript -e 'tell application \"Keynote\" to get the notes of the current slide of the first slideshow'");
	}
	
	public String nextSlide() throws JSchException, IOException  {
		return runCommand("/usr/bin/osascript -e 'tell application \"Keynote\" to advance'");
	}

	public String prevSlide() throws JSchException, IOException  {
		return runCommand("/usr/bin/osascript -e 'tell application \"Keynote\" to show previous'");
	}

	
	public void displayStatus(String newstatus) {
		TextView status = (TextView)findViewById(R.id.status);
		status.setText(newstatus);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ExceptionHandler.register(this);
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getIntent().getExtras();
		String host = bundle.getString("host");
		String user = bundle.getString("user");
		String pass = bundle.getString("pass");
		
		setContentView(R.layout.present);

		Button nextButton = (Button)findViewById(R.id.next_button);
		nextButton.setOnClickListener(nextListener); 
		
		Button prevButton = (Button)findViewById(R.id.prev_button);
		prevButton.setOnClickListener(prevListener);
		
		displayStatus("Here we go");
		
		try {
			session = setupSession(host,user,pass);
			displayStatus(getCurrentNotes());
			recordSuccessfulConnection();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			displayStatus("Couldn't connect to your Mac! (Error: " + e.getMessage() + ") Press 'back' to try again.");
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			displayStatus("Couldn't connect to your Mac! (Error: " + e.getMessage() + ") Press 'back' to try again.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			displayStatus("Couldn't connect to your Mac! (Error: " + e.getMessage() + ") Press 'back' to try again.");
		}
	}
	
	private void recordSuccessfulConnection() {
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("has_ever_connected", true);
        editor.commit();	
	}
}
