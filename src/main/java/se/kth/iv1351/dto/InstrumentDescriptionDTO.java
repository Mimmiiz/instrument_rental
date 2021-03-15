package se.kth.iv1351.dto;

/**
 * This class is a DTO of the instrument description which contains instrument
 * description data such as the instrument name, type, brand, rental price
 * and instrument description ID.
 */
public class InstrumentDescriptionDTO {
    private final String name;
    private final String instrumentType;
    private final String brand;
    private final double rentalPrice;
    private final int id;
    
    /**
     * Creates a new instance.
     * @param name the name of the instrument.
     * @param instrumentType the type of the instrument.
     * @param brand the brand.
     * @param rentalPrice the rental price per month.
     * @param id the sintrument description ID.
     */
    public InstrumentDescriptionDTO(String name, String instrumentType, String brand, double rentalPrice, int id) {
            this.name = name;
            this.instrumentType = instrumentType;
            this.brand = brand;
            this.rentalPrice = rentalPrice;
            this.id = id;
    }
    
    /**
     * @return the name of the instrument.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @return the instrument type, eg. string, percussion, etc.
     */
    public String getInstrumentType() {
        return this.instrumentType;
    }
    
    /**
     * @return the brand of the instrument.
     */
    public String getBrand() {
        return this.brand;
    }
    
    /**
     * @return the rental price of the instrument.
     */
    public double getRentalPrice() {
        return this.rentalPrice;
    }
    
    /**
     * @return the ID of the instrument description.
     */
    public int getID() {
        return this.id;
    }
}
