package model;


import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.lynden.gmapsfx.javascript.object.LatLong;
import java.util.concurrent.TimeUnit;

/**
 * Created by twalker61 on 10/18/16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Location {

    private double longitude;
    private double latitude;
    private String description;
    private String name;
    //private LatLong latLong;
    private String city;
    private String state;
    private String country;

    /**
     * initialize location, set description and name, set latitude/longitude
     * @param desc water type and water condition of location
     * @param n name of location
     * @param setLatLong if latLong property should be set
     */
    public Location(String n, String desc, boolean setLatLong) {
        description = desc;
        name = n;
        //latLong = new LatLong(0,0);
        if (setLatLong) {
            createLatLong(name);
        }
    }

    /**
     * initializes location using only latitude and longitude coordinates
     * @param description name of description
     * @param latLong latLong of location
     */
    public Location(String description, LatLong latLong) {
        this.description = description;
        longitude = latLong.getLongitude();
        latitude = latLong.getLatitude();
        //this.latLong = latLong;
        setName(new LatLng(latLong.getLatitude(), latLong.getLongitude()));
    }

    /**
     * initializes location using only latitude and longitude coordinates with no description
     * @param latLong latLong of location
     */
    public Location(LatLong latLong) {
        longitude = latLong.getLongitude();
        latitude = latLong.getLatitude();
        setName(new LatLng(latLong.getLatitude(), latLong.getLongitude()));
    }

    /**
     * sets new latitude and longitude coordinates and name of location
     * @param loc new string of location
     */
    public void setLatLong(String loc) {
        name = loc;
        createLatLong(loc);
    }

    /**
     * sets new latitude and longitude coordinates and name of location
     * @param latLong new latLong of location
     */
    public void setLatLong(LatLong latLong) {
        latitude = latLong.getLatitude();
        longitude = latLong.getLongitude();
        setName(new LatLng(latLong.getLatitude(), latLong.getLongitude()));
    }

    /**
     * Calls Google API to obtain longitude and latitude from address string name
     */
    private void createLatLong(String name) {
        long connectionTime = 60L;
        GeoApiContext context = new GeoApiContext();
        context = context.setApiKey("AIzaSyBGCSUhS73bdmKgWHaSRRMbICVYsOP3qn4")
                    .setConnectTimeout(connectionTime, TimeUnit.SECONDS)
                    .setReadTimeout(connectionTime, TimeUnit.SECONDS)
                    .setWriteTimeout(connectionTime, TimeUnit.SECONDS);
        GeocodingApiRequest request = GeocodingApi.geocode(context, name);
        try {
            GeocodingResult result = request.await()[0];
            LatLng loc = result.geometry.location;
            latitude = loc.lat;
            longitude = loc.lng;
            if (result.addressComponents.length == 4) {
                city = result.addressComponents[0].longName;
                state = result.addressComponents[2].shortName;
                country = result.addressComponents[3].shortName;
            } else {
                city = result.addressComponents[2].longName;
                state = result.addressComponents[4].shortName;
                country = result.addressComponents[5].shortName;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls Google API to obtain name of address from latitude and longitude
     */
    private void setName(LatLng l) {
        long connectionTime = 60L;
        GeoApiContext context = new GeoApiContext();
        context = context.setApiKey("AIzaSyBGCSUhS73bdmKgWHaSRRMbICVYsOP3qn4")
                .setConnectTimeout(connectionTime, TimeUnit.SECONDS)
                .setReadTimeout(connectionTime, TimeUnit.SECONDS)
                .setWriteTimeout(connectionTime, TimeUnit.SECONDS);
        GeocodingApiRequest request = GeocodingApi.reverseGeocode(context, l);
        try {
            GeocodingResult result = request.await()[0];
            name = result.formattedAddress;
            city = result.addressComponents[2].longName;
            state = result.addressComponents[4].shortName;
            country = result.addressComponents[5].shortName;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * set city of location
     * @param city new city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * set state of location
     * @param state new state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * set country of location
     * @param country new country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * return city of location
     * @return city The city location
     */
    private String getCity() {
        return city;
    }

    /**
     * return state of location
     * @return state The state location
     */
    private String getState() {
        return state;
    }

    /**
     * return country of location
     * @return country The country location
     */
    private String getCountry() {
        return country;
    }

    /**
     * return description of location
     * @return description The water type and condition of location
     */
    public String getDescription() {
        return description;
    }

    /**
     * return name of location
     * @return name The string name of the location
     */
    public String getName() {
        return name;
    }

    /**
     * return latitude of location
     * @return latitude The latitude of location
     */
    public double getLat() {
        return latitude;
    }

    /**
     * return longitude of location
     * @return latitude The latitude of location
     */
    public double getLong() {
        return longitude;
    }

    /**
     * Creates string representation of latitude and longitude
     * @return latitude longitude string
     */
    public String getLatLongString() {
        String locText = "";
        if (latitude > 0) {
            locText += (Math.floor(latitude * 100) / 100) + "*N ";
        } else {
            locText += (Math.floor(latitude * 100) / 100) + "*S ";
        }
        if (longitude > 0) {
            locText += (Math.floor(longitude * 100) / 100) + "*E";
        } else {
            locText += (Math.floor(longitude * 100) / 100) + "*W";
        }
        return locText;
    }

    /**
     * changes name of location
     * @param name new name of location
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * changes description of location
     * @param description new location information
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets latitude
     * @param latitude the new latitude
     */
    public void setLatitude(double latitude) { this.latitude = latitude; }

    /**
     * Sets longitude
     * @param longitude the new longitude
     */
    public void setLongitude(double longitude) { this.longitude = longitude; }

    /**
     * gives the string representation of the location
     * @return location in (City, State, Country) format
     */
    public String toString() {
        return getDescription()+ ", " + getName() + ", " + getCity() + ", " + getState() + ", " + getCountry();
    }
}
