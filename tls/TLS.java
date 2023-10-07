package tls;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
public class TLS {
    public static String fileToWriteTo;

    public static void main(String[] args) {
        
        // Create a File object representing the folder
        File folder = new File(args[0]);


        iterateOverFolder(folder, getprintOutput(args));

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
    private static String getCsvLine(String filePath) throws IOException{
       return filePath + ", " + getClassName(filePath) + ", " + getPackageName(filePath) + ", " + getTloc(filePath) + ", " + getTassert(filePath) + ", " +  getTcmp(filePath);
    }
    private static boolean isValidTestFile(File file) {
        if (file.isFile() && file.getName().contains("Test") && file.getName().endsWith(".java")) {
            return true;
        }
        return false;
    }
    private static void iterateOverFolder(File folder, boolean printOutput) {
        
        File[] files = folder.listFiles();
            
        // Iterate through the files and folders
        for (File file : files) {
        // Check if it's a file
        if (isValidTestFile(file)) {
            
           outputResult(printOutput, file);
        }
        // Check if it's a directory
        else if (file.isDirectory()) {
            iterateOverFolder(file, printOutput);
        }
    }
        
    }
    
    private static void outputResult(boolean printOutput, File file) {
        try {
            if (printOutput) {
            
                System.out.println(getCsvLine(file.getPath()));
            
        }
            else {
            // Create a FileWriter object with the specified file path
            FileWriter fileWriter = new FileWriter(fileToWriteTo);

            // Create a BufferedWriter to efficiently write characters to the file
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write data to the file
            bufferedWriter.write(getCsvLine(file.getPath()));
            bufferedWriter.newLine(); 

            bufferedWriter.close();
            }
        } catch (Exception e) {
            
        }
        

    }


    private static int getTloc(String filePath) throws IOException{
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
    
    private static int getTassert(String filePath) throws IOException {

        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        int result = 0;

        // Read the filee line by line
        while ((line = bufferedReader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("assert") || line.startsWith("fail")) {
                result++;
            }
        }

        bufferedReader.close();
        return result;
    }

    private static boolean getprintOutput(String[] args) {
        if (args.length == 2) {
            fileToWriteTo = args[1];
            return false;
        }
        return true;
    }
    private static double getTcmp(String filePath) {
        try {
            float tloc = (float)getTloc(filePath)/(float)getTassert(filePath);
            DecimalFormat decimalFormat = new DecimalFormat("#.##"); 
            String formattedNumber = decimalFormat.format(tloc);
            return (int) (tloc * 100) / (100.0);

        } catch (Exception e) {
            return 1;
        }
    }
}   