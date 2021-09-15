package by.k19;

import java.io.*;
import java.util.*;

public class FileDataSorter {
    private static final String RESOURCES_FILE_PATH = "src/main/resources/";
    public static void main(String[] args) throws IOException {
        FileDataSorter fileDataSorter = new FileDataSorter();

        File file = new File(RESOURCES_FILE_PATH + "data.properties");
        Properties properties = new Properties();
        properties.load(new FileReader(file));
        String inputFile = properties.getProperty("inputFile");
        String outputFile = properties.getProperty("outputFile");

        fileDataSorter.sortDataFromFileToFile(
                new File(RESOURCES_FILE_PATH + inputFile),
                new File(RESOURCES_FILE_PATH + outputFile));
    }

    public void sortDataFromFileToFile(File inputFile, File outputFile) throws IOException {
        List<String> rows = loadFromFile(inputFile);
        rows = sort(rows);
        uploadToFile(outputFile, rows);
    }

    private List<String> loadFromFile(File inputFile) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(inputFile))) {
            List<String> rows = new ArrayList<>();
            String row;
            while((row = in.readLine()) != null) {
                rows.add(row);
            }
            System.out.println("Data file read " + inputFile.getAbsolutePath());
            return rows;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage() + " : Input file with name \"" + inputFile.getAbsolutePath() + "\" not found");
        } catch (IOException e) {
            throw new IOException(e.getMessage() + " : An error occurred while loading data from a file ");
        }
    }

    private List<String> sort(List<String> rows) {
        String[][] table = new String[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            String[] words = rows.get(i).split("\\s+");
            table[i] = new String[words.length];
            System.arraycopy(words, 0, table[i], 0, words.length);
        }
        Arrays.sort(table, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                for (int count = 0; true; count++) {
                    try {
                        int result = o1[count].compareTo(o2[count]);
                        if (result != 0) {
                            System.out.println("Permutation " + Arrays.toString(o1) + " and " + Arrays.toString(o2));
                            return result;
                        }
                    } catch (Exception e) {
                        return 0;
                    }
                }
            }
        });
        rows = new ArrayList<>();
        for (String[] words : table) {
            rows.add(String.join("\t", words));
        }
        return rows;
    }

    private void uploadToFile(File outputFile, List<String> list) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(outputFile)) {
            for (String line : list) {
                out.println(line);
            }
            System.out.println("The answer is written to the file " + outputFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage() + " : Output file with name \"" + outputFile.getAbsolutePath() + "\" not found");
        }
    }
}
