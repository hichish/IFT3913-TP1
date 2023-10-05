<<<<<<< HEAD

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
public class TLS {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java TLS <path_to_java_file>");
            System.exit(1);
        }
        String filePath = args[0];
        try {
            // Calcul de tloc en utilisant la classe TLOC
            tloc tlocExtractor = new tloc();
            int tloc = tlocExtractor.countLinesOfCode(filePath);

            // Calcul de tassert en utilisant la classe TASSERT
            Tassert tassertExtractor = new Tassert();
            int tassert = tassertExtractor.countAssertions(filePath);
            float tcmp = (float) tloc / tassert;
            DecimalFormat df = new DecimalFormat("#.##"); // Deux décimales
            df.setRoundingMode(RoundingMode.DOWN); // Arrondi vers le bas
            String formattedTcmp = df.format(tcmp);
            System.out.println(filePath);
            System.out.println("Nom de la classe: " + getClassName(filePath));
            System.out.println("tloc: " + tloc);
            System.out.println("tassert: " + tassert);
            System.out.println("tcmp:" + formattedTcmp  );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     private static String getfileName(String filePath) {
        File file = new File(filePath);
        String fileName = file.getName();
        if (fileName.endsWith(".java")) {
            fileName = fileName.substring(0, fileName.lastIndexOf(".java"));
        }
        return fileName;
    }
    private static String getClassName(String filePath) {
        String fileName = new File(filePath).getName();
        if (fileName.endsWith(".java")) {
            fileName = fileName.substring(0, fileName.lastIndexOf(".java"));
        }
        return fileName;
    }
    
=======

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
public class TLS {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java TLS <path_to_java_file>");
            System.exit(1);
        }
        String filePath = args[0];
        try {
            // Calcul de tloc en utilisant la classe TLOC
            tloc tlocExtractor = new tloc();
            int tloc = tlocExtractor.countLinesOfCode(filePath);

            // Calcul de tassert en utilisant la classe TASSERT
            Tassert tassertExtractor = new Tassert();
            int tassert = tassertExtractor.countAssertions(filePath);
            float tcmp = (float) tloc / tassert;
            DecimalFormat df = new DecimalFormat("#.##"); // Deux décimales
            df.setRoundingMode(RoundingMode.DOWN); // Arrondi vers le bas
            String formattedTcmp = df.format(tcmp);
            System.out.println(filePath);
            System.out.println("tloc: " + tloc);
            System.out.println("tassert: " + tassert);
            System.out.println("tcmp:" + formattedTcmp  );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
>>>>>>> c1a66fb12aaab0587bb2448888ca44ce37c1604d
}