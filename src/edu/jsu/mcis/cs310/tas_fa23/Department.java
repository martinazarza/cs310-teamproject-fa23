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
    private int departmentid;
    private String description;
    private int terminalID;
    
    public Department(int departmentid, String descpription, int terminalID){
        this.departmentid = departmentid;
        this.description = description;
        this.terminalID = terminalID;
    }

    public int getdepartmentid() {
         return departmentid;
    }
    
    public void setid(int departmentid){
        this.departmentid = departmentid;
    }
    
    public String getdescription() {
        return description;
    }
    
    public void setdescription(String description){
        this.description = description;
    }
    
            
    public int getterminalID() {
        return terminalID;
    }
    
    public void setterminalID(int terminalID){
        this.terminalID = terminalID;
    }
   
         
    @Override 
    public String toString() {
        return "#" + departmentid +
                " (" + description + "), " +
                "Terminal ID: " + terminalid;
    }
