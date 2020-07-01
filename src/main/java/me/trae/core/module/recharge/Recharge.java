package me.trae.core.module.recharge;

public class Recharge {

    private String ability = "";
    private long seconds;
    private long systime;
    private boolean inform;

    public Recharge(final String ability, final long seconds) {
        this.setAbility(ability);
        this.setSeconds(seconds);
        this.systime = System.currentTimeMillis();
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(final long seconds) {
        this.seconds = seconds;
    }

    public long getSystime() {
        return systime;
    }

    public void setSysTime(final long systime) {
        this.systime = systime;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(final String ability) {
        this.ability = ability;
    }

    public long getRemaining() {
        return (seconds + systime - System.currentTimeMillis());
    }

    public boolean isInform() {
        return inform;
    }

    public void setInform(final boolean inform) {
        this.inform = inform;
    }
}