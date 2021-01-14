# Daily Reminder
I made an Application that will send notification every morning, noon, or night.

## How does this App work?
Press the toggle buttons to choose whether you want the notifications to be on or off.

<img src="https://github.com/randyshee/Android-App/blob/main/Image/DailyReminderImage/DailyReminderImage1.png" width="300"> 

## Notes
1. The checked states of the toggle buttons are saved in ```SharedPreferences```.
2. The notification IDs are passed into Intent extra so that the ```AlarmReceiver``` (which extends ```BroadcastReceiver```) 
would know which notification it should publish.
3. A ```Calender``` object that contains a specific time in a day (e.g. the time for morningCalender is set at 9 AM) is passed 
into the ```setInexactRepeating```method of the ```AlarmManager``` object with the inteveral being ```INTERVAL_DAY```defined in 
the ```AlarmManager``` class, so the notification will fire everyday at approximately the same time.
