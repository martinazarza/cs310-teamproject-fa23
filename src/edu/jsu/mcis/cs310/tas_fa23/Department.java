/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_fa23;

/**
 *
 * @author Johna
 * @author quint
 */
public class Department 
{    
    
    private final String description;
    
    private final int id, terminalID;
 
    public Department (int id, String description, int terminalID)
    {
        this.id = id;
        this.description = description;
        this.terminalID = terminalID;
    }
    
    /**
     *
     * @return - return's the id for the department
     */
    public int getID()
    {
        return id;
    }
    
    /**
     *
     * @return - return's the department's description
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     *
     * @return - returns the terminalID of the department
     */
    public int getTerminalID()
    {
        return terminalID;    
    }
    
    /**
     *
     * @return - return's the department's values as a string.
     */
   
    @Override
    public String toString()
    {
        StringBuilder sBuilder = new StringBuilder(); 
        
        sBuilder.append('#').append(id).append(" ");
        sBuilder.append('(').append( description).append(')').append(", ");
        sBuilder.append("Terminal ID: ").append(terminalID);
        
        return sBuilder.toString();
    }
}