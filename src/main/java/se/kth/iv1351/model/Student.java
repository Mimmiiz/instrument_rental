package se.kth.iv1351.model;

/**
 * This class represents a student.
 */
class Student {
    private int studentID;
    private String firstName;
    private String lastName;
    private String personNumber;
    
    /**
     * Creates a new instance.
     * @param firstName the first name of the student.
     * @param lastName the last name of the student.
     * @param personNumber the person number of the student.
     */
    Student(int studentID, String firstName, String lastName, String personNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personNumber = personNumber;
    }
    
    /** 
     * @return the student ID.
     */
    int getStudentID() {
        return this.studentID;
    }
    
    /**
     * @return the first name of the student.
     */
    String getFirstName() {
        return this.firstName;
    }
    
    /**
     * @return the last name of the student.
     */
    String getLastName() {
        return this.lastName;
    }
    
    /**
     * @return the person number of the student.
     */
    String getPersonNumber() {
        return this.personNumber;
    }
}
