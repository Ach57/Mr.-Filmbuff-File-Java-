import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * Name(s) and ID(s) : Achraf Cheniti - 40244865 / Ali Sher - 40255236
 * COMP 249
 * Section J-X and S
 * Assignment 02
 * Due Date 27 March 2024
 */
public class mainDriver {

    /**
     * 
     * @param txtFileName  the name of the txt file to write to
     * @param pathOfFolder the path of the folder selected
     * 
     *                     The purpose of this method is to accept two strings. one
     *                     is the name of the file and the other the path of the
     *                     folder that contains data.
     *                     It reads the content of the files and then places the
     *                     folder names to txt file Part1_Manifest file
     * End result: Part1_Manifest will contain the name of the records e.g { Movie1990.csv, Movie1991.csv . . .}
     */
    public static void getContentNamesInTxtFile(String txtFileName, String pathOfFolder) {
        String result = "";
        int i = 0;
        try {
            System.out.println("Reading Folder . . .\n");
            File folder = new File(pathOfFolder); // Gets the folder to an object of type File
            File[] listFiles = folder.listFiles(); // addes all elements of the folder to an array of File[]
            for (File file : listFiles) { // Exception might occur here
                String name = file.getName();
                if (name.endsWith("csv")) // Only on csv files
                // The reason we're using Substring is because the files get printed in an incorrect order so we set them in the correct order
                    result += file.getName().substring(0, file.getName().length() - 5) + i + ".csv" + "\n";
                // result takes the file name wihtout the path and does a substring from 0 to
                // the number than addeds the other data to it
                i++;
            }
            System.out.println("Gathering Data . . .\n"); // displaying message

            PrintWriter output = new PrintWriter(new FileOutputStream(txtFileName)); // Intializing writing file 

            output.write(result); // Writing the result to it 
            System.out.println("File with name " + txtFileName + " Created  . . .\n"); // notifying user
            output.close(); // closing the file

        } catch (NullPointerException exception) { // Catch nullpointerexception
            System.out.println("Can not list out item because Folder does not exist or is empty");
            System.exit(0);
        } catch (FileNotFoundException exception) { // catch file not found exception
            System.out.println("File can not be written to.");
            System.exit(0);
        } catch (Exception e) { // other type of exception
            System.out.println(e.getMessage());
            System.exit(0);
        }

    }

    /**
     * @typeOfError error type Semantic or Syntax
     * @param typeOfError errory type as String 
     * @param line     line of the file in the fileName
     * @param fileName e.g Movies1999.csv
     * @param content  String of the data derived from the file
     *                 This method writes the data to the bad_movie_records.txt
     */

    public static void writeToBadFile(String fileName, String content, int line, String typeOfError) {
        // (a) the error and its type,syntax or semantic,
        // (b) the original movie record, (Done)
        // (c)the name of the input file in which the record appears (Done)
        // (d) the record’s position(linenumber) in the input file.
        try {
            RandomAccessFile badFile = new RandomAccessFile("bad_movie_records.txt", "rw"); // opening the file
            long len = badFile.length(); // getting the length of the file
            if (len == 0) //locating if the length if 0
                badFile.writeBytes(typeOfError + "," + content + "," + fileName + ",Line:" + line);
            else {// else seek the last character and write from there
                badFile.seek(len);
                badFile.writeBytes("\n" + typeOfError + "," + content + "," + fileName + ",Line:" + line);
            }

            badFile.close();// closing the file
        } catch (FileNotFoundException exception) {
            System.out.println("Could not write to specific file");
            System.out.println("System exiting . . .");
            System.exit(0); // exiting when not being able to write to the file
        } catch (Exception e) {
            System.out.println("Different exception occured in writeToBadFile() " + e.getMessage());
            System.exit(0);
        }

    }

