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
package fero.taskerplugin.dismissalarm.action.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import fero.taskerplugin.dismissalarm.AbstractPluginActivity;
import fero.taskerplugin.dismissalarm.utilities.BundleScrubber;
import fero.xposed.dismissalarm.Constants;
import fero.xposed.dismissalarm.R;

/**
 * Simple configuration UI for assigning the alarm label to listen to. If the
 * desire is to get notified when any alarm has been dismissed then the
 * configured alarm label should be the empty string.
 */
public class ConfigurationUpdateAlarmActivity extends AbstractPluginActivity {
	/**
	 * Text view alarm label
	 */
	private TextView _txtAlarmLabel = null;
	/**
	 * Seek bar for the hours configuration element
	 */
	private SeekBar _seekBarHours = null;
	/**
	 * Text view hours for the display of seekBarHour
	 */
	private TextView _txtHours = null;
	/**
	 * Seek bar for the minutes configuration element
	 */
	private SeekBar _seekBarMinutes = null;
	/**
	 * Text view minutes for the display of seekBarMinutes
	 */
	private TextView _txtMinutes = null;
	/**
	 * Configured alarm label from the text view
	 */
	private String _configuredAlarmLabel = null;
	/**
	 * Configured hours from the seek bar
	 */
	private int _configuredHours = 0;
	/**
	 * Configured minutes from the seek bar
	 */
	private int _configuredMinutes = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Perform parent operations
		super.onCreate(savedInstanceState);

		//Scrub the intent
		Intent currentIntent = getIntent();
		BundleScrubber.scrub(currentIntent);

		//Retrieve the  the extra Tasker intent bundle
		final Bundle taskerBundle = currentIntent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);

		//Present the configuration UI
		setContentView(R.layout.configuration_update_alarm_action);

		//Assign the TextView instances
		_txtAlarmLabel = ((TextView) findViewById(R.id.txtAlarmLabel));
		_txtHours = ((TextView) findViewById(R.id.txtHours));
		_txtMinutes = ((TextView) findViewById(R.id.txtMinutes));

		//Assign the SeekBar instances
		_seekBarHours = ((SeekBar) findViewById(R.id.seekBarHours));
		_seekBarMinutes = ((SeekBar) findViewById(R.id.seekBarMinutes));

		//Create listeners for the seekbars
		_seekBarHours.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				_txtHours.setText(String.valueOf(progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

		});
		_seekBarMinutes.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				_txtMinutes.setText(String.valueOf(progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

		});

		//Determine if the event has already been configured
		if (taskerBundle != null) {
			//Assign the text view with the already configured alarm label
			_txtAlarmLabel.setText(taskerBundle.getString(Constants.DISMISS_ALARM_KEY_ALARM_LABEL));

			//Assign the seek bars and text views with the already configured hours and minutes
			_seekBarHours.setProgress(taskerBundle.getInt(Constants.DISMISS_ALARM_KEY_HOURS));
			_txtHours.setText(String.valueOf(taskerBundle.getInt(Constants.DISMISS_ALARM_KEY_HOURS)));
			_seekBarMinutes.setProgress(taskerBundle.getInt(Constants.DISMISS_ALARM_KEY_MINUTES));
			_txtMinutes.setText(String.valueOf(taskerBundle.getInt(Constants.DISMISS_ALARM_KEY_MINUTES)));
		}
	}

	@Override
	public void finish() {
		if (!isCanceled()) {
			//Get the alarm label from the TextView
			_configuredAlarmLabel = _txtAlarmLabel.getText().toString().trim();

			//Make sure the configured alarm label is valid (not empty)
			if (!_configuredAlarmLabel.isEmpty()) {
				//Get the hours and minutes from the SeekBars
				_configuredHours = _seekBarHours.getProgress();
				_configuredMinutes = _seekBarMinutes.getProgress();

				//Construct and send the intent to Tasker
				sendTaskerConfigurationResult(getApplicationContext(), _configuredAlarmLabel, _configuredHours, _configuredMinutes);
			} else {
				//Create an error message for the user indicating alarm label is required
				final String toastMessage = getResources().getString(R.string.alarm_label_required_error_message);
				Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();

				//Short circuit the closing of the configuration UI
				return;
			}
		}

		//Perform parent operations
		super.finish();
	}

	/**
	 * Broadcast/Send intent for a updated alarm
	 * 
	 * @param context Context to utilize for sending the intent
	 * @param alarmLabel Label for the updated alarm
	 * @param hours Hours for the updated alarm
	 * @param minutes Minutes for the updated alarm
	 */
	private final void sendTaskerConfigurationResult(final Context context, final String alarmLabel, final int hours, final int minutes) {
		//Create the bundle associated with the intent
		final Bundle bundle = new Bundle();
		bundle.putString(Constants.DISMISS_ALARM_KEY_ALARM_LABEL, alarmLabel);
		bundle.putInt(Constants.DISMISS_ALARM_KEY_HOURS, hours);
		bundle.putInt(Constants.DISMISS_ALARM_KEY_MINUTES, minutes);

		//Create the intent for the Tasker configuration activity
		final Intent intent = new Intent();

		/*
		 * This extra is the data to ourselves: either for the Activity or the
		 * BroadcastReceiver. Note that anything placed in this Bundle must be
		 * available to Tasker's class loader. So storing String, int, and other
		 * standard objects will work just fine. Parcelable objects are not
		 * acceptable, unless they also implement Serializable. Serializable
		 * objects must be standard Android platform objects (A Serializable
		 * class private to this plug-in's APK cannot be stored in the Bundle,
		 * as Taskers's classloader will not recognize it).
		 */
		intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, bundle);

		//Add the extra blurb to show the configured alarm label on the UI (use tertiary operation for proper minutes display)
		final String uiMessage = "Update Alarm: " + alarmLabel + " to " + String.valueOf(hours) + ":" + (minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes));
		intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, uiMessage);

		//Indicate the configuration is complete
		setResult(RESULT_OK, intent);
	}
}
