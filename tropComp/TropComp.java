package tropComp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TropComp {
    static LinkedList<Entry<File, Integer>> tlocResults = new LinkedList<>();
    static LinkedList<Entry<File, Double>> tlsResults = new LinkedList<>();
    static String fileToWriteTo;
    public static void main(String[] args) {
        

        File projectFolder = new File(args[0]);
        

        iterateOverFolder(projectFolder);


        

        outputResult(args, getprintOutput(args));

       

    }

    private static void iterateOverFolder(File folder) {
        
        File[] files = folder.listFiles();
        
            
        // Iterate through the files and folders
        for (File file : files) {

            // Check if it's a file
            if (isValidTestFile(file)) {

                populateResults(file);
            }
            // Check if it's a directory
            else if (file.isDirectory()) {
                iterateOverFolder(file);
         
            }
        }
    }
    private static void populateResults(File file) {
       try {
        int currentFileTloc = getTloc(file.getPath());

        for (int i = 0; i < tlocResults.size(); i++) {
            if (currentFileTloc >= tlocResults.get(i).getValue()) {
                tlocResults.add(i, Map.entry(file, currentFileTloc));
                break;
            }
            else if (i == tlocResults.size()-1) {
                tlocResults.add(Map.entry(file, currentFileTloc));
            }
            
        }

        if (tlocResults.isEmpty()) tlocResults.add(Map.entry(file, currentFileTloc));
        

        double currentFileTls = getTls(file.getPath());
        

        for (int i = 0; i < tlsResults.size(); i++) {
            if (currentFileTls >= tlsResults.get(i).getValue()) {
                tlsResults.add(i, Map.entry(file, currentFileTls));
                break;
            }
            else if (i == tlsResults.size()-1) {
                tlsResults.add(Map.entry(file, currentFileTls));
            }
            
        }

        if (tlsResults.isEmpty()) tlsResults.add(Map.entry(file, currentFileTls));

        
       } catch (Exception e) {
        
       }
    }
    private static boolean isValidTestFile(File file) {
        if (file.isFile() && file.getName().contains("Test") && file.getName().endsWith(".java")) {
            return true;
        }
        return false;
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
    private static double getTls(String filePath) {
        try {
            float tloc = (float)getTloc(filePath)/(float)getTassert(filePath);
            DecimalFormat decimalFormat = new DecimalFormat("#.##"); 
            String formattedNumber = decimalFormat.format(tloc);
            return (int) (tloc * 100) / (100.0);

        } catch (Exception e) {
            return 1;
        }
    }
    private static boolean getprintOutput(String[] args) {
        if (args.length == 3) {
            fileToWriteTo = args[2];
            return false;
        }
        return true;
    }
    private static void outputResult(String[] args, boolean printOutput) {
        int newTlocSize = (int) (tlocResults.size() * (Float.parseFloat(args[1])/100)); 
        List<Entry<File, Integer>> trimmedTlocList = tlocResults.subList(0, newTlocSize);

        ArrayList<String> finalTlocList = new ArrayList<>();

        for (Entry<File, Integer> tlocEntry: trimmedTlocList) {
            finalTlocList.add(tlocEntry.getKey().getPath());
        }

        int newSize = (int) (tlsResults.size() * (Float.parseFloat(args[1])/100)); 
        List<Entry<File, Double>> trimmedTlsList = tlsResults.subList(0, newSize);
        
        ArrayList<String> finalList = new ArrayList<>();

        for (Entry<File, Double> tlsEntry: trimmedTlsList) {
            if (finalTlocList.contains(tlsEntry.getKey().getPath())) {
                finalList.add(tlsEntry.getKey().getPath());
            }
        }

       for (String ab: finalList) {
        outputResult(printOutput, ab);
       }


    }
    private static String getCsvLine(String filePath) throws IOException{
        return filePath + ", " + getClassName(filePath) + ", " + getPackageName(filePath) + ", " + getTloc(filePath) + ", " + getTassert(filePath) + ", " +  getTls(filePath);
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
     private static void outputResult(boolean printOutput, String filePath) {
        try {
            if (printOutput) {
            
                System.out.println(getCsvLine(filePath));
            
        }
            else {
            // Create a FileWriter object with the specified file path
            FileWriter fileWriter = new FileWriter(fileToWriteTo);

            // Create a BufferedWriter to efficiently write characters to the file
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write data to the file
            bufferedWriter.write(getCsvLine(filePath));
            bufferedWriter.newLine(); 

            bufferedWriter.close();
            }
        } catch (Exception e) {
            
        }
        

    }
}
