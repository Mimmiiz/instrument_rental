package se.kth.iv1351.dto;

import java.sql.Date;

/**
 * This class is a DTO that stores rental data. 
 */
public class RentalDTO {
    private final int rentalID;
    private final Date rentalStartDate;
    private final Date rentalReturnDate;
    private final RentalInstrumentDTO rentalInstrument;
    private final InstrumentDescriptionDTO instrumentDescription;
    private final int studentID;
    
    /**
     * Creates a new instance.
     * @param rentalID the rental ID.
     * @param rentalStartDate the rental start date.
     * @param rentalReturnDate the rental return date.
     * @param rentalInstrument the rental instrument.
     * @param instrumentDescription the rental instrument description.
     * @param studentID the student ID.
     */
    public RentalDTO(int rentalID, Date rentalStartDate, Date rentalReturnDate, RentalInstrumentDTO rentalInstrument, 
            InstrumentDescriptionDTO instrumentDescription, int studentID) {
        this.rentalID = rentalID;
        this.rentalStartDate = rentalStartDate;
        this.rentalReturnDate = rentalReturnDate;
        this.rentalInstrument = rentalInstrument;
        this.instrumentDescription = instrumentDescription;
        this.studentID = studentID;
    }
    
    /**
     * @return the rental ID.
     */
    public int getRentalID() {
        return this.rentalID;
    }
    
    /**
     * @return the rental start date.
     */
    public Date getRentalStartDate() {
        return new Date(this.rentalStartDate.getTime());
    }
    
    /**
     * @return the rental return date.
     */
    public Date getRentalReturnDate() {
        return new Date(this.rentalReturnDate.getTime());
    }
    
    /**
     * @return the rental instrument DTO.
     */
    public RentalInstrumentDTO getRentalInstrument() {
        return this.rentalInstrument;
    }
    
    /**
     * @return the rental instrument description DTO.
     */
    public InstrumentDescriptionDTO getInstrumentDescription() {
        return this.instrumentDescription;
    }
    
    /**
     * @return the student ID.
     */
    public int getStudentID() {
        return this.studentID;
    }
}


