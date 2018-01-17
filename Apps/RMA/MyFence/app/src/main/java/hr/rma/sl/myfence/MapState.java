package hr.rma.sl.myfence;

import java.util.ArrayList;

/**
 * Created by Sandi on 25.3.2016..
 */

// Class used to store map data in shared preferences
public class MapState {
    private double target_latitude;
    private double target_longitude;
    private double fence_latitude;
    private double fence_longitude;
    private double radius_m;

    public MapState(double target_latitude, double target_longitude,
                    double fence_latitude, double fence_longitude, double radius_m){

        this.target_latitude = target_latitude;
        this.target_longitude = target_longitude;
        this.fence_latitude = fence_latitude;
        this.fence_longitude = fence_longitude;
        this.radius_m = radius_m;

    }

    public double get_target_latitude(){
        return this.target_latitude;
    }

    public double get_target_longitude(){
        return this.target_longitude;
    }

    public double get_fence_latitude(){
        return this.fence_latitude;
    }

    public double get_fence_longitude(){
        return this.fence_longitude;
    }

    public double get_radius_m(){
        return this.radius_m;
    }
}
