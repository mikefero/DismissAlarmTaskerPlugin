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

import android.util.Log;

/**
 * Logging utility class
 */
public class Logger {
	/**
	 * Prefix for the logging messages
	 */
	private static final String LOGGING_PREFIX = "DATP";

	/**
	 * Log a error message
	 * 
	 * @param message Message to be logged
	 * @param exception Exception/Throwable information to log
	 */
	public synchronized static void Error(String message, Exception exception) {
		if (exception != null) {
			Log.e(LOGGING_PREFIX, message, exception);
		} else {
			Log.e(LOGGING_PREFIX, message);
		}
	}

	/**
	 * Log a error message
	 * 
	 * @param message Message to be logged
	 */
	public synchronized static void Error(String message) {
		Error(message, null);
	}
	
	/**
	 * Log a warning message
	 * 
	 * @param message Message to be logged
	 * @param exception Exception/Throwable information to log
	 */
	public synchronized static void Warning(String message, Exception exception) {
		if (exception != null) {
			Log.w(LOGGING_PREFIX, message, exception);
		} else {
			Log.w(LOGGING_PREFIX, message);
		}
	}

	/**
	 * Log a warning message
	 * 
	 * @param message Message to be logged
	 */
	public synchronized static void Warning(String message) {
		Warning(message, null);
	}

	/**
	 * Log a detailed/fine message
	 * 
	 * @param message Message to be logged
	 */
	public synchronized static void Fine(String message) {
		Log.v(LOGGING_PREFIX, message);
	}

	/**
	 * Log a informative/normal message
	 * 
	 * @param message Message to be logged
	 */
	public synchronized static void Info(String message) {
		Log.i(LOGGING_PREFIX, message);
	}

	/**
	 * Alias for logging level info messages
	 * 
	 * @param message Message to be logged
	 */
	public synchronized static void Log(String message) {
		Info(message);
	}
}
