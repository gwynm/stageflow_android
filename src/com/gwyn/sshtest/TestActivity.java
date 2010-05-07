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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		

		
		JSch jsch=new JSch();  
		try {
			Session session = jsch.getSession("Gwyn Morfey","192.168.1.67",22);
			
			java.util.Properties config = new java.util.Properties(); 
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			session.setPassword("kitt*()");
			session.connect();

			String command = "ls -l /";
			ChannelExec channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(command);
			channel.connect();
			Log.v("taggy","***********************" + "***CONNECTED");


			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));

			StringBuilder responseBuilder = new StringBuilder();
			SystemClock.sleep(1000);
			while (!channel.isClosed() && !channel.isEOF()) {
				String line = reader.readLine();
				if (line != null) {
					responseBuilder.append(line).append("\n");
				}
			}
			Log.v("taggy","********** HERE: " + responseBuilder.toString() + " **************");
			Log.v("taggy","**************************DONE");

		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}
}
