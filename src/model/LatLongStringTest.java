package model;

import org.junit.Before;
import org.junit.Test;
import model.Location;

import static org.junit.Assert.assertEquals;

/**
 * Created by Kevin on 11/13/2016.
 */
public class LatLongStringTest {

    private Location location;

    @Before
    public void setup() {
        location = new Location(null, null, false);
    }

    @Test
    public void getLatLongStringTest() {
        location.setLatitude(10);
        location.setLongitude(10);
        assertEquals(location.getLatLongString(), "10.0*N 10.0*E");

        location.setLatitude(10);
        location.setLongitude(-10);
        assertEquals(location.getLatLongString(), "10.0*N -10.0*W");

        location.setLatitude(-10);
        location.setLongitude(10);
        assertEquals(location.getLatLongString(), "-10.0*S 10.0*E");

        location.setLatitude(-10);
        location.setLongitude(-10);
        assertEquals(location.getLatLongString(), "-10.0*S -10.0*W");
    }
}
