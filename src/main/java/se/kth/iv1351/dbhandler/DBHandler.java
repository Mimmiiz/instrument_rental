package se.kth.iv1351.dbhandler;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import se.kth.iv1351.dto.InstrumentDescriptionDTO;
import se.kth.iv1351.dto.RentalDTO;
import se.kth.iv1351.dto.RentalInstrumentDTO;

/**
 * This takes care of the transactions to the database.
 */
public class DBHandler {
    private final Connection conn;

    public DBHandler() {
        DBAccess dbAccess = new DBAccess();
        this.conn = dbAccess.getConnection();
    }
    
    /**
     * Receives the all rental instrument descriptions that have
     * instruments that are available for rental.
     * @return an ArrayList containing InstrumentDescriptionDTO.
     * @throws SQLException if the transaction fails.
     */
    public ArrayList<InstrumentDescriptionDTO> getInstrumentDescriptions() throws SQLException {
        String query = "SELECT * FROM rental_instrument_description "
                + "WHERE rental_instrument_description.quantity_available_for_rental != 0";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(query);
        ArrayList<InstrumentDescriptionDTO> list = new ArrayList<>();
        while(result.next()) {
            list.add(new InstrumentDescriptionDTO(result.getString("name"), result.getString("instrument_type"),
                result.getString("brand"), result.getDouble("rental_price"), result.getInt("id")));
        }
        return list;
    }
   
     /**
     * Rents the desired instrument that matches the given InstrumentDescriptionDTO for the
     * student that is given by the StudentDTO.
     * @param instrumentDescriptionID the instrumentDescription ID of the instrument to rent.
     * @param personNumber the person number of the student that is renting an instrument.
     * @param monthsToRent the rental time, has to be a value between 1-6.
     * @throws SQLException if the transaction fails.
     * @throws Exception if the maximum number of rentals is already reached.
     */
    public void rentInstrument(int instrumentDescriptionID, String personNumber, int monthsToRent) throws SQLException, Exception {
        if(!checkEligibleForRental(personNumber)) { 
            throw new Exception("The number of current rentals can not exceed 2.");
        }
        if(!checkIfInstrumentIsAvailable(instrumentDescriptionID)) {
            throw new Exception("The instrument is out of stock.");
        }
        
        conn.setAutoCommit(false);
        String query = "INSERT INTO instrument_rental(rental_start_date, rental_return_date, "
                + "school_id, rental_instrument_id, student_id) VALUES (?, ?, ?, ?, ?)";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, monthsToRent);