    /**
     * 
     * @param correctMovie Movie object
     * @param orginalData  orginal unedit data
     *                     writes the data to the corret file using the generes
     *                     array of Movies and compares it to the value in the
     *                     object
     */
    public static void writeToGoodFile(Movie correctMovie, String orginalData) {
        int i = 0;
        while (i < Movie.genreList.length && !correctMovie.getGenres().equals(Movie.genreList[i])) {
            i++;
        }
        try {
            RandomAccessFile testor = new RandomAccessFile(correctMovie.getGenres() + ".csv", "rw");
            long l = testor.length(); // gets the length of the file

            if (l == 0) // if length is 0
                testor.writeBytes(orginalData);
            else {
                testor.seek(l); // else seek last character position
                testor.writeBytes("\n" + orginalData); // and write
            }
            testor.close(); // closing the file
        } catch (IOException e) {
            System.out.println("Couldn't write to this file in writeToGoodFile()");
            System.out.println("Exiting . . .");
            System.exit(0);

        } catch (Exception e) {
            System.out.println("Different exception occured in writeToGoodFile() " + e);
        }

    }

    /**
     * Checks if there are missing quotations or not and throws exception
     *  Once the data is splitted in the array of Strings, the method goes through every index and checks if the data 
     * begins with quotes, if so if it ends with it and vice versa
     * @param data
     * @throws MissingQuotesException
     */
    public static void checkQuotation(String[] data) throws MissingQuotesException {
        for (int i = 0; i < data.length; i++) {
            if (!data[i].equals("")) {
                if (data[i].charAt(0) == '"' && data[i].charAt(data[i].length() - 1) != '"') {
                    throw new MissingQuotesException("Syntax Error misisng quotes");

                }
                if (data[i].charAt(data[i].length() - 1) == '"' && data[i].charAt(0) != '"')
                    throw new MissingQuotesException("Syntax Error misisng quotes");
            }

        }

    }

