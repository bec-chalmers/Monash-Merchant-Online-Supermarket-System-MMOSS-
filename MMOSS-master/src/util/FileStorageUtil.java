package util;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Utility class used to read and write to all the relevant csv files.
 * Does not focus on building entity objects, instead reads and writes raw data.
 * @author Yapa Jayasinghe
 * @version  1.0
 */
public class FileStorageUtil {

    /**
     * For reading CSV files which contain only one column.
     * Example - When reading the Categories.
     *
     * @param pathToSingleColumnFile the path to the CSV file with one column.
     * @return list of Strings - for one line.
     */
    public static List<String> readSingleColumnFile(String pathToSingleColumnFile) throws IOException
    {
        List<String> rawData = new ArrayList<String>();
        FileReader fileReader = null;
        Scanner fileScanner = null;

        try
        {
            fileReader = new FileReader(pathToSingleColumnFile);
            fileScanner = new Scanner(fileReader);
            if(fileScanner.hasNextLine())
            {
                fileScanner.nextLine();
            }
            while(fileScanner.hasNextLine())
            {
                String line = fileScanner.nextLine().trim();
                if(!line.isEmpty())
                {
                    rawData.add(line);
                }
            }
        }
        catch (IOException e)
        {
            // making sure the controller gets the exception occurred.
            throw new IOException("Error reading file : "+pathToSingleColumnFile,e);
        }
        finally
        {
            if(fileScanner != null){
                try {
                    fileScanner.close();
                }catch(Exception e){
                    throw new IOException("Error closing scanner for file : "+pathToSingleColumnFile,e);
                }
            }
            if(fileReader != null){
                try {
                    fileReader.close();
                }catch(Exception e){
                    throw new IOException("Error closing file : "+pathToSingleColumnFile,e);
                }
            }

        }
        return rawData;
    }

    /**
     * Reading CSV files which contain multiple columns.
     * Example - Products, Customers
     * @param pathToMultiColumnFile the path to the csv file containing multiple columns.
     * @return a list of rows - each row is a list of Strings
     */
    public static List<List<String>> readMultiColumnFile(String pathToMultiColumnFile) throws IOException
    {
        List<List<String>> rawData = new ArrayList<>();
        FileReader fileReader = null;
        Scanner fileScanner = null;

        try {
            fileReader = new FileReader(pathToMultiColumnFile);
            fileScanner = new Scanner(fileReader);
            if(fileScanner.hasNextLine())
            {
                fileScanner.nextLine(); // skipping the headers
            }

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                List<String> rowValues = new ArrayList<>();
                StringBuilder currentField = new StringBuilder();
                boolean insideQuotes = false;

                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);

                    if (c == '"') {
                        // Toggle quote mode
                        insideQuotes = !insideQuotes;
                    } else if (c == ',' && !insideQuotes) {
                        // Field boundary (only when not inside quotes)
                        rowValues.add(currentField.toString().trim());
                        currentField.setLength(0);
                    } else {
                        currentField.append(c);
                    }
                }
                // Add last field
                rowValues.add(currentField.toString().trim());

                // Clean quotes from around values and unescape double quotes
                for (int j = 0; j < rowValues.size(); j++) {
                    String value = rowValues.get(j);
                    if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
                        value = value.substring(1, value.length() - 1);
                    }
                    value = value.replace("\"\"", "\""); // unescape internal quotes
                    rowValues.set(j, value);
                }

                rawData.add(rowValues);
            }
        }
        catch(IOException e)
        {
            throw new IOException("Error reading file : "+pathToMultiColumnFile,e);
        }finally {
            if(fileScanner != null){
                try {
                    fileScanner.close();
                }catch(Exception e){
                    throw new IOException("Error closing scanner for file : "+pathToMultiColumnFile,e);
                }
            }

            if(fileReader != null){
                try {
                    fileReader.close();
                }catch(Exception e){
                    throw new IOException("Error closing reader for file : "+pathToMultiColumnFile,e);
                }
            }
        }
        return   rawData;
    }

    /**
     * This method is used to append a new row containing a product data.
     *
     * @param pathToFIle the path to the file to which the data should be written to.
     * @param lineForCSV String to be written as a row to the csv file.
     */
    public static void appendToFile(String pathToFIle, String lineForCSV) throws IOException
    {
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter(pathToFIle, true);
            fileWriter.write(System.lineSeparator());
            fileWriter.write(lineForCSV);
        }catch (IOException e)
        {
            throw new IOException("Error writing to file :"+pathToFIle);
        }
        finally
        {
            if (fileWriter != null)
            {
                try {
                    fileWriter.close();
                }
                catch (IOException e) {
                    throw new IOException("Error closing file writer : "+pathToFIle,e);
                }
            }
        }
    }

    /**
     * This method rewrites the whole csv file with the updated in memory product list.
     *
     * @param pathToFIle  String path to the csv file to which the data is being written to.
     * @param linesForCSV  List of Strings one string represents one product related data for one row of csv file.
     */
    public static void writeToFile(String pathToFIle, List<String> linesForCSV) throws IOException
    {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(pathToFIle, false);
            for (String line : linesForCSV)
            {
                fileWriter.write(System.lineSeparator());
                fileWriter.write(line);
            }
        }catch (IOException e)
        {
            throw new IOException("Error writing to file :"+pathToFIle);
        }
        finally
        {
            if (fileWriter != null)
            {
                try {
                    fileWriter.close();
                }catch (IOException e) {
                    throw new IOException("Error closing file writer : "+pathToFIle,e);
                }
            }
        }
    }
}
