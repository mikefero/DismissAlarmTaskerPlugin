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
package fero.xposed.dismissalarm;

/**
 * Shared constants between the Xposed module and the Tasker plugin packages
 */
public class Constants {
	/**
	 * Intent to notify that an alarm has been dismissed
	 */
	public static final String DISMISS_ALARM_STATE_INTENT = "fero.xposed.dismissalarm.DISMISS_ALARM_STATE";
	/**
	 * Alarm label key for the alarm dismissed intent and tasker requery request
	 * intent
	 */
	public static final String DISMISS_ALARM_KEY_ALARM_LABEL = "alarm_label";
	/**
	 * Configured alarm label key for the tasker requery request intent
	 */
	public static final String CONFIGURED_KEY_ALARM_LABEL = "configured_alarm_label";
}
