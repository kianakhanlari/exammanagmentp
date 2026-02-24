package ir.maktab.Exception;


public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String string) {
        super("Student not found");
    }
      }