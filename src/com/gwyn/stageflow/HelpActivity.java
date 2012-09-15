package com.gwyn.stageflow;

import com.gwyn.stageflow.R;
import com.nullwire.trace.ExceptionHandler;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class HelpActivity extends Activity {
	public static String HELP_HTML="" +
			"<h1>Welcome to Stageflow!</h1> " +
			"<p>StageFlow uses your Mac's built-in Remote Login feature to access Keynote. " +
			"You don't need to install anything on your Mac. " +
			"<h3>Turning on Remote Login</h3>" + 
			"<p>Open System Preferences, select Sharing, and then tick 'Remote Login' (not 'Remote Management')" +
			" about halfway down. " + 
			"<h3>Getting your Mac's Address</h3>" + 
			"To get your mac's address, go back to System Preferences, select Network, and look for text " +
			"like this: " + 
			"<blockquote><font color='#0000ff'>Airport is connected to ConferenceWifi and has the IP address 192.168.1.123</font></blockquote>" + 
			"In this example your address is <b>192.168.1.123</b>." +
			"<h3>Your Login Name and Password</h3>" +
			"<p>The 'Login Name' is whatever you use to login to your mac normally, probably your full name. " + 
			"<p>The 'Password' is your normal login password. " +
			"<p>That's all you need to get started!" +
			"<h3>More Help</h3>" +
			"<p>Your phone and your mac need to be able to reach each other. The easiest way is to make sure " +
			"they're on the same wifi network. You can even create a wifi network from your phone, and then " +
			"connect to it from your mac. If there's any kind of firewall in the way it probably won't work." +
			"<p>Need more help? Ask your local tech (tell them your phone needs to SSH to your mac), or email me on mail@gwynmorfey.com!" +
			"<h3>Credits</h3>" +
			"Cheers to hiddedevries for the icon photo: http://www.flickr.com/photos/hiddedevries/599606659/)";
	
	
	private OnClickListener okListener = new OnClickListener() {
		public void onClick(View v) {
			HelpActivity.this.finish();
		}
	};	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		ExceptionHandler.register(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		TextView helpText = (TextView) findViewById(R.id.help_text);
		helpText.setText(Html.fromHtml(HELP_HTML));

		Button okButton = (Button)findViewById(R.id.ok_button);
		okButton.setOnClickListener(okListener); 

	}
		
	@Override
	protected void onResume(){
		super.onResume();
	}	
}
