package hr.rma.sl.myfence;

/**
 * Created by Sandi on 24.3.2016..
 */

public final class Constants {

    private Constants() {
    }

    public static final String PACKAGE_NAME = "hr.rma.sl.myfence";
    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

    // Used to set an expiration time for a geofence.
    // After this amount of time Location Services stops tracking the geofence.
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

    // For this sample, geofences expire after twelve hours.
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
}