import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TLOC {
    public static void main(String[] args) {
        String filePath = "test.java"; // Remplacez "VotreClasse.java" par le chemin de votre fichier source
        
        try {
            int linesOfCode = countLinesOfCode(filePath);
            System.out.println("Nombre de lignes de code (sans commentaires) : " + linesOfCode);
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
                    continue;
                }

                if (inCommentBlock) {
                    if (line.contains("*/")) {
                        inCommentBlock = false;
                        line = line.substring(line.indexOf("*/") + 2).trim();
                    } else {
                        continue; // Ignorer les lignes à l'intérieur des blocs de commentaires
                    }
                }

                if (!inCommentBlock && !line.startsWith("//")) {
                    count++;
                }
            }
        }

        return count;
    }
}
