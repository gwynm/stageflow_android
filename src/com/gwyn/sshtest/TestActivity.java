package com.gwyn.sshtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import com.jcraft.*;
import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class TestActivity extends Activity {
	/** Called when the activity is first created. */
	static final String SSH_HOST = "192.168.1.67";
	static final String SSH_USER = "Gwyn Morfey";
	static final String SSH_PASS = "kitt*()";
	Session mysession;

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
	public String runCommand(Session session, String cmd) {
		try {
			ChannelExec channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(cmd);
			channel.connect();

			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));

			StringBuilder responseBuilder = new StringBuilder();
			Log.v("taggy","start sleep");
			SystemClock.sleep(200); //command must begin sending output within 1000ms!
			Log.v("taggy","end sleep..");

			while (!channel.isClosed() && !channel.isEOF()) {
				Log.v("taggy","reading a line");
				String line = reader.readLine();
				if (line != null) {
					responseBuilder.append(line).append("\n");
				}
			}
			Log.v("taggy","returning");

			return responseBuilder.toString();
		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();	
			return "KABOOM";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "KABOOM";
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		try {
			mysession = setupSession();
			Log.v("taggy","***!!!!!!!!!!**********" + runCommand(mysession,"ls ~"));
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
