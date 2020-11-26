package com;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String LOG_FILE_NAME = "logfile.txt";

    public static void main(String[] args) throws IOException {

        ArrayList<Company> companyList = new ArrayList<>();
        Path path = Paths.get("in.txt");
        FileWriter fw = new FileWriter("out.csv", false);
        LOGGER.setLevel(Level.FINE);
        FileHandler fh = new FileHandler(LOG_FILE_NAME, true);
        fh.setLevel(Level.FINE);
        fh.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fh);
        LOGGER.fine("Program started\n");
        try (Scanner scanner = new Scanner(path)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] company = line.split(";");
                companyList.add(new Company(company));
            }
            List<Company> foundCompanies = getByRequest(companyList);
            for (Company s : foundCompanies) {
                for (String k : s.getStr()) {
                    fw.write(k + ";");
                }
                fw.write("\n");
            }
        } catch (IOException | ParseException a) {
            System.out.println(a.getMessage());
        }
        fw.close();
    }

    public static void showRequest() {
        System.out.println("1.by short name");
        System.out.println("2.by industry");
        System.out.println("3.by kind of activity");
        System.out.println("4.by foundation date (between date1 and date2)");
        System.out.println("5.by employees number (between 1 and 2)");
        System.out.println();
    }

    static List<Company> getByRequest(List<Company> companyList) throws IllegalArgumentException, ParseException {
        List<Company> result = new ArrayList<>();
        String requestString;
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                showRequest();
                System.out.println("Enter request: ");
                switch (sc.nextInt()) {
                    case 1:
                        System.out.println("Enter shortname: ");
                        String shortName = sc.next();
                        requestString = shortName;
                        result = Query.findByShortName(companyList, shortName);
                        break;
                    case 2:
                        System.out.println("Enter industry");
                        String industry = sc.next();
                        requestString = industry;
                        result = Query.findByIndustry(companyList, industry);
                        break;
                    case 3:
                        System.out.println("Enter kind of activity: ");
                        String activity = sc.next();
                        requestString = activity;
                        result = Query.findByActivity(companyList, activity);
                        break;
                    case 4:
                        System.out.println("Enter first dates: ");
                        requestString = sc.next();
                        Date firstDate = Company.dateFormat.parse(requestString);
                        System.out.println("Enter second dates: ");
                        requestString = sc.next();
                        Date secondDate = Company.dateFormat.parse(requestString);
                        result = Query.findByFoundationDate(companyList, firstDate, secondDate);
                        break;
                    case 5:
                        System.out.println("Enter first numbers: ");
                        requestString = sc.next();
                        int firstNumber = Integer.parseInt(requestString);
                        System.out.println("Enter second numbers: ");
                        requestString = sc.next();
                        int secondNumber = Integer.parseInt(requestString);
                        result = Query.findByAmountEmployee(companyList, firstNumber, secondNumber);
                        break;
                    default:
                        return result;
                }
                Main.LOGGER.fine("Find company " + " :" + requestString + ", Companies found: " + result.size() + "\n");
            }
        }
    }


}