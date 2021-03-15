package se.kth.iv1351.model;

/**
 * This class represents a rental instrument.
 */
class RentalInstrument {
    private int id;
    private String rentalInstrumentID;
    private RentalInstrumentDescription rentalInstrumentDescription; 
    
    /**
     * Creates a new instance.
     * @param id the rental instrument id (used in the database).
     * @param rentalInstrumentID the physical rental insturment ID (used in reality).
     * @param rentalInstrumentDescription the rental instrument description for this instrument.
     */
    RentalInstrument(int id, String rentalInstrumentID, RentalInstrumentDescription rentalInstrumentDescription) {
        this.id = id;
        this.rentalInstrumentID = rentalInstrumentID;
        this.rentalInstrumentDescription = rentalInstrumentDescription;
    }
    
    /**
     * Creates a new instance (copy) of an already existing RentalInstrument.
     * @param rentalInstrument the RentalInstrument to copy.
     */
    RentalInstrument(RentalInstrument rentalInstrument) {
        this.id = rentalInstrument.getID();
        this.rentalInstrumentID = rentalInstrument.rentalInstrumentID;
        this.rentalInstrumentDescription = new RentalInstrumentDescription(rentalInstrument.getRentalInstrumentDescription());
    }
    
    /**
     * @return the rental instrument ID used in the database.
     */
    int getID() {
        return this.id;
    }
    
    /**
     * @return the physical rental instrument ID, used in reality.
     */
    int getRentalInstrumentID() {
        return this.getRentalInstrumentID();
    }
    
    /**
     * @return the rental instrument description of the instrument.
     */
    RentalInstrumentDescription getRentalInstrumentDescription() {
        return new RentalInstrumentDescription(this.rentalInstrumentDescription);
    }
    
    /**
     * @return a string representation of the rental Instrument
     */
    public String toString() {
        return "Instrument ID: " + this.rentalInstrumentID + ", " + rentalInstrumentDescription.toString();
    }
}
