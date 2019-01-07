package model;

/**
 * Created by taiga on 10/24/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public enum OverallCondition {
    Safe ("Safe"),
    Treatable ("Treatable"),
    Unsafe ("Unsafe");

    private final String name;

    OverallCondition(String name) {
        this.name = name;
    }

    /**
     * returns name of overall condition
     * @return condition of water overall
     */
    public String getName() {
        return name;
    }

    /**
     * simply returns condition for toString method
     * @return overall condition of water as string
     */
    @Override
    public String toString() {
        return name;
    }
}
