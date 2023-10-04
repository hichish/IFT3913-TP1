import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tassert {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java TAssert <TitleTest.java>");
            System.exit(1);
        }

        String fileName = args[0];
        int tassertCount = countAssertions(fileName);
        System.out.println("TASSERT: " + tassertCount);
    }

    private static int countAssertions(String fileName) {
        int assertionCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                assertionCount += countAssertionsInLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return assertionCount;
    }

    private static int countAssertionsInLine(String line) {
        int count = 0;
        String regex = "assert(\\w)*\\s*\\(";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            count++;
        }

        return count;
    }
}