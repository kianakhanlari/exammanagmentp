package ir.maktab.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ExamFileWriter {

    public void files(String text) {
        String path = "exam.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path,true))){
            writer.write(text);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

}
