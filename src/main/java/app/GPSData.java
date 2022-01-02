package app;

import org.geotools.referencing.GeodeticCalculator;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GPSData {

    private String id;
    private String name;
    private long officeId;
    private float lat;
    private float lon;
    private long created_at;


    Map<String, Double> geoDistance(List<GPSData> gpaDataList) {
        final GeodeticCalculator calc = new GeodeticCalculator();
        final Point2D point1 = new Point2D.Double(this.lat, this.lon);
        calc.setStartingGeographicPoint(point1);
        Map<String, Double> distanceResult = new HashMap<>();
        gpaDataList.stream()
                .filter(gpsData -> gpsData.id != null && this.id != null && !this.id.equalsIgnoreCase(gpsData.id))
                .filter(gpsData -> new Date(System.currentTimeMillis()).after(new Date(gpsData.created_at + TimeUnit.MINUTES.toMillis(10))))
                .filter(gpsData -> !distanceResult.containsKey(this.id + "-" + gpsData.id) && !distanceResult.containsKey(gpsData.id + "-" + this.id) )
                .forEach(gpsData -> {
                    Point2D point2 = new Point2D.Double(gpsData.lat, gpsData.lon);
                    calc.setDestinationGeographicPoint(point2);
                    if (calc.getOrthodromicDistance() < 1 ) // check if the disctance is more than 1 meter
                        distanceResult.put(this.id + "-" + gpsData.id, calc.getOrthodromicDistance());
                }
        );
        return distanceResult;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(long officeId) {
        this.officeId = officeId;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
