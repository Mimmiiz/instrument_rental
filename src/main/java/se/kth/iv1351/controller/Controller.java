package se.kth.iv1351.controller;

import java.sql.SQLException;
import se.kth.iv1351.dbhandler.DBHandler;
import se.kth.iv1351.model.InstrumentRental;

/**
 * This class represents the controller which takes care of all calls to the model layer.
 */
public class Controller {
    private InstrumentRental instrumentRental;
    private final DBHandler dbHandler;
    
    /**
     * Creates a new instance of the controller.
     * @param dbHandler the database handler which allows for database transactions.
     */
    public Controller(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }
    
    /**
     * This method initiates an instrument rental for a student with the given
     * first name, last name and person number.
     * @param firstName the first name of the student.
     * @param lastName the last name of the student.
     * @param personNumber the person number of the student.
     * @throws SQLException of the transaction fails.
     */
    public void startInstrumentRental(String firstName, String lastName, String personNumber) throws SQLException {
        this.instrumentRental = new InstrumentRental(firstName, lastName, personNumber, this.dbHandler);
    }
    
    /**
     * Views the rentable instruments by getting a String representation of the rentable instruments.
     * @return a String representation of the rentable instruents.
     * @throws SQLException if the transaction fails.
     */
    public String viewRentableInstruments() throws SQLException {
        return(this.instrumentRental.getInstrumentsAvailableToRent(this.dbHandler));
    }
    
    /**
     * Rents an instrument for the specified number of months.
     * @param instrumentToRent the instrument to rent.
     * @param monthsToRent the number of months to rent the instrument.
     * @throws SQLException if the transaction fails.
     * @throws Exception if the rental can not be performed.
     */
    public void rentInstrument(int instrumentToRent, int monthsToRent) throws SQLException, Exception {
        this.instrumentRental.rentInstrument(instrumentToRent, monthsToRent, this.dbHandler);
    }
    
    /**
     * Views the current rentals that the student has.
     * @return a string representaiton of the current rentals.
     * @throws SQLException if the trasaction fails.
     */
    public String viewCurrentRentals() throws SQLException {
        return(this.instrumentRental.getCurrentRentals(this.dbHandler));
    }
    
    /**
     * Terminates a specified rental.
     * @param rentalToTerminate the rental to terminate.
     * @throws SQLException if the transaction fails.
     */
    public void terminateRental(int rentalToTerminate) throws SQLException {
        this.instrumentRental.terminateRental(rentalToTerminate, this.dbHandler);
    }
}
