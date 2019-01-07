package model;

/**
 * Created by dionisiatara on 10/11/16.
 */
@SuppressWarnings("DefaultFileTemplate")
public enum WaterType {
    Bottled ("Bottled", "BTL"),
    Well ("Well", "WLL"),
    Stream ("Stream", "STR"),
    Lake ("Lake", "LK"),
    Spring ("Spring", "SPR"),
    Other ("Other", "OTH");

    private final String name;
    private final String charRep;

    /**
     * Counstructor for WaterType enumeration
     * @param cName the full name for water type
     * @param cRep the abbreviation for the full name
     */
    WaterType(String cName, String cRep) {
        name = cName;
        charRep = cRep;
    }

    /**
     * returns the full string name
     * @return full name string
     */
    public String getName() {return name;}

    /**
     * returns the shortened char representation
     * @return char rep string
     */
    public String getCharRep() {return charRep;}

    /**
     * returns the shortened char rep
     * @return char rep string
     */
    public String toString() {return name;}
}
