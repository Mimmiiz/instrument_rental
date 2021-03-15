package se.kth.iv1351.dto;

/**
 * This class is a DTO that stores rental instrument information.
 */
public class RentalInstrumentDTO {
    private final int id;
    private final String rentalInstrumentID;
    private final int instrumentDescriptionID;
    
    /**
     * Creates a new instance.
     * @param id the instrument ID used int the database.
     * @param rentalInstrumentID the physical instrument ID.
     * @param instrumentDescriptionID the instrument description ID.
     */
    public RentalInstrumentDTO(int id, String rentalInstrumentID, int instrumentDescriptionID) {
        this.id = id;
        this.rentalInstrumentID = rentalInstrumentID;
        this.instrumentDescriptionID = instrumentDescriptionID;
    }
    
    /**
     * @return the instrument ID that is used in the database.
     */
    public int getID() {
        return this.id;
    }
    
    /**
     * @return the physical instrument ID.
     */
    public String getRentalInstrumentID() {
        return this.rentalInstrumentID;
    }
    
    /**
     * @return the instrument description ID.
     */
    public int getInstrumentDescriptionID() {
        return this.instrumentDescriptionID;
    }
}
