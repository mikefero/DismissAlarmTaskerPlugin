/**
 * DATP - Dismiss Alarm Tasker Plugin
 * Copyright (C) 2014
 *
 * DATP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DATP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Fero
 */
package fero.taskerplugin.dismissalarm.event.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import fero.taskerplugin.dismissalarm.AbstractPluginActivity;
import fero.taskerplugin.dismissalarm.utilities.BundleScrubber;
import fero.xposed.dismissalarm.Constants;
import fero.xposed.dismissalarm.R;

/**
 * Simple configuration UI for assigning the alarm label to listen to.  If the
 * desire is to get notified when any alarm has been dismissed then the
 * configured alarm label should be the empty string.
 */
public class ConfigurationDismissedAlarmActivity extends AbstractPluginActivity {
	/**
	 * Text view alarm label
	 */
	private TextView _txtAlarmLabel = null;
	/**
	 * Configured alarm label from the text view
	 */
	private String _configuredAlarmLabel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Perform parent operations
		super.onCreate(savedInstanceState);

		//Scrub the intent
		final Intent currentIntent = getIntent();
		BundleScrubber.scrub(currentIntent);

		//Retrieve the  the extra Tasker intent bundle
		final Bundle taskerBundle = currentIntent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);

		//Present the configuration UI
		setContentView(R.layout.configuration_dismissed_alarm_event);

		//Assign the TextView alarm label instance
		_txtAlarmLabel = ((TextView) findViewById(R.id.txtAlarmLabel));

		//Determine if the event has already been configured
		if (taskerBundle != null) {
			//Assign the text view with the already configured alarm label
			_txtAlarmLabel.setText(taskerBundle.getString(Constants.DISMISS_ALARM_KEY_ALARM_LABEL));
		}
	}

	@Override
	public void finish() {
		if (!isCanceled()) {
			//Get the alarm label from the TextView
			_configuredAlarmLabel = _txtAlarmLabel.getText().toString().trim();

			//Construct and send the intent to Tasker
			sendTaskerConfigurationResult(getApplicationContext(), _configuredAlarmLabel);
		}

		//Perform parent operations
		super.finish();
	}
	
	/**
	 * Broadcast/Send intent for a dismissed alarm
	 * 
	 * @param context Context to utilize for sending the intent
	 * @param alarmLabel Label for the dismissed alarm
	 */
	private final void sendTaskerConfigurationResult(final Context context, final String alarmLabel) {
		//Create the bundle associated with the intent
		final Bundle bundle = new Bundle();
		bundle.putString(Constants.DISMISS_ALARM_KEY_ALARM_LABEL, alarmLabel);
		
		//Create the intent for the Tasker configuration activity
		final Intent intent = new Intent();
		
		/*
		 * This extra is the data to ourselves: either for the Activity or
		 * the BroadcastReceiver. Note that anything placed in this Bundle
		 * must be available to Tasker's class loader. So storing String,
		 * int, and other standard objects will work just fine. Parcelable
		 * objects are not acceptable, unless they also implement
		 * Serializable. Serializable objects must be standard Android
		 * platform objects (A Serializable class private to this plug-in's
		 * APK cannot be stored in the Bundle, as Taskers's classloader will
		 * not recognize it).
		 */
		intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, bundle);
		
		//Add the extra blurb to show the configured alarm label on the UI
		intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, alarmLabel);

		//Indicate the configuration is complete
		setResult(RESULT_OK, intent);
	}
}
