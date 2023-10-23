/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_fa23;

/**
 *
 * @author Johna
 */
public class Department {
    private int id;
    private String desc;
    private int terminalID;
    
    public Department(int id, String desc, int terminalID){
        this.id = id;
        this.desc = desc;
        this.terminalID = terminalID;
    }

    public Department(int departmentid, int terminalid, String desc) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public int getID() {
         return id;
    }
    public String getDesc() {
        return desc;
    }
    public int getterminalID() {
        return terminalID;
    }
    @Override 
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", desc='" + desc + '\'' +
                ", terminalID" + terminalID +
                '}';
                
    }
}
