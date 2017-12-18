package com.example.gabriela.firecastcommunity.helper;

/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/*::                                                                         :*/
/*::  This routine calculates the distance between two points (given the     :*/
/*::  latitude/longitude of those points). It is being used to calculate     :*/
/*::  the distance between two locations using GeoDataSource (TM) prodducts  :*/
/*::                                                                         :*/
/*::  Definitions:                                                           :*/
/*::    South latitudes are negative, east longitudes are positive           :*/
/*::                                                                         :*/
/*::  Passed to function:                                                    :*/
/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
/*::    unit = the unit you desire for results                               :*/
/*::           where: 'M' is statute miles (default)                         :*/
/*::                  'K' is kilometers                                      :*/
/*::                  'N' is nautical miles                                  :*/
/*::  Worldwide cities and other features databases with latitude longitude  :*/
/*::  are available at http://www.geodatasource.com                          :*/
/*::                                                                         :*/
/*::  For enquiries, please contact sales@geodatasource.com                  :*/
/*::                                                                         :*/
/*::  Official Web site: http://www.geodatasource.com                        :*/
/*::                                                                         :*/
/*::           GeoDataSource.com (C) All Rights Reserved 2015                :*/
/*::                                                                         :*/
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

import com.example.gabriela.firecastcommunity.domain.Occurrence;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

public class DistanceCalculator {
    public static Double distance(LatLng actualPosition, Occurrence occurrence) {
        if(actualPosition!=null) {
            if (occurrence.latitude != null && occurrence.longitude != null) {
                double lat1 = actualPosition.latitude;
                double lon1 = actualPosition.longitude;
                double lat2 = occurrence.latitude;
                double lon2 = occurrence.longitude;
                double theta = lon1 - lon2;
                double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
                dist = Math.acos(dist);
                dist = rad2deg(dist);
                dist = dist * 60 * 1.1515 * 1.609344;
                return (dist);
            }
        }
        return null;
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::    This function converts decimal degrees to radians           :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::    This function converts radians to decimal degrees           :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

//    double distance = SphericalUtil.computeDistanceBetween(posicaoInicial, posicaiFinal);
//    Log.i("LOG","A Distancia é = "+
//
//    formatNumber(distance));
//
//    private String formatNumber(double distance) {
//        String unit = "m";
//        if (distance 1000){
//            distance /= 1000;
//            unit = "km";
//        }
//
//        return String.format("%4.3f%s", distance, unit);
//    }

    public double distanceCalculator(LatLng actualPosition, Occurrence occurrence) {
        if(actualPosition!=null && occurrence!=null) {
            if (occurrence.latitude != null && occurrence.longitude != null) {
                double lat1 = actualPosition.latitude;
                double lon1 = actualPosition.longitude;
                double lat2 = occurrence.latitude;
                double lon2 = occurrence.longitude;
                LatLng posicaoInicial = new LatLng(lat1, lon1);
                LatLng posicaiFinal = new LatLng(lat2, lon2);
                return SphericalUtil.computeDistanceBetween(posicaoInicial, posicaiFinal);
            }
        }
            return -1;
    }
}