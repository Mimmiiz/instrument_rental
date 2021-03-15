package se.kth.iv1351.model;

/**
 * This class represents a rental instrument description.
 */
class RentalInstrumentDescription {
    private int id;
    private String name;
    private String instrumentType;
    private String brand;
    private double rentalPrice;  
    
    /**
     * Creates a new instance.
     * @param id the rental instrument description ID.
     * @param name the name of the instrument.
     * @param instrumentType the type of instrument.
     * @param brand the brand of the instrument.
     * @param rentalPrice the monthly rental price.
     */
    RentalInstrumentDescription(int id, String name, String instrumentType,String brand, double rentalPrice) {
        this.id = id;
        this.name = name;
        this.instrumentType = instrumentType;
        this.brand = brand;
        this.rentalPrice = rentalPrice;
    }
    
    /**
     * Creates a new instance (copy) from an existing RentalInstrumentDescription.
     * @param rentalInstrumentDescription the RentalInstrumentDescription to copy.
     */
    RentalInstrumentDescription(RentalInstrumentDescription rentalInstrumentDescription) {
        this.id = rentalInstrumentDescription.getID();
        this.name = rentalInstrumentDescription.getName();
        this.instrumentType = rentalInstrumentDescription.getInstrumentType();
        this.brand = rentalInstrumentDescription.getBrand();
        this.rentalPrice = rentalInstrumentDescription.getRentalPrice();
    }
    
    /**
     * @return the ID.
     */
    int getID() {
        return this.id;
    }
    
    
    /**
     * @return the name.
     */
    String getName() {
        return this.name;
    }
    
    /**
     * @return the instrument type.
     */
    String getInstrumentType() {
        return this.instrumentType;
    }
    
    /**
     * @return the brand.
     */
    String getBrand() {
        return this.brand;
    }
    
    /**
     * @return the rental price.
     */
    double getRentalPrice() {
        return this.rentalPrice;
    }
   
    /**
     * @return a String representation of the rental instrument description.
     */
    public String toString() {
        if(brand == null) {
            return "Name: " + name + ", Type: " + instrumentType + ", Brand: not available, Price: " + rentalPrice;
        }
        return "Name: " + name + ", Type: " + instrumentType + ", Brand: " + brand + ", Price: " + rentalPrice;
    }
}