        try {
            int instrumentID = getRentalInstrumentID(instrumentDescriptionID);
            int studentID = getStudentID(personNumber);

            addRentedInstrument(instrumentID);
            rentInstrumentUpdateDescription(instrumentDescriptionID);

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDate(1, new Date(System.currentTimeMillis()));
            stmt.setDate(2, new Date(cal.getTimeInMillis()));
            stmt.setInt(3, 1);
            stmt.setInt(4, instrumentID);
            stmt.setInt(5, studentID);
            stmt.executeUpdate();
            stmt.close();
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            ex.printStackTrace();
        }
    }
    
    /**
     * Returns the rentals for the given person number.
     * @param personNumber the person number of the student.
     * @return a RentalDTO containing rental information.
     * @throws SQLException if the transaction fails.
     */
    public ArrayList<RentalDTO> getRentals(String personNumber) throws SQLException {
        int studentID = getStudentID(personNumber);
        String query = "SELECT * FROM instrument_rental "
                + "WHERE " + studentID + " = instrument_rental.student_id "
                + "AND rental_return_date > current_date";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(query);
        ArrayList<RentalDTO> rentals = new ArrayList<>();
        while(result.next()) {
            RentalInstrumentDTO rentalInstrument = getRentalInstrument(result.getInt("rental_instrument_id"));
            InstrumentDescriptionDTO description = getRentalInstrumentDescription(rentalInstrument.getInstrumentDescriptionID());
            
            rentals.add(new RentalDTO(result.getInt("rental_id"), result.getDate("rental_start_date"),
            result.getDate("rental_return_date"), rentalInstrument, description, result.getInt("student_id")));
        }
        return rentals;
    }
        
    /**
     * Terminates the rental with the matching rentalID. The method updates the database
     * by updating the rental instrument and rental instrument description tables.
     * When a rental is terminated it is not removed from the database, but instead,
     * the rental return date is updated to the current date.
     * @param rentalID the ID of the rental.
     * @param rentalInstrumentID the ID of the rental instrument.
     * @param rentalInstrumentDescriptionID the ID of the rental instrument description.
     * @throws SQLException if the transaction fails.
     */
    public void terminateRental(int rentalID, int rentalInstrumentID, int rentalInstrumentDescriptionID) throws SQLException {
        conn.setAutoCommit(false);
        
        java.util.Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
        
        String query = "UPDATE instrument_rental SET rental_return_date = '" + formatter.format(date)
                + "' WHERE rental_id = " + rentalID;
        
        try {
            returnRentedInstrument(rentalInstrumentID);
            returnInstrumentUpdateDescription(rentalInstrumentDescriptionID);
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        }
    }
    
    /**
     * Updates the rental status of the rental instrument that matches 
     * the given instrument ID to not rented by setting the bit to 0.
     * @param instrumentID the ID of the rental instrument.
     * @throws SQLException if the transaction fails.
     */
    private void returnRentedInstrument(int instrumentID) throws SQLException {
        conn.setAutoCommit(false);
        String query = "UPDATE rental_instrument SET currently_rented = '0' "
                + "WHERE rental_instrument.id = " + instrumentID;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        }
    }
    
    /**
     * Updates the rental status of the rental instrument that matches 
     * the given instrument ID to currently rented by setting the bit to 1.
     * @param instrumentID the ID of the rental instrument.
     * @throws SQLException if the transaction fails.
     */
    private void addRentedInstrument(int instrumentID) throws SQLException {
        conn.setAutoCommit(false);
        String query = "UPDATE rental_instrument SET currently_rented = '1' "
                + "WHERE rental_instrument.id = " + instrumentID;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            conn.commit();
        
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } 
    }
    
    /**
     * Updates the rental instrument description of the rented instrument by updating
     * the quantity available for rental and the quantity rented.
     * This query should be executed whenever a new instrument is being rented.
     * @param instrumentDescriptionID the rental instrument description ID of the rented instrument.
     * @throws SQLException if the transaction fails.
     * */
    private void rentInstrumentUpdateDescription(int instrumentDescriptionID) throws SQLException {
        conn.setAutoCommit(false);
        String query = "UPDATE rental_instrument_description "
                    + "SET quantity_rented = quantity_rented + 1, "
                    + "quantity_available_for_rental = quantity_available_for_rental - 1 "
                    + "WHERE rental_instrument_description.id = " + instrumentDescriptionID
                    + " AND rental_instrument_description.quantity_available_for_rental != 0";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            conn.commit();
        } catch(SQLException ex) {
            conn.rollback();
            throw ex;
        }
    }
    
    /**
     * Updates the rental instrument description of the returned rental instrument by
     * updated the quantiyt available for rental and the quantity rented.
     * This query should be executed whenever an instrument is being returned and the rental is terminated.
     * @param instrumentDescriptionID the rental instrument description ID of the instrument.
     * @throws SQLException if the transaction fails.
     */
    private void returnInstrumentUpdateDescription(int instrumentDescriptionID) throws SQLException {
        conn.setAutoCommit(false);
        String query = "UPDATE rental_instrument_description "
                + "SET quantity_rented = quantity_rented - 1, "
                + "quantity_available_for_rental = quantity_available_for_rental + 1 "
                + "WHERE rental_instrument_description.id = " + instrumentDescriptionID;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            conn.commit();
        } catch(SQLException ex) {
            conn.rollback();
            throw ex;
        }
    }
    
    /**
     * Checks if a student is eligible for renting an instrument.
     * @param personNumber the person number of the student.
     * @return true if the student is eligible for renting, else false.
     * @throws SQLException if the transaction fails.
     */
    private boolean checkEligibleForRental(String personNumber) throws SQLException {
        int studentID = getStudentID(personNumber);
        String query = "SELECT COUNT(*) < 2 AS eligible FROM instrument_rental "
                + "WHERE " + studentID + " = instrument_rental.student_id "
                + "AND rental_return_date > current_date";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(query);
        boolean eligible = false;
        while(result.next()) {
            eligible = result.getBoolean("eligible");
        }
        return eligible;
    }
    
    /**
     * Retreives the student ID from a given person number.
     * @param personNumber the person number.
     * @return the student ID.
     * @throws SQLException if the transaction fails.
     */
    public int getStudentID(String personNumber) throws SQLException {
        String query = "SELECT id from student "
                + "WHERE student.person_id = "
                + "(SELECT id FROM person WHERE person_number = '" + personNumber + "')";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(query);
        int studentID = 0;
        while(result.next()) {
            studentID = result.getInt("id");
        }
        return studentID;
    }
    
    /**
     * Returns the first avaiable instrument ID given an instrument description ID.
     * @param instrumentDescriptionID the given instrument description ID.
     * @return the instrument rental ID 
     * @throws SQLException if the transaction fails.
     */
    private int getRentalInstrumentID(int instrumentDescriptionID) throws SQLException {
        String query = "SELECT * FROM rental_instrument "
                + "WHERE rental_instrument_description_id = " + instrumentDescriptionID 
                + "AND (rental_instrument.currently_rented = '0' "
                + "OR rental_instrument.currently_rented IS NULL) limit 1";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(query);
        int instrumentID = 0;
        while(result.next()) {
            instrumentID = result.getInt("id");
        }
        return instrumentID;
    }
    
    /**
     * Returns the rental instrument that matches the given rental instrument ID.
     * @param rentalInstrumentID the given rental instrument ID.
     * @return a RentalInstrumentDTO containg rental instrument data.
     * @throws SQLException if the transaction fails.
     */
    private RentalInstrumentDTO getRentalInstrument(int rentalInstrumentID) throws SQLException {
        String query = "SELECT * FROM rental_instrument "
                + "WHERE rental_instrument.id = " + rentalInstrumentID;
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(query);
        RentalInstrumentDTO rentalInstrument = null;
        while(result.next()) {
            rentalInstrument = new RentalInstrumentDTO(result.getInt("id"), result.getString("instrument_id"),
                result.getInt("rental_instrument_description_id"));
        }
        return rentalInstrument;
    }
    
    /**
     * Returns a rental instrument description that matches the given rental instrument description.
     * @param instrumentDescriptionID the rental instrument description ID.
     * @return a InstrumentDescriptionDTO containing the rental instrument description data.
     * @throws SQLException if the transaction fails.
     */
    private InstrumentDescriptionDTO getRentalInstrumentDescription(int instrumentDescriptionID) throws SQLException {
        String query = "SELECT * FROM rental_instrument_description "
                + "WHERE id = " + instrumentDescriptionID;
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(query);
        InstrumentDescriptionDTO description = null;
        while(result.next()) {
            description = new InstrumentDescriptionDTO(result.getString("name"),
                result.getString("instrument_type"), result.getString("brand"),
                result.getDouble("rental_price"), result.getInt("id"));
        }
        return description;
    }
    
    private boolean checkIfInstrumentIsAvailable(int instrumentDescriptionID) throws SQLException {
        String query = "SELECT COUNT(*) > 0 AS available FROM rental_instrument_description "
                + "WHERE id = " + instrumentDescriptionID
                + " AND rental_instrument_description.quantity_available_for_rental > 0";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(query);
        boolean available = false;
        while(result.next()) {
            available = result.getBoolean("available");
        }
        return available;
    }
}
