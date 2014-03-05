DismissAlarmTaskerPlugin
========================

DATP is an Xposed Module and Tasker plugin for handling dismissed alarms originating from the KitKat Desk Clock (Alarm Clock) on Nexus 5 devices.  DATP uses the Xposed findAndHookMethod to intercept the AlarmStateManager::setDismissState and creates an intent to initiate the Tasker plugin event mechanism.  The Tasker plugin portion of DATP allows you to set an alarm label of interest to isolate named alarm events; blank (empty string) alarm labels will result in all dismissed alarms events for that configuration.
