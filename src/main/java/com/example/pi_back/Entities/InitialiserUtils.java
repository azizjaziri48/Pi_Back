package com.example.pi_back.Entities;

import java.util.List;

public class InitialiserUtils {

    private InitialiserUtils() {
    }
//calculer la distance entre les differents point
    public static double getMinimumDistanceSquared(DataPoint point, List<DataPoint> centroidsAvailable) {

        double distance = Double.MAX_VALUE;

        for (int i = 0; i < centroidsAvailable.size(); i++) {

            double newDistance = point.distanceTo(centroidsAvailable.get(i));
            if (newDistance < distance) {
                distance = newDistance;
            }
        }

        return Math.pow(distance, 2.0);
    }
//retourne l'index de la distance minim entre les points
    public static int getIndexOfMinimumDistanceSquared(DataPoint point, List<DataPoint> centroidsAvailable) {

        double distance = Double.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < centroidsAvailable.size(); i++) {

            double newDistance = point.distanceTo(centroidsAvailable.get(i));
            if (newDistance < distance) {
                distance = newDistance;
                index = i;
            }
        }

        return index;
    }


}
