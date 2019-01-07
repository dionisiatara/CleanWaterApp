package model;

/**
 * Created by dionisiatara on 10/11/16.
 */
@SuppressWarnings("DefaultFileTemplate")
public enum WaterCondition {
    Waste ("Waste", "WST"),
    TreatableClear ("Treatable-Clear", "TCL"),
    TreatableMuddy ("Treatable-Muddy", "TMD"),
    Potable ("Potable", "PTB");

    private final String name;
    private final String charRep;

    /**
     * Constructor for WaterCondition enumeration
     * @param cName the full name of water condition
     * @param cRep the abbreviation for full name
     */
    WaterCondition(String cName, String cRep) {
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
