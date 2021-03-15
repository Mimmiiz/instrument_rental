package se.kth.iv1351.view;

import java.sql.SQLException;
import java.util.Scanner;
import se.kth.iv1351.controller.Controller;

/**
 * This class represents the view of the application. This is not a real view,
 * the output is only displayed in the terminal window.
 */
public class View {
    private final Controller controller;
    
    public View(Controller controller) {
        this.controller = controller;
    }
    
    public void runExecution() throws SQLException {
     
        Scanner in = new Scanner(System.in);
        int userChoice = -1;
        while(userChoice != 3) {
            if(userChoice == -1) {
                System.out.println("Welcome to the instrument rental.");
                System.out.println("Please enter your personal information.");
                System.out.println("First name: ");
                String firstName = in.nextLine();
                System.out.println("Last name: ");
                String lastName = in.nextLine();
                System.out.println("Person number (YYYYMMDDXXXX): ");
                String personNumber = in.nextLine();
               
                controller.startInstrumentRental(firstName, lastName, personNumber);
               
                userChoice = 0;
            }
            else if(userChoice == 0) {
                System.out.println("Please choose an option between 1-3");
                System.out.println("1. View available instruments to rent");
                System.out.println("2. View your current rentals");
                System.out.println("3. Quit");
                userChoice = in.nextInt();
            }
            
            else if(userChoice == 1) {
                System.out.println("Instruments available for rental: ");
                System.out.println(controller.viewRentableInstruments());
                System.out.println("Enter 1 to rent an instrument.");
                System.out.println("Enter 0 to return.");
                int input = in.nextInt();
                if(input == 0)
                    userChoice = 0;
                else if(input == 1) {
                    System.out.println("Enter the number of the instrument you want to rent: ");
                    int instrumentToRent = in.nextInt();
                    System.out.println("Enter number of months you want to rent the instrument, "
                            + "the minimum is 1 month and maximum is 6 months: ");
                    int monthsToRent = in.nextInt();
                    try {
                        controller.rentInstrument(instrumentToRent, monthsToRent);
                         System.out.println("The specified instrument has been rented for " + monthsToRent + " month(s).");
                    } catch(Exception ex) {
                        System.out.println("Could not rent instrument: " + ex.getMessage());
                        userChoice = 1;
                    }
                }
                else 
                    userChoice = 1;                               
            }
            
            else if(userChoice == 2) {
                String currentRentals = controller.viewCurrentRentals();
                if(currentRentals.equals("")) {
                    System.out.println("You have no current rentals.");
                    System.out.println("Enter 0 to return.");
                    if(in.nextInt() == 0)
                        userChoice = 0;
                    else
                        userChoice = 2;       
                }
                else {
                    System.out.println("Your current rentals: ");
                    System.out.println(currentRentals);
                    System.out.println("Enter 1 to terminate a rental.");
                    System.out.println("Enter 0 to return.");
                    int input = in.nextInt();
                    if(input == 0)
                        userChoice = 0;
                    else if(input == 1) {
                        System.out.println("Enter the number of the rental to terminate.");
                        int rentalToTerminate = in.nextInt();
                        controller.terminateRental(rentalToTerminate);
                        System.out.println("The rental has been terminated.");
                    }
                    else 
                        userChoice = 2;
                }
            }
            else {
                System.out.println("Please enter a valid number.");
                userChoice = 0;
            }
        }
    }  
}
