package XYC;

import java.util.Date;

public class Statistic {

	private static Date baseTime = new Date(2015, 11, 6, 0, 0, 0);

	public static double Fun_Rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static double getCosValue(double firstPLng, double firstPLat,
			double secondPLng, double secondPLat, double thirdPLng,
			double thirdPLat)

	{
		double a = Fun_GetDistance(firstPLat, firstPLng, secondPLat, secondPLng);
		double b = Fun_GetDistance(thirdPLat, thirdPLng, secondPLat, secondPLng);
		double c = Fun_GetDistance(firstPLat, firstPLng, thirdPLat, thirdPLng);
		return (a * a + b * b - c * c) / (2 * a * b);
	}

	public static double Fun_GetDistance(double lat1, double lng1, double lat2,
			double lng2) {
		double d_EarthRadius = 6378.137;
		double radLat1 = Fun_Rad(lat1);
		double radLat2 = Fun_Rad(lat2);
		double radLat = Fun_Rad(lat1) - Fun_Rad(lat2);
		double radLng = Fun_Rad(lng1) - Fun_Rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(radLat / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(radLng / 2), 2)));
		s = s * d_EarthRadius;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	public static int getTimeInterval(String dateTimeStart, String dateTimeEnd) {
		String[] timeA = dateTimeStart.split(" ")[1].split(":");
		String[] timeB = dateTimeEnd.split(" ")[1].split(":");

		int iTimeA = Integer.parseInt(timeA[0]) * 3600
				+ Integer.parseInt(timeA[1]) * 60 + Integer.parseInt(timeA[2]);
		int iTimeB = Integer.parseInt(timeB[0]) * 3600
				+ Integer.parseInt(timeB[1]) * 60 + Integer.parseInt(timeB[2]);

		return iTimeB - iTimeA;
	}

	public static String getStringTime(int timeInt) {
		long curTime = baseTime.getTime() + timeInt * 1000;
		Date curDate = new Date(curTime);
		int year = curDate.getYear();
		int month = curDate.getMonth();
		int day = curDate.getDay();
		day++;

		int hour = curDate.getHours();
		int minute = curDate.getMinutes();
		int second = curDate.getSeconds();
		return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":"
				+ second;
	}

	public static int getIntTime(String dateTimeString) {
		/*
		 * String[] sTime; int iTime; if(dateTimeString.contains(" "))
		 * sTime=dateTimeString.split(" ")[1].split(":"); else
		 * sTime=dateTimeString.split(":");
		 * iTime=Integer.parseInt(sTime[0])*3600
		 * +Integer.parseInt(sTime[1])*60+Integer.parseInt(sTime[2]); return
		 * iTime;
		 */
		String[] sTime = dateTimeString.split(" ");
		String[] date = sTime[0].split("-");
		String[] time = sTime[1].split(":");
		int year = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int day = Integer.parseInt(date[2]);
		int hour = Integer.parseInt(time[0]);
		int minute = Integer.parseInt(time[1]);
		int second = Integer.parseInt(time[2]);
		Date curDate = new Date(year, month, day, hour, minute, second);
		long diffDate = (curDate.getTime() - baseTime.getTime()) / 1000;
		return (int) diffDate;
	}

	public static String getAddedTime(String baseTime, int timeInterval) {
		String[] dateTime = baseTime.split(" ");
		String[] time = dateTime[1].split(":");
		int curTime = Integer.parseInt(time[0]) * 3600
				+ Integer.parseInt(time[1]) * 60 + Integer.parseInt(time[2]);
		int addedTime = curTime + timeInterval;

		int hour = addedTime / 3600;
		int minute = (addedTime - hour * 3600) / 60;
		int second = addedTime - hour * 3600 - minute * 60;

		return dateTime[0] + " " + Integer.toString(hour) + ":"
				+ Integer.toString(minute) + ":" + Integer.toString(second);

	}

	public static String getMinusTime(String baseTime, int timeInterval) {
		String[] dateTime = baseTime.split(" ");
		String[] time = dateTime[1].split(":");
		int curTime = Integer.parseInt(time[0]) * 3600
				+ Integer.parseInt(time[1]) * 60 + Integer.parseInt(time[2]);
		int minusTime = curTime - timeInterval;

		int hour = minusTime / 3600;
		int minute = (minusTime - hour * 3600) / 60;
		int second = minusTime - hour * 3600 - minute * 60;

		return dateTime[0] + " " + Integer.toString(hour) + ":"
				+ Integer.toString(minute) + ":" + Integer.toString(second);

	}
}
