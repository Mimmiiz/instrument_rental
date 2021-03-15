package se.kth.iv1351.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import se.kth.iv1351.dbhandler.DBHandler;
import se.kth.iv1351.dto.InstrumentDescriptionDTO;
import se.kth.iv1351.dto.RentalDTO;
import se.kth.iv1351.dto.RentalInstrumentDTO;


/**
 * This class represents an instrument rental that has methods that 
 * takes care of renting instruments, viewing rented instruments and
 * terminating rentals.
 */
public class InstrumentRental {
    private Student student;
    private ArrayList<RentalInstrumentDescription> instrumentsAvailableToRent;
    private ArrayList<Rental> rentals;
    
    /**
     * Creates a new instance.
     * @param firstName the first name of the student of this instrument rental.
     * @param lastName the last name of the student.
     * @param personNumber the person number of the student.
     * @param dbHandler the database handler to get access to the database.
     * @throws SQLException if the transaction fails.
     */
    public InstrumentRental(String firstName, String lastName, String personNumber, DBHandler dbHandler) throws SQLException {
        int studentID = dbHandler.getStudentID(personNumber);
        if(studentID == 0) {
            throw new NoSuchElementException("No student with the person number " + personNumber + " exists.");
        }
        this.student = new Student(studentID, firstName, lastName, personNumber);
    }
    
    /**
     * Returns a string representation of the instruments that are available to rent.
     * @param dbHandler the database handler.
     * @return a String representation of the instruments available to rent.
     * @throws SQLException if the transaction fails.
     */
    public String getInstrumentsAvailableToRent(DBHandler dbHandler) throws SQLException {
        updateInstrumentsAvailableToRent(dbHandler);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < instrumentsAvailableToRent.size(); i++) {
            sb.append(i + 1);
            sb.append(". " + instrumentsAvailableToRent.get(i).toString() + "\n");
        }
        return sb.toString();
    }
    
    /**
     * Rents the specified instrument from the database for
     * the desired number of months. After renting an instrument, the ArrayLists
     * that contain the instruments available to rent and rentals are updated.
     * @param instrumentToRent specifies which instr to rent.
     * @param monthsToRent specifies the number of months to rent, has to be between 1-6.
     * @param dbHandler the DH handler to get access to the database.
     * @throws SQLException if the transaction fails.
     */
    public void rentInstrument(int instrumentToRent, int monthsToRent, DBHandler dbHandler) throws SQLException, Exception {
        if(monthsToRent > 6 || monthsToRent < 1) {
            throw new IllegalArgumentException("The rental period has to be between 1-6 months. "
                    + "The entered value is: " + monthsToRent);
        }
        
        RentalInstrumentDescription instrumentDescription = instrumentsAvailableToRent.get(instrumentToRent - 1);
        dbHandler.rentInstrument(instrumentDescription.getID(), student.getPersonNumber(), monthsToRent);
        updateInstrumentsAvailableToRent(dbHandler);
        updateRentals(dbHandler);
    }
    
    /**
     * Gets the current rentals that the student has. If the student has no rentals
     * and enmpty string is returned.
     * @param dbHandler the DBHandler.
     * @return a String representation of the current rentals.
     * @throws SQLException if the transaction fails. 
     */
    public String getCurrentRentals(DBHandler dbHandler) throws SQLException {
        if(this.rentals == null)
            updateRentals(dbHandler);
        
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < rentals.size(); i++) {
            sb.append(i + 1);
            sb.append(". " + rentals.get(i).toString() + "\n");
        }
        return sb.toString();
    }
    
    /**
     * Terminates an ongoing rental.
     * @param rentalToTerminate the rental to terminate.
     * @param dbHandler the DB handler.
     * @throws SQLException if the transaction fails.
     */
    public void terminateRental(int rentalToTerminate, DBHandler dbHandler) throws SQLException {
        int rentalID = rentals.get(rentalToTerminate - 1).getRentalID();
        int instrumentID = rentals.get(rentalToTerminate - 1).getRentalInstrument().getID();
        int instrumentDescriptionID = rentals.get(rentalToTerminate - 1).getRentalInstrument()
                .getRentalInstrumentDescription().getID();
                        
        dbHandler.terminateRental(rentalID, instrumentID, instrumentDescriptionID);
        updateRentals(dbHandler);
    }
    
    private void updateInstrumentsAvailableToRent(DBHandler dbHandler) throws SQLException {
        ArrayList<InstrumentDescriptionDTO> list = dbHandler.getInstrumentDescriptions();
        this.instrumentsAvailableToRent = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            this.instrumentsAvailableToRent.add(new RentalInstrumentDescription(
                list.get(i).getID(), list.get(i).getName(), list.get(i).getInstrumentType(),
                list.get(i).getBrand(), list.get(i).getRentalPrice()));
        }
    }
    
    private void updateRentals(DBHandler dbHandler) throws SQLException {
        ArrayList<RentalDTO> list = dbHandler.getRentals(student.getPersonNumber());
        this.rentals = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            InstrumentDescriptionDTO desc = list.get(i).getInstrumentDescription();
            RentalInstrumentDescription instrumentDescription = new RentalInstrumentDescription(
                desc.getID(), desc.getName(), desc.getInstrumentType(), desc.getBrand(), desc.getRentalPrice());
            
            RentalInstrumentDTO instr = list.get(i).getRentalInstrument();
            RentalInstrument rentalInstrument = new RentalInstrument(instr.getID(), instr.getRentalInstrumentID(), 
                instrumentDescription);
            
            this.rentals.add(new Rental(list.get(i).getRentalID(), list.get(i).getRentalStartDate(),
                list.get(i).getRentalReturnDate(), rentalInstrument));
        }
    }
}