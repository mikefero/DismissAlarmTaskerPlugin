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
import fero.xposed.dismissalarm.Logger;

/**
 * This is the receiver for the delete and update alarm intent provided by
 * Tasker plugin actions. Upon receipt of action intent a new intent will be
 * fired to notify the Xposed function hooks for performing the actual delete
 * and update for a particular alarm.
 */
public class DeleteUpdateAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.Info("Intent Received: " + intent.toString());
	}
}
