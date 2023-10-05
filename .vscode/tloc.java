import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class tloc {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Utilisation : java tloc <chemin_vers_fichier>");
            System.exit(1);
        }
        String filePath = args[0];
        try {
            int linesOfCode = countLinesOfCode(filePath);
            System.out.println(linesOfCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int countLinesOfCode(String filePath) throws IOException {
        int count = 0;
        boolean inCommentBlock = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue; // Ignorer les lignes vides
                }

                   if (line.startsWith("/*")) {
                    inCommentBlock = true;
                }
    
                if (!inCommentBlock && !line.startsWith("//")) {
                    count++;
                }
    
                if (line.contains("*/")) {
                    inCommentBlock = false;
                    line = line.substring(line.indexOf("*/") + 2).trim();
                }
            }
        }

        return count;
    }
}
