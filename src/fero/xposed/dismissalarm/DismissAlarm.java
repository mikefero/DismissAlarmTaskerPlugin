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

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * Xposed initialization class to ensure KitKat or greater compatibility and
 * to hook into the android Alarm Clock/Desk Clock setDismissState methods.
 * During reflection of the setDismissState, this class will create an intent
 * to relay and check an event plugin for Tasker.
 * 
 * NOTE:  Tasker event protocol was added in v4.3b5 and may require download
 *        and installation from Tasker's beta page:
 *        
 *         http://tasker.dinglisch.net/beta.html
 */
public class DismissAlarm extends Service implements IXposedHookZygoteInit,
		IXposedHookLoadPackage {
	/**
	 * Package name for the alarm/desk clock
	 */
	private static final String ALARM_CLOCK_PACKAGE_NAME = "com.google.android.deskclock";
	/**
	 * AlarmStateManager class string name
	 */
	private static final String ALARM_STATE_MANAGER_CLASS_NAME = "com.android.deskclock.alarms.AlarmStateManager";
	/**
	 * AlarmInstance class string name
	 */
	private static final String ALARM_INSTANCE_CLASS_NAME = "com.android.deskclock.provider.AlarmInstance";

	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		//Log hardware and software information for device
		Logger.Info("Hardware: " + Build.HARDWARE);
		Logger.Info("Product: " + Build.PRODUCT);
		Logger.Info("Device manufacturer: " + Build.MANUFACTURER);
		Logger.Info("Device brand: " + Build.BRAND);
		Logger.Info("Device model: " + Build.MODEL);
		Logger.Info("Android SDK: " + Build.VERSION.SDK_INT);
		Logger.Info("Android Release: " + Build.VERSION.RELEASE);
		Logger.Info("ROM: " + Build.DISPLAY);

		//Ensure that KitKat or greater is being used
		if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)) {
			Logger.Error("Module requires KitKat or greater");
			throw new Exception("Dismiss Alarm Tasker Plugin module requires KitKat or greater");
		}
	}

	/**
	 * Called during each package (apk) load to handle hooking into methods for
	 * the xposed module
	 * 
	 * @param loadPackageParam Parameters for the package being loaded
	 */
	@Override
	public void handleLoadPackage(final LoadPackageParam loadPackageParam) throws Throwable {
		/*
		 * Ensure that we are only handling customizations for the alarm/desk
		 * clock package
		 */
		if (loadPackageParam.packageName.equals(ALARM_CLOCK_PACKAGE_NAME)) {
			//Find the AlarmStateManager and AlarmInstance classes
			final Class<?> alarmStateManager = XposedHelpers.findClass(ALARM_STATE_MANAGER_CLASS_NAME, loadPackageParam.classLoader);
			final Class<?> alarmInstance = XposedHelpers.findClass(ALARM_INSTANCE_CLASS_NAME, loadPackageParam.classLoader);

			Logger.Fine("Hooking AlarmStateManager::setDismissState(Context, AlarmInstance)");
			findAndHookMethod(alarmStateManager, "setDismissState", Context.class, alarmInstance, new XC_MethodHook() {

				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					//Get the alarm label from the method parameters
					String alarmLabel = getObjectField(param.args[1], "mLabel").toString();
					Context context = (Context) param.args[0];
					Logger.Info("Alarm Dismissed: " + alarmLabel);

					//Broadcast the event to tasker
					broadcastDismissStateChange(context, alarmLabel);
					super.beforeHookedMethod(param);
				}

			});
		}
	}

	/**
	 * Broadcast/Send intent for a dismissed alarm
	 * 
	 * @param context Context to utilize for sending the intent
	 * @param alarmLabel Label for the dismissed alarm
	 */
	private final void broadcastDismissStateChange(final Context context, final String alarmLabel) {
		//Create the intent for the dismissed alarm
		Intent intent = new Intent(Constants.DISMISS_ALARM_STATE_INTENT);
		intent.putExtra(Constants.DISMISS_ALARM_KEY_ALARM_LABEL, alarmLabel);

		//Broadcast the intent for the dismissed alarm
		context.sendBroadcast(intent);
		Logger.Fine("Broadcasting Dismiss Alarm Intent");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
