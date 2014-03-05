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
package fero.taskerplugin.dismissalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import fero.taskerplugin.dismissalarm.utilities.BundleScrubber;
import fero.xposed.dismissalarm.Constants;
import fero.xposed.dismissalarm.Logger;

/**
 * This is the receiver for the dismiss alarm intent provided by the Xposed
 * setDismissState hook method and for the Takser event plugin. Upon receipt of
 * the Xposed dismiss alarm intent the alarm label will be stored and used to
 * respond to the Tasker QUERY_EVENT by checking the configuration alarm against
 * the dismissed one.
 * 
 * NOTE: Tasker event protocol was added in v4.3b5 and may require download and
 * installation from Tasker's beta page:
 * 
 * http://tasker.dinglisch.net/beta.html
 */
public class DismissAlarmReceiver extends BroadcastReceiver {
	/**
	 * Intent to ask Takser to re-query this plug-in event
	 */
	private static final Intent INTENT_REQUEST_REQUERY = new Intent(com.twofortyfouram.locale.Intent.ACTION_REQUEST_QUERY).putExtra(com.twofortyfouram.locale.Intent.EXTRA_ACTIVITY, ConfigurationActivity.class.getName());
	/**
	 * Dismissed alarm label
	 */
	private static String _dismissedAlarmLabel = new String("");

	@Override
	public void onReceive(Context context, Intent intent) {
		/*
		 * Check for malformed intent (accept only DISMISS_ALARM_INTENT and
		 * ACTION_QUERY_CONDITION
		 */
		if (isDismissAlarmIntentValid(intent)) {
			//Get the alarm label
			_dismissedAlarmLabel = intent.getExtras().getString(Constants.DISMISS_ALARM_KEY_ALARM_LABEL);
			Logger.Fine("Alarm is Being Dismissed: " + _dismissedAlarmLabel);

			//Broadcast the Tasker requery event to finalize the event
			context.sendBroadcast(INTENT_REQUEST_REQUERY);
		} else if (isTaskerQueryConditionIntentValid(intent)) {
			//Get the configured alarm label
			final Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
			String configuredAlarmLabel = bundle.getString(Constants.DISMISS_ALARM_KEY_ALARM_LABEL);
			Logger.Fine("Alarm Label to Validate Against Dismissed Alarm: " + configuredAlarmLabel);

			//Determine if the event condition has been satisfied
			boolean isEventConditionSatisfied = false;
			if (configuredAlarmLabel.isEmpty()) {
				isEventConditionSatisfied = true;
				Logger.Fine("Satisfying Event Condition for Dismissed Alarm: Configured alarm label is empty");
			} else if (_dismissedAlarmLabel.equals(configuredAlarmLabel)) {
				isEventConditionSatisfied = true;
				Logger.Fine("Satisfying Event Condition for Dismissed Alarm: " + configuredAlarmLabel);
			}

			//Determine if the event was satisfied or not
			if (isEventConditionSatisfied) {
				setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_SATISFIED);
			} else {
				Logger.Fine("Event Condition for Dismissed Alarm is Unsatisfied: " + _dismissedAlarmLabel + " != " + configuredAlarmLabel);
				setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNSATISFIED);
			}
		} else {
			Logger.Error(String.format("Received Unexpected/Malformed Intent: " + intent.getAction()));
		}
	}

	/**
	 * Determine if the dismiss alarm intent is valid
	 * 
	 * @param intent Intent to check
	 * @return True if dismiss alarm intent is valid; false otherwise
	 */
	private final boolean isDismissAlarmIntentValid(Intent intent) {
		//Ensure the intent is valid
		if (intent != null) {
			//Scrub the intent
			BundleScrubber.scrub(intent);

			//Verify the action is the dismiss alarm intent
			if (Constants.DISMISS_ALARM_STATE_INTENT.equals(intent.getAction())) {
				Logger.Fine("Dimiss Alarm State Intent Received");
				//Ensure the bundle (extras) are valid
				if (isDismissAlarmBundleValid(intent.getExtras())) {
					//Dismiss alarm intent is valid
					Logger.Fine("Dismiss Alarm Intent is Valid");
					return true;
				}
			}
		}

		//Invalid dismiss alarm intent
		return false;
	}

	/**
	 * Determine if the Tasker query condition intent is valid
	 * 
	 * @param intent Intent to check
	 * @return True if Tasker query condition intent is valid; false otherwise
	 */
	private final boolean isTaskerQueryConditionIntentValid(Intent intent) {
		//Ensure the intent is valid
		if (intent != null) {
			//Scrub the intent
			BundleScrubber.scrub(intent);

			//Verify the action is the Tasker query condition intent
			if (com.twofortyfouram.locale.Intent.ACTION_QUERY_CONDITION.equals(intent.getAction())) {
				Logger.Fine("Tasker Query Condition Intent Received");
				//Ensure the bundle (extras) are valid
				final Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
				if (isDismissAlarmBundleValid(bundle)) {
					//Tasker query condition intent is valid
					Logger.Fine("Tasker Query Condition Intent is Valid");
					return true;
				}
			}
		}

		//Invalid Tasker query condition intent
		return false;
	}

	/**
	 * Determine if the dismiss alarm bundle is valid. This is for the intent
	 * received via the Xposed method hook and from the Tasker query condition
	 * intent for configured plugin events.
	 * 
	 * @param bundle Bundle to check
	 * @return True if dismiss alarm bundle is valid; false otherwise
	 */
	private final boolean isDismissAlarmBundleValid(Bundle bundle) {
		//Ensure the bundle is valid
		if (bundle != null) {
			//Ensure the bundle contains the dismissed alarm label key
			if (bundle.containsKey(Constants.DISMISS_ALARM_KEY_ALARM_LABEL)) {
				//Ensure the size of the bundle (only one extra)
				int bundleSize = bundle.keySet().size();
				if (bundleSize == 1) {
					//Dismiss alarm bundle is valid
					Logger.Fine("Dismiss Alarm Bundle is Valid");
					return true;
				} else {
					Logger.Warning("Invalid Dismissed Alarm Bundle: Bundle contains " + bundleSize + " keys; only one key is required");
				}
			} else {
				Logger.Warning("Invalid Dismissed Alarm Bundle: Missing alarm label");
			}
		}

		//Invalid dismiss alarm intent
		return false;
	}

}
