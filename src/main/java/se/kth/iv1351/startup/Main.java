package se.kth.iv1351.startup;


import se.kth.iv1351.controller.Controller;
import se.kth.iv1351.dbhandler.DBHandler;
import se.kth.iv1351.view.View;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Maria Halvarsson
 */

public class Main {

    /**
     * Starts the application.
     * 
     * @param args the command line arguments. This program does not take any command line arguments.
     */
    public static void main(String[] args) {
        try {
            DBHandler dbHandler = new DBHandler();
            System.out.println(dbHandler);
            Controller controller = new Controller(dbHandler);
            View view = new View(controller);
            view.runExecution();
        } catch(Exception ex) {
            System.out.println("The application could not start");
            ex.printStackTrace();
        }
    }
}
