package model;


/**
 * Created by Taiga on 10/1/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public enum AccountType {
    User ("User", "USR"),
    Worker ("Worker", "WKR"),
    Manager ("Manager", "MGR"),
    Admin ("Admin","ADM");

    private final String name;
    private final String charRep;

    /**
     * Constructor for AccountType enumeration
     * @param cName the full name of type
     * @param cRep the abbreviation of type name
     */
    AccountType(String cName, String cRep) {
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
