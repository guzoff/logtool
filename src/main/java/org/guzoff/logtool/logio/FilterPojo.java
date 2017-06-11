package org.guzoff.logtool.logio;

import java.time.LocalDateTime;

/**
 * Helper POJO for passing filter parameters between objects.
 * @author guzoff
 */
public class FilterPojo {

    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String username;
    private String msgPattern;

    public FilterPojo() {
        this(LocalDateTime.MIN, LocalDateTime.MAX, "", "");
    }

    public FilterPojo(LocalDateTime periodStart, LocalDateTime periodEnd, 
                                    String username, String msgPattern) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.username = username;
        this.msgPattern = msgPattern;
    }

    public LocalDateTime getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDateTime periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDateTime getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDateTime periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsgPattern() {
        return msgPattern;
    }

    public void setMsgPattern(String msgPattern) {
        this.msgPattern = msgPattern;
    }
    
}
