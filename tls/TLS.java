package tls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
public class TLS {
    public static void main(String[] args) {
        if (args.length != 0) {
            System.err.println("Usage: java TLS <path_to_java_file>");
            System.exit(1);
        }
        //String filePath = args[0];
        String filePath = "TitleTest.java";
        try {
            // Calcul de tloc en utilisant la classe TLOC
            // Run a java app in a separate system process
            // Run a java app in a separate system process
            Process proc = Runtime.getRuntime().exec("java -jar tloc/tloc.jar " + filePath);

            // Then retrieve the process output
            InputStream in = proc.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;

            // Read and print the output of the process
            while ((line = reader.readLine()) != null) {
            System.out.println("Process Output: " + line);
            }

            // Wait for the process to complete and get the exit code
            int exitCode = proc.waitFor();
            System.out.println("Process exited with code: " + exitCode);

            // Calcul de tassert en utilisant la classe TASSERT
            //Tassert tassertExtractor = new Tassert();
            //int tassert = tassertExtractor.countAssertions(filePath);
            //float tcmp = (float) tloc / tassert;
            DecimalFormat df = new DecimalFormat("#.##"); // Deux décimales
            df.setRoundingMode(RoundingMode.DOWN); // Arrondi vers le bas
            //String formattedTcmp = df.format(tcmp);
            System.out.println(filePath);
             System.out.println(getPackageName(filePath));
            System.out.println(getClassName(filePath));
           // System.out.println(tloc);
            //System.out.println(tassert);
            //System.out.println(formattedTcmp  );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
      private static String getPackageName(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("package ")) {
                    // Trouvé une déclaration de paquet, extrayons le nom du paquet
                    return line.substring(8, line.length() - 1).trim(); // Ignorer le point-virgule à la fin
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ""; // Si aucune déclaration de paquet n'est trouvée, retournez une chaîne vide
    }
    private static String getClassName(String filePath) {
        String fileName = new File(filePath).getName();
        if (fileName.endsWith(".java")) {
            fileName = fileName.substring(0, fileName.lastIndexOf(".java"));
        }
        return fileName;
    }
    
}