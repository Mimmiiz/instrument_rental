package se.kth.iv1351.model;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * This class represens a rental in the instrument rental and contains rental
 * information and can be printed out as a string representation.
 */
class Rental {
    private int rentalID;
    private Date rentalStartDate;
    private Date rentalReturnDate;
    private RentalInstrument instrument;
    
    Rental(int rentalID, Date rentalStartDate, Date rentalReturnDate, RentalInstrument instrument) {
        this.rentalID = rentalID;
        this.rentalStartDate = rentalStartDate;
        this.rentalReturnDate = rentalReturnDate;
        this.instrument = instrument;
    }
    
    /**
     * @return the rental ID.
     */
    int getRentalID() {
        return this.rentalID;
    }
    
    /**
     * @return the rental start date.
     */
    Date getRentalStartDate() {
        return this.rentalStartDate;
    }
    
    /** 
     * @return the rental return date.
     */
    Date getRentalReturnDate() {
        return this.rentalReturnDate;
    }
    
    /**
     * @return the rental instrument.
     */
    RentalInstrument getRentalInstrument() {
        return new RentalInstrument(instrument);
    }
    
    /**
     * @return a string representation of a rental.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
        sb.append(this.instrument.toString());
        sb.append(", Start date: " + formatter.format(this.rentalStartDate));
        sb.append(", Return date: " + formatter.format(this.rentalReturnDate));
        return sb.toString();
    }
}
