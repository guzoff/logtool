package org.guzoff.logtool.logio;

/**
 * Helper POJO for passing grouping parameters between objects.
 * @author guzoff
 */
public class GrouperPojo {
    
    private boolean byUsername;
    private boolean byTimeUnit;
    private Units unit;

    public GrouperPojo() {
        this(false, false, Units.YEAR);
    }
    
    public GrouperPojo(boolean byUsername, boolean byTimeUnit, Units unit) {
        this.byUsername = byUsername;
        this.byTimeUnit = byTimeUnit;
        this.unit = unit;
    }

    public boolean isByUsername() {
        return byUsername;
    }

    public void setByUsername(boolean byUsername) {
        this.byUsername = byUsername;
    }

    public boolean isByTimeUnit() {
        return byTimeUnit;
    }

    public void setByTimeUnit(boolean byTimeUnit) {
        this.byTimeUnit = byTimeUnit;
    }

    public Units getUnit() {
        return unit;
    }

    public void setUnit(Units unit) {
        this.unit = unit;
    }

}
