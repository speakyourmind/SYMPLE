package org.symfound.prediction.phrasefinder.query;

/**
 *
 * @author Javed Gangjee
 */
public class QueryScenario {

    private String name;
    private String determiner;
    private String type;

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getDeterminer() {
        return determiner;
    }

    /**
     *
     * @param determiner
     */
    public void setDeterminer(String determiner) {
        this.determiner = determiner;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return "Punctuation: Name=" + this.name + " Type=" + this.type + " Determiner=" + this.determiner;
    }

}
