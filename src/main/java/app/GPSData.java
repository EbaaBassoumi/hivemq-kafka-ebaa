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
                    if (calc.getOrthodromicDistance() > 3) // check if the disctance is more than 3 meters
                        distanceResult.put(this.id + "-" + gpsData.id, calc.getOrthodromicDistance());
                }
        );
        return distanceResult;
    }
}