    /**
     * cleanse data is used with part1 to do the partition. It handles the data and if there is an exception
     * it throws it.
     * @param data // String[] array
     * @throws ExcessFieldsException
     * @throws MissingQuotesException
     * @throws MissingFieldsException
     * @throws BadYearException
     * @throws BadTitleException
     * @throws BadDurationException
     * @throws BadGenreException
     * @throws BadScoreException
     * @throws BadRatingException
     * @throws BadNameException
     */
    public static void cleanseData(String[] data)
            throws ExcessFieldsException, MissingQuotesException, MissingFieldsException, BadYearException,
            BadTitleException, BadDurationException, BadGenreException, BadScoreException, BadRatingException,
            BadNameException {
        // Syntax errors:

        checkQuotation(data); // Throws missing quotation

        if (data.length > 10) // Excess filed exception
            throw new ExcessFieldsException("Syntax Error Excess filed");

        if (data.length < 10) // missing fields of data
            throw new MissingFieldsException("Syntax Error Missing fileds");
        // Semantic Error:
        try {
            // rating and genres are handled in the constructor
            if (!data[0].equals("")) { // check if the String is empty
                if (data[0].charAt(0) == '"' && data[0].charAt(data[0].length() - 1) == '"')
                    data[0] = data[0].substring(1, data[0].length() - 1); // removes quotation

            } else {
                throw new BadYearException("Semantic Error Missing year", 1990, 1999); // throws exception
            }
            // same process
            if (!data[2].equals("")) {
                if (data[2].charAt(0) == '"' && data[2].charAt(data[2].length() - 1) == '"')
                    data[2] = data[2].substring(1, data[2].length() - 1);

            } else {
                throw new BadDurationException("Semantic Error Missing Duration", 30, 300);
            }
            if (!data[5].equals("")) {
                if (data[5].charAt(0) == '"' && data[5].charAt(data[5].length() - 1) == '"')
                    data[5] = data[5].substring(1, data[5].length() - 1);
            } else {
                throw new BadScoreException("Semantic Error Missing Score", 0, 10);
            }

            int year;
            int duration;
            double score;

            try {
                year = Integer.parseInt(data[0]); // NumberformatException can occur
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Semantic Error Invalid Year"); // check if we can convert the string to a number
            }
            try {
                duration = Integer.parseInt(data[2]); // NumberformatException can occur
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Semantic Error Invalid Duration");
            }

            try {
                score = Double.parseDouble(data[5]); // NumberformatException can occur
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Semantic Error Invalid score");
            }

            Movie myMovie = new Movie(year, data[1], duration,
                    data[3].toLowerCase(),
                    data[4].toUpperCase(), score, data[6], data[7], data[8], data[9]);

            writeToGoodFile(myMovie, String.join(",", data) + ",");

        } catch (NumberFormatException e) { // number format will occur when parseInt or double
            throw e;

        } catch (BadYearException e) {
            throw e;
        } catch (BadTitleException e) {
            throw e;
        } catch (BadDurationException e) {
            throw e;
        } catch (BadGenreException e) {
            throw e;
        } catch (BadScoreException e) {
            throw e;
        } catch (BadRatingException e) {
            throw e;
        } catch (BadNameException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * This method is used to split the given string into an array of Strings[]
     * 
     * @param nextLine takes as paramter the line in the file
     * @return String[] of the data
     */

    public static String[] splitData(String nextLine) { // will take as paramater the writteninput

        int i = nextLine.indexOf('"');
        int j = i;
        if (i == -1) { // when there is no quotes detected within the String, split only
            return nextLine.split(",");
        } else { // in case a quote is detected

            while (i < nextLine.length()) { // look for second quote
                i++;
                if (nextLine.charAt(i) == '"') {
                    String sub = nextLine.substring(j, i + 1);
                    int indexOfComma = sub.indexOf(',');
                    if (indexOfComma == -1) { // no comma detected
                        return nextLine.split(",");
                    } else { // comma detected
                        nextLine = nextLine.substring(0, j + indexOfComma) +
                                nextLine.substring(j + indexOfComma + 1, nextLine.length());
                        String[] data = nextLine.split(",");
                        if (data.length > 10 || data.length < 10) { // checks if the array has 10 data attributes
                            return data;
                        } else { // does have exactly 10
                            i = 0;
                            while (i < data.length) {
                                if (data[i].charAt(0) == '"') {
                                    data[i] = data[i].substring(0, indexOfComma) +
                                            "," + data[i].substring(indexOfComma, data[i].length()); // returning back
                                                                                                     // the information
                                    return data;
                                }
                                i++;
                            }

                        }

                    }

                }

            }

        }

        return nextLine.split(",");

    }

    /**
     * handleContent( contentName ) uses cleanseData(data) to check the exception
     * when the exception occurs, it throws it and handleconent catches it
     * Whenever an exception occurs, in the catch block, it writes to the bad file
     * @param contentName // File name e.g Movies1990.csv
     */
    public static void handleContent(String contentName) {
        String writeninput = "";
        String[] data = null;
        int line = 0; // line counter
        try {
            Scanner content = new Scanner(new FileInputStream(contentName)); // reading the csv file

            while (content.hasNextLine()) {
                try {
                    line++;
                    writeninput = content.nextLine().trim(); // gets the line 
                    // System.out.println(writeninput);
                    data = splitData(writeninput); // call the method split data

                    cleanseData(data); // call the method cleanse Data for the array
                } catch (MissingQuotesException e) {
                    writeToBadFile(contentName, writeninput, line, e.getMessage());

                } catch (ExcessFieldsException e) {
                    writeToBadFile(contentName, writeninput, line, e.getMessage());

                } catch (MissingFieldsException e) { // Requires a detection to what the missing filed is
                    writeToBadFile(contentName, writeninput, line, e.getMessage());

                } catch (BadYearException e) {
                    writeToBadFile(contentName, writeninput, line, e.getMessage());

                } catch (BadTitleException e) {
                    writeToBadFile(contentName, writeninput, line, e.getMessage());

                } catch (BadDurationException e) {
                    writeToBadFile(contentName, writeninput, line, e.getMessage());
                } catch (BadGenreException e) {
                    writeToBadFile(contentName, writeninput, line, e.getMessage());

                } catch (BadScoreException e) {
                    writeToBadFile(contentName, writeninput, line, e.getMessage());

                } catch (BadRatingException e) {
                    writeToBadFile(contentName, writeninput, line, e.getMessage());

                } catch (BadNameException e) {
                    writeToBadFile(contentName, writeninput, line, e.getMessage());

                } catch (NumberFormatException e) { // incase the data[0], data[2], data[5] have strings
                    writeToBadFile(contentName, writeninput, line, e.getMessage());

                } catch (Exception e) { // in case of a different exception to occur
                    System.out.println(
                            "Different exception occured in second try block of HandleContent() " + e.getMessage());
                }

            }
            content.close(); // close the file

        } catch (FileNotFoundException e) {
            System.out.println("Could not read file: " + contentName + " . . .");
        } catch (Exception e) {
            System.out.println("Different exception in handleContent() " + contentName + " " + writeninput + e);
        }
    }

    /**
     * The do_part1 method takes as input the part1.txt file
     * reads the file and then loops through it line by line
     * using the HandleContent method that takes the name of the csv file e.g
     * Movie1999.csv and handles that data in that method
     * Lastly, it writes in the part2.txt file
     * 
     * @param txtFile part1_manifest.txt as input that has all the Names of the
     *                Movies files
     */
    public static String do_part1(String txtFile) {
        Scanner streamObject = null;
        PrintWriter manifestFile = null;
        try {

            streamObject = new Scanner(new FileInputStream(txtFile)); // opening part1_manifest.txt

        } catch (FileNotFoundException exception) {
            System.out.println("Could not be able to open the file. Either file not in directory or not able to open");
            System.exit(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        while (streamObject.hasNextLine()) {
            handleContent(streamObject.nextLine()); // Read all the lines and handle them in the part1_manifestFile
        }
        streamObject.close(); // closing the part1_ManifestFile

        // Writing to part2_manifest.txt using the join method for the static array from Movie
        String output = String.join(".csv\n", Movie.genreList) + ".csv"; // Collects all the gener.csv names in one
                                                                         // string
        try {

            manifestFile = new PrintWriter(new FileOutputStream("part2_manifest.txt"));

        } catch (FileNotFoundException e) {
            System.out.println("Could not write to file part2_manifest.txt ");
            System.exit(0); // exiting

        } catch (Exception e) {
            System.out.println("Different exception occcured in do_part1() in writing to part2 " + e.getMessage());
            System.exit(0); // exiting
        }
        manifestFile.write(output);
        manifestFile.close();

        return "part2_manifest.txt";
    }

    /**
     * part2_handler takes as paramter the csv file name in String format
     * opens the csv file e.g music.csv
     * counts the number of lines
     * then creates an array of Movies
     * lastly, it serilizes the file into the right .ser file
     * 
     * @param csvName csv file e.g comedy.csv
     */
    public static void part2_Handler(String csvName) { // takes CSVFile as paramter
        Scanner csvFile = null;
        Movie[] movieArray = null;
        
        try {
            // Gets number of lines first:
            csvFile = new Scanner(new FileInputStream(csvName)); // gets csv file 

            int numberOfLines = 0;
            while (csvFile.hasNextLine()) {
                csvFile.nextLine();
                numberOfLines++; // Count of number of lines
            }
            csvFile.close(); // close the file

            // Filling the array of Movies:
            csvFile = new Scanner(new FileInputStream(csvName)); // gets csv file name
            movieArray = new Movie[numberOfLines];
            int c = 0;
            while (csvFile.hasNextLine()) {
                try {
                    String[] data = splitData(csvFile.nextLine());

                    
                    if (data[0].charAt(0) == '"' && data[0].charAt(data[0].length() - 1) == '"')
                        data[0] = data[0].substring(1, data[0].length() - 1); // removes the quotes
                    if (data[2].charAt(0) == '"' && data[2].charAt(data[2].length() - 1) == '"')
                        data[2] = data[2].substring(1, data[2].length() - 1); // removes the quotes
                    if (data[5].charAt(0) == '"' && data[5].charAt(data[5].length() - 1) == '"')
                        data[5] = data[5].substring(1, data[5].length() - 1); // removes the quotes

                    int year = Integer.parseInt(data[0]);
                    int duration = Integer.parseInt(data[2]);
                    double score = Double.parseDouble(data[5]);
                    Movie m = new Movie(year, data[1], duration, data[3].toLowerCase(), data[4].toUpperCase(), score,
                            data[6],
                            data[7],
                            data[8], data[9]);
                    
                    movieArray[c] = m; // entering the movie object within the array
                    

                } catch (NumberFormatException e) { // number format will occur when parseInt or double
                    System.out.println("Exception at csv file " + csvName + "Line: " + c + " " + e.getMessage());
                } catch (BadYearException e) {
                    System.out.println("Exception at csv file " + csvName + "Line: " + c + " " + e.getMessage());
                } catch (BadTitleException e) {
                    System.out.println("Exception at csv file " + csvName + "Line: " + c + " " + e.getMessage());
                } catch (BadDurationException e) {
                    System.out.println("Exception at csv file " + csvName + "Line: " + c + " " + e.getMessage());
                } catch (BadGenreException e) {
                    System.out.println("Exception at csv file " + csvName + "Line: " + c + " " + e.getMessage());
                } catch (BadScoreException e) {
                    System.out.println("Exception at csv file " + csvName + "Line: " + c + " " + e.getMessage());
                } catch (BadRatingException e) {
                    System.out.println("Exception at csv file " + csvName + "Line: " + c + " " + e.getMessage());
                } catch (BadNameException e) {
                    System.out.println("Exception at csv file " + csvName + "Line: " + c + " " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Exception at csv file " + csvName + "Line: " + c + " " + e.getMessage());
                }
                c++; // increment 
                
            } // End of creating array of movies
            csvFile.close(); // closing the file
            // serlize the file
            ObjectOutputStream serlizableFile = new ObjectOutputStream(
                    new FileOutputStream(csvName.substring(0, csvName.length() - 4) + ".ser")); // gets the name comedy.ser by replacing .csv from comedy.csv

            serlizableFile.writeObject(movieArray); // Writes the array of objects
            serlizableFile.close(); // close the file

        // exceptions to handle    
        } catch (FileNotFoundException e) {
            System.out.println("Could not open file " + csvName);
        } catch (IOException e) {
            System.out.println("Could not do serlization " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Different exception occured  in part2_Handler() " + e.getMessage());
        }

    }

    /**
     * This method do_part2, takes as input the part2.txt file with all the csv file
     * names.
     * reads the .txt file, loops through it line by line and uses the
     * part2_handler() method that takes as input
     * the .csv file String
     * 
     * @param part2ManifestFile the manifestpart2.txt file
     * @return String
     */

    public static String do_part2(String part2ManifestFile) { // takes as input the part2_manifest file
        Scanner part2File = null;
        try {
            part2File = new Scanner(new FileInputStream(part2ManifestFile)); // Reads file of part2Manifest

        } catch (FileNotFoundException e) {
            System.out.println("Could not open part2 manifest file in do_part2 ");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Different exception occured in do_part2");
            System.exit(0);
        }

        String SerlizableFileNames = "";
        while (part2File.hasNextLine()) { // Reads .csv line
            String csvName = part2File.nextLine();
            SerlizableFileNames += csvName.substring(0, csvName.length() - 4) + ".ser" + "\n";
            part2_Handler(csvName); // csv line put to the part2_Handler
        }
        part2File.close();

        PrintWriter part3File = null;
        try {
            part3File = new PrintWriter(new FileOutputStream("part3_manifest.txt"));

        } catch (FileNotFoundException e) {
            System.out.println("Could not open the file part3 manifest.txt");
            System.exit(0);

        } catch (Exception e) {
            System.out.println("Different exception occured in part3 manifest.txt");
            System.exit(0);
        }
        part3File.write(SerlizableFileNames);
        part3File.close();

        return "part3_manifest.txt";
    }

    /**
     * 
     * @param part3ManifestTxtFile takes the .txt file as paramter 
     * @return 2D movie array 
     * it opens the manifest 3 file and deserlizes the data 
     * the way this works is by opening first the part3ManifestTxt file, deserilizing the information using a for loop
     */
    public static Movie[][] do_part3(String part3ManifestTxtFile) { // takes as paramter the part3.txt file
        Scanner part3File = null;  // part3.txt file
        Movie[][]movieArrays = new Movie[17][]; // 2D Array
        
        try {

            part3File = new Scanner(new FileInputStream(part3ManifestTxtFile)); // opening the file

            ObjectInputStream serFileInput = null; // intializing the object input stream
            
            for(int i = 0; i< movieArrays.length; i++){
                try {
                    serFileInput = new ObjectInputStream(new FileInputStream(part3File.nextLine())); // open serilizable file
                    movieArrays[i] = (Movie[]) serFileInput.readObject(); //deserlize the object into the array of movies

                    serFileInput.close(); // closing the file
                } catch (FileNotFoundException e) {
                    movieArrays[i]= new Movie[0]; // each time exception caught, i'm creating an array of zero length
        
                } catch (Exception e) {
                    movieArrays[i]= new Movie[0];
                    System.out.println("Different exception occured in part3Handler() " + e.getMessage());
                }
            }        
            part3File.close(); // closing manifest_Part3 file

        } catch (FileNotFoundException e) {
            System.out.println("Could not read " + part3ManifestTxtFile);

        } catch (Exception e) {

            System.out.println("Different Exception occured in do_part3() " + e.getMessage());
        }
        return movieArrays;
        
    }


    /**
     * @param index index of the gener in the 2D Movie Array
     * @param movieArraMovies 
     * This method is to display the menu taking two parameters 
     */
    public static void mainMenu(int index, Movie[][] movieArraMovies){
        System.out.println("----------------------------");
        System.out.println("\t Main Menu");
        System.out.println("----------------------------");

        System.out.println("s   Select a movie array to navigate");
        System.out.println("n   Navigate "+ Movie.genreList[index]+ " movies ("+movieArraMovies[index].length+" records)" );
        System.out.println("x   Exit");

        System.out.println("----------------------------");
        System.out.print("Enter your choice: ");

    }
    /**
     * 
     * @param movieArrays Takes as paramter the 2D movie Array
     */
    public static void generSubMenu(Movie[][] movieArrays ){
        System.out.println("----------------------------");
        System.out.println("\tGenre Sub-Menu");
        System.out.println("----------------------------");
        for(int i = 0; i< movieArrays.length; i++){
            if(i ==4 || i == 5){ // this if statement is to only ensure the representation is clear and nice for the user
                System.out.println((i+1)+" " + Movie.genreList[i]+"\t\t("+ movieArrays[i].length+" movies)");    
                continue;
            }
            System.out.println((i+1)+" " + Movie.genreList[i]+"\t("+ movieArrays[i].length+" movies)");
            
        }

        System.out.println("18 Exit");
        System.out.println("----------------------------");
        System.out.print("Enter your choice: ");

    }


    /**
     * 
     * @param movieArrays 2D movie array as parameter
     * @param choice entered choice by the user
     * @param currentIndex current index in 2D array
     * @param currentPos current position within the array of movies
     * @param keyboard scanner 
     * 
     * The way this method works is by using do while loop 
     * within that loop, we create a menu for the user and handle the exception and cases might occur
     */
    public static void navigateThroughArray(Movie[][] movieArrays, String choice, int currentIndex, int currentPos, Scanner keyboard ){
        int previousposition=0;
        do{
            
            mainMenu(currentIndex, movieArrays);
            choice = keyboard.nextLine();

            //Check condition to notify the user if the input is wrong
            
            if(!choice.equalsIgnoreCase("s")&&
            !choice.equalsIgnoreCase("n") && !choice.equalsIgnoreCase("x"))
                System.out.println("Wrong input!");
            else{
                choice = choice.toLowerCase();
                switch(choice){
                    case "s": // case of select a movie array to navigate 
                        int optionS= 0;
                        do{ // handle number within 1 and 18
                            try{
                                generSubMenu(movieArrays);
                                optionS = keyboard.nextInt();
                                keyboard.nextLine(); // consume newLine character
                                if(optionS<1||optionS>18)
                                    System.out.println("Input must be between 1 and 18");
                            } catch(InputMismatchException e){
                                System.out.println("Input must be between 1 and 18");
                                optionS = 0;
                                keyboard.nextLine();
                            }catch(Exception e){
                                System.out.println("Different exception occured in handling optionS "+ e.getMessage());
                                System.exit(0);
                            }
                        }while(optionS <1 || optionS>18);

                        if(optionS == 18){
                            
                           break;
                        }
                        previousposition = currentIndex;
                        currentIndex = optionS-1; // set the new current index
                        
                        
                    break;

                    case "n": // case of navigating 

                        System.out.println("\nNavigating "+ Movie.genreList[currentIndex]+ " movies ("+ movieArrays[currentIndex].length+")");
                        System.out.print("Enter Your Choice: "); // accept choice from user

                        int optionN=0;   // Option n entered by the user
                        boolean isValid= false; // validate the option
                        //First we get the optionN entered by the user
                        while(!isValid){
                            try{

                                optionN = keyboard.nextInt(); // Accept a number from the user
                                keyboard.nextLine();
                                isValid = true;

                            } catch(InputMismatchException e){
                                System.out.print("You must enter a number please: ");
                                keyboard.nextLine();
                            }catch(Exception e){
                                System.out.println("Different exception occured in case n: "+ e.getMessage());
                                System.exit(0);
                            }
                        }
                        if(previousposition!= currentIndex) {// checking the previous and current position 
                            currentPos = 0; // if they are different we set it back to 0
                            previousposition = currentIndex;
                        }
                        // optionN holds now either a postive or negative or zero number
                        if(optionN ==0){ //If n = 0, back to main menu
                            System.out.println("Back to main menu . . .");
                            break; // return back to menu and break out of the navigation

                        }else if(optionN >0 ){ // n >0 
                            int endingIndex = Math.abs(optionN)-1; //  value of |n| - 1 

                            if(movieArrays[currentIndex].length ==0){ // if the moviearray has no elements
                                System.out.println("You don't have any records");

                            } else if(endingIndex>currentPos) { // if |n| -1 > current pos
                                
                                // case of |n|-1 > current pos
                                // when that's the case we first check if |n|-1 is greater than the length
                                if(endingIndex< movieArrays[currentIndex].length){ // check if the |n|-1 is less than the length of the movie
                                    // if Yes, then print from current pos to ending index
                                    for(int i = currentPos; i<= endingIndex; i++){
                                        System.out.println(movieArrays[currentIndex][i]);
                                    }
                                    currentPos = endingIndex; // change current postition to ending index 
                                } else{ //|n| -1 > length of the array
                                    // print from current pos to the end of the array e.g cp =5, length = 10, |n|-1 = 14
                                    for(int i = currentPos; i< movieArrays[currentIndex].length; i++){
                                        System.out.println(movieArrays[currentIndex][i]);
                                    }
                                    currentPos = movieArrays[currentIndex].length-1; // set the new current position
                                }
                                

                            } else if( endingIndex == currentPos){ // if endingiIndex == currentPos
                                System.out.println(movieArrays[currentIndex][currentPos]); // print the currentPos
                            
                            } else{ // case endingIndex < currentPos
                                //|n|-1 < currentPos
                                // for example currentPos = 8 and |n|-1 = 4
                                System.out.println("EOF has been reached"); // print this message

                                for(int i = currentPos; i< movieArrays[currentIndex].length; i++){
                                    System.out.println(movieArrays[currentIndex][i]);
                                } //print from current till the end of the array
                                currentPos = movieArrays[currentIndex].length -1 ; // largest index 
                            }

                        } else{  // ptionN < 0
                            // case of n<0
                            int endingIndex = Math.abs(optionN)-1; // |n| - 1
                            System.out.println(endingIndex);
                            if(movieArrays[currentIndex].length ==0){ // check of the array has records or not
                                System.out.println("You don't have any records");
                                
                            } else if(endingIndex ==0)
                                System.out.println(movieArrays[currentIndex][currentPos]); // print currentpos

                            
                            else if( currentPos > endingIndex){ // currentpos > |n|-1
                                // print from currentpos upwards to endingIndex = |n|-1
                                    // case of cp = 4 and |n|-1 = 2
                                    // print 4, 3, 2
                                    for( int i = currentPos ; i>= endingIndex; i--){
                                    System.out.println(movieArrays[currentIndex][i]);
                                    }
                                    currentPos = endingIndex;
                                
                                
                            } else{ // |n| -1 > currentPos
                                System.out.println("BOF has been reached"); //printed message
                                // print from currentPos to 0
                                for(int i = currentPos; i>=0; i--){
                                    System.out.println(movieArrays[currentIndex][i]);
                                }
                                currentPos = 0;
                            }

                        }
                    break;
                }
            }
        } while(!choice.equalsIgnoreCase("x")); // keep handling while choice is different than x or X


    }

    public static void main(String[] args) {
        // part 1's manifest file;
        String part1_manifest = "part1_manifest.txt";
        getContentNamesInTxtFile(part1_manifest, "Movies");
        
        // part 2's manifest file;
         
        String part2_manifest = do_part1(part1_manifest);
        
        // do part 2 

        String part3_manifest = do_part2("part2_manifest.txt");
        
        // do part 3    
        Movie[][] movieArrays = do_part3("part3_manifest.txt");
      
        String choice="";
        int currentIndex=0;
        int currentPos = 0;
        Scanner keyboard = new Scanner(System.in);
        System.out.println("*** Welcom to Achraf and Ali's Program ***");

        navigateThroughArray(movieArrays, choice, currentIndex, currentPos, keyboard); // navigate through the 2D Array

        keyboard.close();
        System.out.println("\nThank you For using the mainMenu!");
        System.out.println("See you again.\nProgram will terminate . . .");
        
        
        
    }
}

/*
 * --------NOTES:--------
 * do_part1() Method supposed to get from part1_manifest.txt the file names
 * read them and organize data in 17 csv files, and one bad_movie_records.txt
 * bad_movie_records: must include error and type, orginal movie, the name of
 * the input file which the issue happened in , and line number of the issue
 * it also creates part2_manifest.txt that has the 17 csv files names
 * 
 * handleContent() method takes as input the file name and does the organization
 * of the files.
 * it acts as a backer for the do_part1 method
 * 
 * cleanseData() cleans the data and throws exceptions caught in the
 * HandleContent()
 * 
 * writeToBadFile() writes the bad files down, but require to add a way it gets
 * the other data the need
 * 
 * writeToGoodFile() writes the good movie to the right data
 * 
 * splitData() Takes as input a String and returns an array of Strings organized
 * 
 * do_part2(String part2ManifestFile) takes as paramter part2ManifestFile.txt {family.csv, horror.csv . . .}
 * do_part2 method uses part2_Handler to handle the csv files and write them in the .ser file.
 * lastly, it writes the part3Manifest.text
 * 
 * do_part3(String part3Manifest) takes as paramater the part3Manifest.txt file { family.ser, horror.ser. . .}
 * returns a 2D movie array after deserilizing the objects   
 * 
 */