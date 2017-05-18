# Pedometer_Android

This is an android app that is useful to calcualte your BMI and number of steps walked, distance covered, calories burned etc. It also keeps track of the location where you have started the workout and present location.

Code Description 

•	Home.java – Main class that depicts all the functionality of the application.
	It shows the steps, duration, speed, distance, calories and frequency. Result 	of the application is displayed in this class.
•	Userdata.java – User data class is used to get the data from the user and calculate the BMI. Details like height, weight, age and gender is received as input from the user and result is displayed on the screen.
•	Userprogress.java - User progress is another class used to show the progress of the user through a graphical representation.
•	GeoLocator.java – This class tracks the path of the user. Google map is used in this class. Also the latitude and longitude of the location is used to track the location path.
•	GPSActivity.java – This class gives the current location of the user using GPS. GPSTracker class is used.
•	GPSTracker.java – Extension class that supports the GPSActivity.java class.
•	ExampleUnitTest.java – Unit test case is used in this unit test framework.
