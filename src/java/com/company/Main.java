package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String inputFile = args[0];
        String outputFile = args[1];
        String logFile = args[2];
        try {
            processingQuery(inputFile, outputFile, logFile);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    static final String SHORTNAME = "SHORTNAME";
    static final String INDUSTRY = "INDUSTRY";
    static final String KIND_OF_ACTIVITY = "KIND_OF_ACTIVITY";
    static final String FOUNDATION_DATE = "FOUNDATION_DATE";
    static final String NUMBER_OF_EMPLOYEES = "NUMBER_OF_EMPLOYEES";
    static final String EXIT = "EXIT";
    public static void processingQuery(String inputFile, String outputFile, String logFile) {
        Queries companies = new Queries();
        try (Scanner scanner = new Scanner(new File(inputFile))) {
            while (scanner.hasNextLine()) {
                companies.addCompany(new Company(scanner.nextLine()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try (Scanner scanner = new Scanner(System.in);
             BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile));
             BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile, true))
        ) {
            System.out.println("Доступные запросы: \n" + SHORTNAME + " String, " + INDUSTRY + " String, " +
                    KIND_OF_ACTIVITY + " String, " + FOUNDATION_DATE + " Date Date, " + NUMBER_OF_EMPLOYEES + " int int, " +
                    EXIT);
            String query;
            while (!(query = scanner.next()).equals(EXIT)) {
                List<Company> result;
                switch (query) {
                    case SHORTNAME:
                        String sName = scanner.next();
                        query += " " + sName;
                        result = companies.findShortName(sName);
                        break;
                    case INDUSTRY:
                        String industry = scanner.next();
                        query += " " + industry;
                        result = companies.findIndustry(industry);
                        break;
                    case KIND_OF_ACTIVITY:
                        String type = scanner.next();
                        query += " " + type;
                        result = companies.findKindOfActivity(type);
                        break;
                    case FOUNDATION_DATE:
                        Date start = new SimpleDateFormat("дд/ММ/гггг").parse(scanner.next());
                        Date end = new SimpleDateFormat("дд/ММ/гггг").parse(scanner.next());
                        query += " " + start.toString() + " " + end.toString();
                        result = companies.findDateOfFoundation(start, end);
                        break;
                    case NUMBER_OF_EMPLOYEES:
                        int min = scanner.nextInt();
                        int max = scanner.nextInt();
                        query += " " + min + " " + max;
                        result = companies.findNumberOfEmployees(min, max);
                        break;
                    default:
                        System.out.println("запрос " + query + " не найден\n");
                        continue;
                }
                printLogFile(query, logWriter);
                printOutput(result, query, outputWriter);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void printLogFile(String str, BufferedWriter writer) throws IOException {
        writer.write(str + " " + LocalDateTime.now() + '\n');
    }

    private static void printOutput(List<Company> result, String query, BufferedWriter writer) throws IOException {
        writer.write(query + "\n");
        for (Company company : result) {
            writer.write(company.toString() + '\n');
        }
    }
}
