Dismiss Alarm Tasker Plugin
========================================

DATP is an Xposed Module and Tasker plugin for handling dismissed alarms
originating from the KitKat Desk Clock (Alarm Clock) on Nexus 5 devices.  DATP
uses the Xposed findAndHookMethod to intercept the
AlarmStateManager::setDismissState and creates an intent to initiate the Tasker
plugin event mechanism.  The Tasker plugin portion of DATP allows you to set an
alarm label of interest to isolate named alarm events; blank (empty string)
alarm labels will result in all dismissed alarms events for that configuration.

Installation Requirements
----------------------------------------

 ### [Xposed Framework] v2.4+ - http://repo.xposed.info/ (FREE)
 
     Xposed is a framework for modules that can change the behavior of the
     system and apps without touching any APKs. That\'s great because it means
     that modules can work for different versions and even ROMs without any
     changes (as long as the original code was not changed too much). It\'s also
     easy to undo. As all changes are done in the memory, you just need to
     deactivate the module and reboot to get your original system back. There
     are many other advantages, but here is just one more: Multiple modules can
     do changes to the same part of the system or app. With modified APKs, you
     to decide for one. No way to combine them, unless the author builds
     multiple APKs with different combinations.

 ### [Tasker] v4.3b5+ - http://tasker.dinglisch.net/index.html (Purchase Required)
                    http://tasker.dinglisch.net/beta.html

     Tasker is an application for Android which performs tasks (sets of
     actions) based on contexts (application, time, date, location, event,
     gesture) in user-defined profiles or in clickable or timer home screen
     widgets.

     This simple concept profoundly extends your control of your Android device
     and it\'s capabilities, without the need for \'root\' or a special home
     screen.
     
     *NOTE:  As of 2014/03/05 the current version available on the Google Play
            Store is v4.2u3.  The beta edition is required due to the addition
            of support for developer event plugins.
            
              Action - net.dinglisch.android.tasker.ACTION_EDIT_EVENT
            
            See http://tasker.dinglisch.net/changes/nextversion.html for the
            changelog in the upcoming version of Tasker.*

Build Requirement
----------------------------------------

  ### [Locale API] - http://www.twofortyfouram.com/developer
  
       The Locale Developer Platform exposes APIs for plug-in conditions and
       settings. Interaction with Locale occurs via Intents which are
       documented in the following sections, along with the
       http://www.twofortyfouram.com/developer/doc/index.html

Version/Change Log
----------------------------------------
1.0 - 2014/03/05 - Initial release

Thanks
----------------------------------------
rovo89 - Xposed Framework
Pent - Tasker
Two Forty Four AM - Locale API
