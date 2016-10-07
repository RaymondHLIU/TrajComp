package RoadNetWork;

public class Point {

	public double lat;
	public double lng;

	public Point(String lat, String lng) {
		this.lat = Double.parseDouble(lat);
		this.lng = Double.parseDouble(lng);
	}

	@Override
	public boolean equals(Object obj) {
		Point obj_point = (Point) obj;
		if (this.lat == obj_point.lat && this.lng == obj_point.lng)
			return true;
		else
			return false;
	}

}
