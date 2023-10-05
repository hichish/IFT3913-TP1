
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Tassert 
{
    public static void main( String[] args ) {
        
        
        String filePath = args[0];

        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            int result = 0;

            // Read the filee line by line
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("assert") || line.startsWith("fail")) {
                    result++;
                }
            }

            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }
}
