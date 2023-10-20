/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_fa23;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
/**
 *
 * @author quint
 */
public class Shift {
    
     public Shift(HashMap<String, String> shiftrules) {
        this.id = Integer.parseInt(shiftrules.get("id"));
        this.description = (shiftrules.get("description"));
        this.shiftstart = LocalTime.parse(shiftrules.get("shiftstart"));
        this.shiftstop = LocalTime.parse(shiftrules.get("shiftstop"));
        this.lunchstart = LocalTime.parse(shiftrules.get("lunchstart"));
        this.lunchstop = LocalTime.parse(shiftrules.get("lunchstop"));
        this.roundinterval = Integer.parseInt(shiftrules.get("roundinterval"));
        this.graceperiod = Integer.parseInt(shiftrules.get("graceperiod"));
        this.dockpenalty = Integer.parseInt(shiftrules.get("dockpenalty"));
        this.lunchthreshold = Integer.parseInt(shiftrules.get("lunchthreshold"));

        this.shiftduration = Duration.between(shiftstart, shiftstop).toMinutes();
        this.lunchduration = Duration.between(lunchstart, lunchstop).toMinutes();
 
     }
    
    private int id;
    private String description;
    private LocalTime shiftstart, shiftstop, lunchstart, lunchstop;
    private int graceperiod, dockpenalty, roundinterval, lunchthreshold;
    private long lunchduration, shiftduration;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getShiftstart() {
        return shiftstart;
    }

    public void setShiftstart(LocalTime shiftstart) {
        this.shiftstart = shiftstart;
    }

    public LocalTime getShiftstop() {
        return shiftstop;
    }

    public void setShiftstop(LocalTime shiftstop) {
        this.shiftstop = shiftstop;
    }

    public LocalTime getLunchstart() {
        return lunchstart;
    }

    public void setLunchstart(LocalTime lunchstart) {
        this.lunchstart = lunchstart;
    }

    public LocalTime getLunchstop() {
        return lunchstop;
    }

    public void setLunchstop(LocalTime lunchstop) {
        this.lunchstop = lunchstop;
    }

    public int getRoundinterval() {
        return roundinterval;
    }

    public void setRoundinterval(int roundinterval) {
        this.roundinterval = roundinterval;
    }

    public int getGraceperiod() {
        return graceperiod;
    }

    public void setGraceperiod(int graceperiod) {
        this.graceperiod = graceperiod;
    }

    public int getDockpenalty() {
        return dockpenalty;
    }

    public void setDockpenalty(int dockpenalty) {
        this.dockpenalty = dockpenalty;
    }

    public long getLunchduration() {
        return lunchduration;
    }

    public void setLunchduration(long lunchduration) {
        this.lunchduration = lunchduration;
    }

    public long getShiftduration() {
        return shiftduration;
    }

    public void setShiftduration(long shiftduration) {
        this.shiftduration = shiftduration;
    }

    public int getLunchthreshold() {
        return lunchthreshold;
    }

    public void setLunchthreshold(int lunchthreshold) {
        this.lunchthreshold = lunchthreshold;
    }

    @Override
    public String toString() {
        return description  + ": " + shiftstart + " - " + shiftstop +
                " (" + shiftduration + " minutes); Lunch: " + lunchstart +
                " - " + lunchstop + " (" + lunchduration + " minutes)";
    }

}
