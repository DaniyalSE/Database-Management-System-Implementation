import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class pro {
    public static final String FILE_EXTENSION = ".txt";
    public static void createTable(String command) {
        try {
            command = command.replaceAll("\\s+", " ");
            String upperCase = command.toUpperCase();
            String[] parts = upperCase.split(" HAVING ", 2);
            if (parts.length  < 2) {
                throw new IllegalArgumentException("Error: Invalid CREATE syntax. Use CREATE table_name HAVING column1,column2,...");
            }
            String [] exceptionParts = parts[0].split(" ");
            if(exceptionParts.length > 2){
                throw new IllegalArgumentException("Error: Invalid CREATE syntax. Use CREATE table_name HAVING column1,column2,...");
            }
            
            String [] subPartsOriginal = command.split(" ", 4);
            
            String tableName = subPartsOriginal[1].trim();
            // Table name can never be having 
            String nameException = "Having";
            if(tableName.equalsIgnoreCase(nameException)){
                throw new IllegalArgumentException("Error: Invalid Table Name. It should be other than " + tableName + ".");
            }    
            // handling exception in the line before column1, column2, colun3
            for(int y = 0; y<3;y++ ){
                if(!Character.isJavaIdentifierStart(subPartsOriginal[y].charAt(0))){
                    throw new IllegalArgumentException("Error: Invalid CREATE syntax. Use CREATE table_name HAVING column1,column2,...");
                }
                for (int c = 0; c<subPartsOriginal[y].length(); c++){
                    if(!Character.isJavaIdentifierPart(subPartsOriginal[y].charAt(c))){
                        throw new IllegalArgumentException("Error: Invalid CREATE syntax. Use CREATE table_name HAVING column1,column2,...");
                    }
                }
            }
            String[] columns = command.split(" ", 4)[3].split(",");
            // Handling exceptions in the column
            for (int i = 0; i < columns.length; i++) {  // Yeh code har kisi column k starting walay element ko check karay ga k is it accoring to JAVA or not
                String column = columns[i].trim();
                if (column.isEmpty() || !Character.isJavaIdentifierStart(column.charAt(0))) {
                    throw new IllegalArgumentException("Error: Invalid column name '" + column + "'. Column names must follow Java variable naming rules.");
                    }
                for (int j = 1; j < column.length(); j++) {  // Yeh code har ek specific colum k andar walay words check karay ga k are they accord to java
                    if (!Character.isJavaIdentifierPart(column.charAt(j))) {
                        throw new IllegalArgumentException("Error: Invalid column name '" + column + "'. Column names must follow Java variable naming rules.");
                    }
                }
            }
            ArrayList<String> modifiedColumns = new ArrayList<>();  // (agar user comma seprated fields enter krtay huay commas k baad extra spaces dalnay wali chawal maray to yeh code us chawal ko cover krday ga and it would convert extra spaces to a single tab space)
            for(int k = 0; k<columns.length; k++){
                String newColumn = columns[k].trim();
                modifiedColumns.add(newColumn);
            }
            File file = new File(tableName + FILE_EXTENSION);
            if (file.exists()) {
                System.out.println("Error: Table already exists.");
                return;
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for( int m = 0; m<modifiedColumns.size(); m++ ){  //(yeh loop isi liye lagaya k har single column k likhnay k baad tab seprately print ho)
                writer.write(modifiedColumns.get(m));
                writer.write("\t");
            }
            writer.newLine();
            writer.close();
            
            
            System.out.println("Table " + tableName + " created successfully.");
            
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Invalid CREATE syntax. Use CREATE table_name HAVING column1,column2,...");
        } catch (Exception e){
            System.out.println(e.getMessage());   // {Two catches are for (createTable having column1,column2) & (create Table having 1column, 2column)}
        }
    }
        

    public static void dropTable(String delCommand){
        try {
            for (int i = 0; i<delCommand.length(); i++){
            }
            delCommand = delCommand.replaceAll("\\s+", " ");
            String upperCase = delCommand.toUpperCase();
            String[] parts = upperCase.split(" ");
            for(int i = 0; i<parts.length; i++){
                String part = parts[i].trim();
                if(!Character.isJavaIdentifierStart(part.charAt(0))){
                    throw new IllegalArgumentException("Error: Invalid DROP syntax. Use DROP TABLE table_name");
                }
                for(int j =0; j<part.length(); j++){
                    if(!Character.isJavaIdentifierPart(part.charAt(j))){
                        throw new IllegalArgumentException("Error: Invalid DROP syntax. Use DROP TABLE table_name");
                    }
                }
            }
            if (parts.length < 3 || !parts[0].equals("DROP") || !parts[1].equals("TABLE")) {
                throw new IllegalArgumentException("Error: Invalid DROP syntax. Use DROP TABLE table_name");
            }
            String [] subParts = delCommand.split(" ");
            String fileName = subParts[2].trim() + ".txt";
            File file = new File(fileName);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Table " + subParts[2].trim() + " deleted successfully.");
                } 
            } else {
                System.out.println("Error: Table " + subParts[2].trim() + " does not exist.");
            }
        } catch (Exception e) {
            System.out.println("Error: Invalid DROP syntax. Use DROP TABLE table_name");
        }
    }

    public static void showAllTables(String showAllCommand) {
        try {
            String upperCase = showAllCommand.toUpperCase();
            String [] parts = upperCase.split(" ");
            if(parts.length >2 || parts.length<2){
                throw new IllegalArgumentException("Error: Invalid SHOW syntax. Use SHOW ALL");
            }
            for(int i = 0; i<parts.length; i++){
                String part = parts[i].trim();
                if(!Character.isJavaIdentifierStart(parts[i].charAt(0))){
                    throw new IllegalArgumentException("Error: Invalid SHOW syntax. Use SHOW ALL");
                }for(int j =0; j<part.length(); j++ ){
                    if(!Character.isJavaIdentifierPart(part.charAt(j))){
                        throw new IllegalArgumentException("Error: Invalid SHOW syntax. Use SHOW ALL");
                    }
                }
            }
            String path = "."; //(magical DOT jo path nikal dega current folder ka)
            File directory = new File(path);

            if (!directory.exists() || !directory.isDirectory()) {
                throw new Exception("Error: The specified path does not exist or is not a directory.");
            }

            File[] files = directory.listFiles();

            if (files != null && files.length > 0) {
                boolean tableFound = false;

                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        String tableName = file.getName().replace(".txt", "");
                        System.out.println(tableName);
                        tableFound = true;
                    }
                }
                if (!tableFound) {
                    System.out.println("No tables found.");
                }
            } else {
                System.out.println("No tables found.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    
    public static void insertField(String insertCommand){
        try {
            insertCommand = insertCommand.replaceAll("\\s+", " ").trim();
            String [] mainParts = insertCommand.split("(?i)\\s+VALUES\\s+", 2);
            String upperCase = insertCommand.toUpperCase();  
            String [] parts = upperCase.split(" VALUES ", 2);
            String [] exceptionPart = parts[0].split(" ");
            if(exceptionPart.length!=3){
                throw new IllegalArgumentException("Error: Invalid INSERT syntax. Use INSERT INTO table_name VALUES (value1, value2, value3, ...)");
            }
            for(int i = 0; i< exceptionPart.length; i++){
                String part = exceptionPart[i].trim();
                if(!Character.isJavaIdentifierStart(exceptionPart[i].charAt(0))){
                    throw new IllegalArgumentException("Error: Invalid INSERT syntax. Use INSERT INTO table_name VALUES (value1, value2, value3, ...)");
                }for(int j = 0; j< part.length(); j++){
                    if(!Character.isJavaIdentifierPart(part.charAt(j))){
                        throw new IllegalArgumentException("Error: Invalid INSERT syntax. Use INSERT INTO table_name VALUES (value1, value2, value3, ...)");
                    }
                }
            }
            String table_name = exceptionPart[2];
        if (!Character.isJavaIdentifierStart(table_name.charAt(0))) {
            throw new IllegalArgumentException("Error: Invalid table name. Table names must follow Java identifier rules.");
        }
        String values = mainParts[1].trim();
        if (!values.startsWith("(") || !values.endsWith(")")) {
            throw new IllegalArgumentException("Error: Values must be enclosed in parentheses.");
        }

        values = values.substring(1, values.length() - 1).trim(); 
        String[] valueArray = values.split(",");

        File file = new File(table_name + FILE_EXTENSION);
        if (!file.exists()) {
            throw new IllegalArgumentException("Error: Table does not exist.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            if (header == null) {
                throw new IllegalArgumentException("Error: Table is corrupted (missing header).");
            }
            int columnCount = header.split("\t").length;
            if (valueArray.length != columnCount) {
                throw new IllegalArgumentException("Error: Value count does not match column count.");
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(String.join("\t", valueArray).trim());
            writer.newLine();
        }

        System.out.println("The field is appended successfully!");
        } catch (Exception e) {
            System.out.println("Error: Invalid INSERT syntax. Use INSERT INTO table_name VALUES (value1, value2, value3, ...)");
        }
    }

public static void selectStatement(String selectCommand) {
    try {
        selectCommand = selectCommand.trim().replaceAll("\\s+", " ");
        String[] parts = selectCommand.split(" ");

        if (parts.length < 10 || !"FROM".equalsIgnoreCase(parts[1]) || !"TABLE".equalsIgnoreCase(parts[2]) ||
            !"HAVING".equalsIgnoreCase(parts[4]) || !"SORT".equalsIgnoreCase(parts[8]) || !"BY".equalsIgnoreCase(parts[9])) {
            throw new IllegalArgumentException("Error: Invalid SELECT syntax. Use SELECT FROM TABLE table_name HAVING column = value SORT BY column");
        }

        String tableName = parts[3];
        String conditionColumn = parts[5].toLowerCase();
        String conditionValue = parts[6].replaceAll("[\"']", "").trim();
        String sortByColumn = parts[10].toLowerCase();

        File file = new File(tableName + ".txt");
        if (!file.exists()) {
            throw new IllegalArgumentException("Error: Table \"" + tableName + "\" does not exist.");
        }

        List<String[]> rows = new ArrayList<>();
        String[] headers;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            headers = reader.readLine().trim().split("\\t");
            Map<String, Integer> columnIndices = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                columnIndices.put(headers[i].toLowerCase(), i);
            }

            Integer conditionIndex = columnIndices.get(conditionColumn);
            Integer sortIndex = columnIndices.get(sortByColumn);

            if (conditionIndex == null) {
                throw new IllegalArgumentException("Error: Column \"" + conditionColumn + "\" does not exist.");
            }
            if (sortIndex == null) {
                throw new IllegalArgumentException("Error: Column \"" + sortByColumn + "\" does not exist.");
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.trim().split("\\t");
                if (row.length == headers.length && row[conditionIndex].trim().equalsIgnoreCase(conditionValue)) {
                    rows.add(row);
                }
            }

            for (int i = 0; i < rows.size() - 1; i++) {
                for (int j = 0; j < rows.size() - i - 1; j++) {
                    if (rows.get(j)[sortIndex].compareToIgnoreCase(rows.get(j + 1)[sortIndex]) > 0) {
                        String[] temp = rows.get(j);
                        rows.set(j, rows.get(j + 1));
                        rows.set(j + 1, temp);
                    }
                }
            }

            System.out.println(String.join("\t", headers));
            for (String[] row : rows) {
                System.out.println(String.join("\t", row));
            }
        }
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
    }
}
    

    public static void updateTable(String command) {
        command = command.trim().replaceAll("\\s+", " ").toLowerCase();
        int updateIndex = command.indexOf("update table ") + "update table ".length();
        int setIndex = command.indexOf(" set ");
        int havingIndex = command.indexOf(" having ");
        if (updateIndex == -1 || setIndex == -1 || havingIndex == -1) {
            System.out.println("Error: Invalid command format. USE UPDATE TABLE table_name SET column TO value HAVING column = value");
            return;
        }
        String tableName = command.substring(updateIndex, setIndex).trim();
        String setClause = command.substring(setIndex + " set ".length(), havingIndex).trim();
        String conditionClause = command.substring(havingIndex + " having ".length()).trim();
        int toIndex = setClause.indexOf(" to ");
        if (toIndex == -1) {
            System.out.println("Error: Invalid SET clause. Expected SET column TO value");
            return;
        }
        String updateColumn = setClause.substring(0, toIndex).trim();
        String newValue = setClause.substring(toIndex + " to ".length()).trim().replace("\"", "");
        int equalIndex = conditionClause.indexOf("=");
        if (equalIndex == -1) {
            System.out.println("Error: Invalid HAVING clause. Expected HAVING column = value");
            return;
        }
        String conditionColumn = conditionClause.substring(0, equalIndex).trim();
        String conditionValue = conditionClause.substring(equalIndex + 1).trim().replace("\"", "");
        File oldFile = new File(tableName + ".txt");
        if (!oldFile.exists()) {
            System.out.println("Error: Table file '" + tableName + ".txt' does not exist.");
            return;
        }
        File tempFile = new File(tableName + "_temp.txt");
        try (
            BufferedReader reader = new BufferedReader(new FileReader(oldFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String header = reader.readLine();
            if (header == null) {
                System.out.println("Error: Table file is empty.");
                tempFile.delete();
                return;
            }
            writer.write(header);
            writer.newLine();
            String[] columns = header.split("\t");
            int updateColumnIndex = -1;
            int conditionColumnIndex = -1;
            for (int i = 0; i < columns.length; i++) {
                if (columns[i].equalsIgnoreCase(updateColumn)) {
                    updateColumnIndex = i;
                }
                if (columns[i].equalsIgnoreCase(conditionColumn)) {
                    conditionColumnIndex = i;
                }
            }
            if (updateColumnIndex == -1) {
                System.out.println("Error: Update column " + updateColumn + " does not exist.");
                tempFile.delete();
                return;
            }
            if (conditionColumnIndex == -1) {
                System.out.println("Error: Condition column " + conditionColumn + " does not exist.");
                tempFile.delete();
                return;
            }
            String line;
            int updatedRows = 0;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split("\t");
                if (rowData[conditionColumnIndex].trim().equalsIgnoreCase("\"" + conditionValue.trim() + "\"")) {
                    rowData[updateColumnIndex] = "\"" + newValue + "\"";
                    updatedRows++;
                }
                writer.write(String.join("\t", rowData));
                writer.newLine();
            }
            System.out.println("Rows updated: " + updatedRows);
        } catch (IOException e) {
            System.out.println("Error: Unable to update file. " + e.getMessage());
            tempFile.delete();
            return;
        }
        try {
            Files.move(tempFile.toPath(), oldFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File updated successfully.");
        } catch (IOException e) {
            System.out.println("Error: Unable to replace old file. " + e.getMessage());
        }
    }

    public static void deleteFromTable(String command) {
        command = command.trim().replaceAll("\\s+", " ").toLowerCase();
    
        int deleteIndex = command.indexOf("delete from table ") + "delete from table ".length();
        int havingIndex = command.indexOf(" having ");
    
        if (deleteIndex == -1 || havingIndex == -1) {
            System.out.println("Error: Invalid command format. USE DELETE FROM TABLE table_name HAVING column = value");
            return;
        }
    
        String tableName = command.substring(deleteIndex, havingIndex).trim();
        String condition = command.substring(havingIndex + " having ".length()).trim();
    
        int equalIndex = condition.indexOf("=");
        if (equalIndex == -1) {
            System.out.println("Error: Invalid HAVING clause. Expected HAVING column = value");
            return;
        }
    
        String conditionColumn = condition.substring(0, equalIndex).trim();
        String conditionValue = condition.substring(equalIndex + 1).trim().replace("\"", "");
    
        File oldFile = new File(tableName + ".txt");
        if (!oldFile.exists()) {
            System.out.println("Error: Table file '" + tableName + ".txt' does not exist.");
            return;
        }
    
        File tempFile = new File(tableName + "_temp.txt");
    
        try (
            BufferedReader reader = new BufferedReader(new FileReader(oldFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String header = reader.readLine();
            if (header == null) {
                System.out.println("Error: Table file is empty.");
                tempFile.delete();
                return;
            }
    
            writer.write(header);
            writer.newLine();
    
            String[] columns = header.split("\t");
            int conditionColumnIndex = -1;
    
            for (int i = 0; i < columns.length; i++) {
                if (columns[i].equalsIgnoreCase(conditionColumn)) {
                    conditionColumnIndex = i;
                    break;
                }
            }
    
            if (conditionColumnIndex == -1) {
                System.out.println("Error: Specified condition column does not exist.");
                tempFile.delete();
                return;
            }
    
            String line;
            int deletedRows = 0;
    
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split("\t");
                if (!rowData[conditionColumnIndex].trim().equalsIgnoreCase("\"" + conditionValue.trim() + "\"")) {
                    writer.write(String.join("\t", rowData));
                    writer.newLine();
                } else {
                    deletedRows++;
                }
            }
    
            System.out.println("Rows deleted: " + deletedRows);
        } catch (IOException e) {
            System.out.println("Error: Unable to process the file. " + e.getMessage());
            tempFile.delete();
            return;
        }
    
        try {
            Files.move(tempFile.toPath(), oldFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File updated successfully.");
        } catch (IOException e) {
            System.out.println("Error: Unable to replace old file. " + e.getMessage());
        }
    }

    public static void help(String helpCommand) {
        System.out.println("Available Commands:\n");
        System.out.println("1. CREATE Syntax: \n\tCREATE TABLE table_name HAVING column1,column2,...\n");
        System.out.println("2. DROP Syntax: \n\tDROP TABLE table_name\n");
        System.out.println("3. SHOW Syntax: \n\tSHOW ALL\n");
        System.out.println("4. INSERT Syntax: \n\tINSERT INTO table_name VALUES (value1, value2, value3, ...)\n");
        System.out.println("5. SELECT Syntax: \n\tSELECT FROM TABLE table_name HAVING column = value SORT BY column\n");
        System.out.println("6. UPDATE Syntax: \n\tUPDATE TABLE table_name SET column TO value HAVING column = value\n");
        System.out.println("7. DELETE Syntax: \n\tDELETE FROM TABLE table_name HAVING column = value\n");
        System.out.println("8. HELP: \n\tDisplays this help message\n");
        System.out.println("9. EXIT: \n\tTerminates the program\n");
    }
    
    
    public static void main(String[] args) {
        while (true){
            Scanner sc = new Scanner(System.in);
            System.out.print(">> ");
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("EXIT")){
                System.out.println("DBMS Terminated...!");
                break;
            }else{
                processCommand(input);
            }
            
        }
        
    }

    public static void processCommand(String input) {
        try {
            String[] parts = input.split(" ", 2);
            String command = parts[0].toUpperCase();

            switch (command) {
                case "CREATE":
                if(command.equals("CREATE") ){
                    createTable(input);
                    break;
                }   
                case "DROP":
                if(command.equals("DROP")){
                    dropTable(input);
                    break;
                }
                case "SHOW":
                if(command.equals("SHOW")){
                    showAllTables(input);
                    break;
                } 
                case "INSERT":
                if(command.equals("INSERT")){
                    insertField(input);
                    break;
                }
                case "SELECT":
                if(command.equals("SELECT")){
                    selectStatement(input);
                    break;
                }
                case "HELP":
                if(command.equals("HELP")){
                    help(input);
                    break;
                }
                case "UPDATE":
                if(command.equals("UPDATE")){
                    updateTable(input);
                    break;
                }
                case "DELETE":
                if(command.equals("DELETE")){
                    deleteFromTable(input);
                    break;
                }
                default: 
                    throw new IllegalArgumentException("Invalid! Statement");
            }
        }catch (Exception e) {
            System.out.println("Error processing command: " + e.getMessage());
        }
    }
}