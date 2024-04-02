import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {

    public List<StringBuilder> plainTextList;

    //stores all the elements from the "fileName" to "plainTextList"
    public ReadFile(String fileName) throws Exception {
        plainTextList = readFileContents(fileName);
    }

    //this method gets the content from the file
    public List<StringBuilder> getFileContent() {
        return plainTextList;
    }

    //readFileContents is reading the contents of a text file and returning those contents as a single string
    public static List<StringBuilder> readFileContents(String filename) {
        List<StringBuilder> fileContent = new ArrayList<>(); //StringBuilder stores the fileContent of the file
        //FileReader class is used to read character streams from a file
        //BufferedReader class is used to read text from a character-input stream
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            //this while loop checks if the end of the file is reached
            while ((line = reader.readLine()) != null) {
                fileContent.add(new StringBuilder(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    //write data to a file
    public static void writeToFile(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}