package com.gwyn.sshtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jcraft.*;
import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class TestActivity extends Activity {
	/** Called when the activity is first created. */
	public static String SSH_HOST = "192.168.1.67";
	public static String SSH_USER = "Gwyn Morfey";
	public static String SSH_PASS = "kitt*()";
	Session session;	

	private OnClickListener nextListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				nextSlide();
				displayStatus(getCurrentNotes());
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				displayStatus("Explode: " + e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				displayStatus("Explode: " + e.getMessage());
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
				displayStatus("Explode: " + e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				displayStatus("Explode: " + e.getMessage());
			}
		}
	};


	public Session setupSession() throws JSchException {
		JSch jsch=new JSch();  
		Session session = jsch.getSession(SSH_USER,SSH_HOST,22);

		java.util.Properties config = new java.util.Properties(); 
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);

		session.setPassword(SSH_PASS);
		session.connect();

		return session;
	}

	public String runCommand(String command) throws JSchException,IOException {
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(command +  " && echo '---END---' && ls -l /");
		channel.connect();

		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));

		StringBuilder responseBuilder = new StringBuilder();
		SystemClock.sleep(250);
		while (!channel.isClosed() && !channel.isEOF()) {
			String line = reader.readLine();
			if (line != null) {
				responseBuilder.append(line).append("\n");
			}
		}
		return trimToEndMarker(responseBuilder.toString());
	}
	
	public String trimToEndMarker(String txt) {
		int endpos = txt.indexOf("---END---");
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button nextButton = (Button)findViewById(R.id.next_button);
		nextButton.setOnClickListener(nextListener); 
		
		Button prevButton = (Button)findViewById(R.id.prev_button);
		prevButton.setOnClickListener(prevListener);
		
		displayStatus("O Hai");
		
		try {
			session = setupSession();
			displayStatus(getCurrentNotes());
		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			displayStatus("eExplode: " + e1.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			displayStatus("eExplode: " + e.getMessage());
		}
	}
}
