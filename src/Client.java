import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author {Piotr Kacprzak}
 * @version {1.0.0}
 */

public class Client {

    /**
     * Main function expecting to get filename as first argument
     * and string pattern as second
     *
     * @param args
     */
    public static void main(String[] args) {
        String fileName;
        String pattern;
        int bestMatchLineNumber;

        try {
            fileName = args[0];
            pattern = args[1];
            bestMatchLineNumber = findBestMatchingLineInFile(fileName, pattern);
            System.out.println(bestMatchLineNumber);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Missing parameters");
            e.printStackTrace();
        }
    }

    /**
     * Function calling Levenshtein algorithm for file with given name
     *
     * @param fileName
     * @param pattern
     * @return
     */
    private static int findBestMatchingLineInFile(String fileName, String pattern) {
        Levenshtein levenshtein = new Levenshtein(pattern);

        int lineCounter = 1;
        int bestMatchLineNumber = 0;
        int currentLineMatchValue;
        int bestMatchValue = Integer.MAX_VALUE;

        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            Scanner myReader = new Scanner(inputStream);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                currentLineMatchValue = levenshtein.compareWithPattern(line);
                if (currentLineMatchValue < bestMatchValue) {
                    bestMatchLineNumber = lineCounter;
                    bestMatchValue = currentLineMatchValue;
                }
                if (bestMatchValue == 0) break;
                lineCounter++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
        return bestMatchLineNumber;
    }
}
