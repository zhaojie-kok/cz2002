package boundaries;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

import entities.School;
import exceptions.*;
import managers.StaffSystem;

public class StaffUI extends Promptable {
    private static Scanner scn;
    private static String userId;
    private static StaffSystem system;

    public StaffUI(Scanner scn, String userId) {
        StaffUI.scn = scn;
        StaffUI.userId = userId;
    }

    @Override
    public Object getUserInput() {
        return scn.nextLine();
    }

    @Override
    public void displayOutput(Object toDisplay) {
        System.out.println(toDisplay.toString());
    }

    @Override
    public void run() {
        // set up the system
        try {
            StaffUI.system = new StaffSystem(userId);
            mainMenu();
        } catch (FileReadingException e) {
            e.printStackTrace();
            displayOutput(e.getMessage());
        }
        shutDown();
    }

    private void mainMenu() {
        int choice = 0;

        String[] options = { "Change student access period", "Add Student to system", "Add course to system",
                "Update course information", "Add index to a course", "Update index information",
                "Print Students in a course index", "Print Students in a course", "Exit" };

        while (choice != 8) {
            choice = promptChoice("++++++++++Main Menu++++++++++", options);
            switch (choice) {
                case 0:
                    updateAccessPeriod();
                    break;
                case 1:
                    addStudent();
                    break;
                case 2:
                    addCourse();
                    break;
                case 3:
                    updateCourse();
                    break;
                case 4:
                    addIndex();
                    break;
                case 5:
                    updateIndex();
                    break;
                case 6:
                    printIndexStudents();
                    break;
                case 7:
                    printCourseStudents();
                    break;
                case 8:
                    break;
                default:
                    displayOutput("Choices must be between 1 and 8");
                    break;
            }
        }
    }

    private int promptCourseSelection() {
        String courseCode = "";
        displayOutput("Please Enter Course Code: ");
        while (true) {
            courseCode = (String) getUserInput();;
            courseCode = courseCode.toUpperCase();
            if (courseCode.equals("EXIT")){
                return -1;
            }
            try {
                system.selectCourse(courseCode);
                return 1;
            } catch (KeyNotFoundException e) {
                displayOutput(e.getMessage());
            }
        }
    }

    private int promptIndexSelection() {
        String indexNo = "";
        displayOutput("Please enter an Index");
        while (true) {
            indexNo = (String) getUserInput();
            indexNo = indexNo.toUpperCase();
            if (indexNo.equals("EXIT")){
                return -1;
            }
            try {
                system.selectIndex(indexNo);
                return 1;
            } catch (KeyNotFoundException e) {
                displayOutput("Index No. could not be found, please re-enter or type \"exit\" to return to main menu");
            } catch (MissingSelectionException e) {
                displayOutput("Please select a course first");
            }
        }
    }

    private int promptStudentSelection() {
        String identifier = "";
        displayOutput("Please enter a student's user ID or matriculation number");
        while (true) {
            identifier = (String) getUserInput();;
            identifier = identifier.toUpperCase();
            if (identifier.equals("EXIT")) {
                return -1;
            }
            try {
                system.selectStudent(identifier);
                return 1;
            } catch (KeyNotFoundException e) {
                displayOutput("Unable to identify student. Please re-enter or type \"exit\" for return to main menu");
            }
        }
    }

    private int promptIntegerInput() {
        int response;
        while (true) {
            try {
                response = Integer.parseInt((String) getUserInput());
                return response;
            } catch (NumberFormatException e) {
                displayOutput("Input must be formatted as integer");
            }
        }
    }

    private int promptIntegerInput(int lowerLim, int upperLim) {
        int response;
        while (true) {
            try {
                response = Integer.parseInt((String) getUserInput());
                if (response > upperLim || response < lowerLim) {
                    displayOutput("Must be from " + lowerLim + " to " + upperLim);
                } else {
                    return response;
                }
            } catch (NumberFormatException e) {
                displayOutput("Input must be formatted as integer");
            }
        }
    }

    private LocalDateTime getDateInput() {
        int day, mth, yr, hr, min;
        LocalDateTime ldt = LocalDateTime.now();
        // get new year;
        displayOutput("Enter new year");
        do {
            yr = promptIntegerInput();
            if (yr < ldt.getYear() || yr > (ldt.getYear() + 1)) {
                displayOutput("Year must be current year or up to 1 year in advance");
            }
        } while (yr < ldt.getYear() || yr > (ldt.getYear() + 1));

        // get new mth
        displayOutput("Enter new month:");
        do {
            mth = promptIntegerInput();
            if (!(mth >= 1 && mth <= 12)) {
                displayOutput("Month must be integer from 1 to 12");
            }
        } while (!(mth >= 1 && mth <= 12));

        int maxDay;
        switch (mth) {
            // months with 31 days
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                maxDay = 31;
                break;
            case 2:
                boolean isLeapYear = yr % 4 == 0 && (yr % 100 != 0 || yr % 400 == 0);
                maxDay = isLeapYear ? 29 : 28;
                break;
            default:
                maxDay = 30;
        }

        // get new day
        displayOutput("Enter day of month");
        do {
            day = promptIntegerInput();
            if (!(day >= 1 && day <= maxDay)) {
                displayOutput("Day must be integer from 1 to " + maxDay);
            }
        } while (!(day >= 1 && day <= maxDay));

        // get new hour
        displayOutput("Enter new hour of day (24 hour format)");
        do {
            hr = promptIntegerInput();
            if (!(hr >= 0 && hr <= 23)) {
                displayOutput("Hour must be integer from 1 to 23");
            }
        } while (!(hr >= 0 && hr <= 23));

        // get new minute
        displayOutput("Enter new minute");
        do {
            min = promptIntegerInput();
            if (!(min >= 0 && min <= 59)) {
                displayOutput("Minute must be integer from 1 to 59");
            }
        } while (!(min >= 0 && min <= 59));

        // set the date and time
        ldt = LocalDateTime.of(yr, mth, day, hr, min);
        return ldt;
    }

    private LocalTime getTimeInput() {
        int hr;
        int min;
        // get new hour
        displayOutput("Enter new hour of day (24 hour format)");
        do {
            hr = promptIntegerInput();
            if (!(hr >= 0 && hr <= 23)) {
                displayOutput("Hour must be integer from 1 to 23");
            }
        } while (!(hr >= 0 && hr <= 23));

        // get new minute
        displayOutput("Enter new minute");
        do {
            min = promptIntegerInput();
            if (!(min >= 0 && min <= 59)) {
                displayOutput("Minute must be integer from 1 to 59");
            }
        } while (!(min >= 0 && min <= 59));

        return LocalTime.of(hr, min);
    }

    private void updateAccessPeriod() {
        int result;

        // prompt user for selection of student
        result = promptStudentSelection();
        if (result == -1) {
            return;
        }

        displayOutput("Start time for new access Period");
        LocalDateTime start = getDateInput();
        displayOutput("End time for new access Period");
        LocalDateTime end = getDateInput();
        LocalDateTime[] newAccessPeriod = { start, end };

        if (system.updateAccessPeriod(newAccessPeriod)) {
            displayOutput("Access Period changed successfully");
        } else {
            displayOutput("Error occured, please inform system administrator");
        }
        ;
    }

    private void printCourseStudents() {
        int result;

        // prompt user to select a course
        result = promptCourseSelection();
        if (result == -1) {
            return;
        }

        // TODO: make try catch
        String toPrint = system.printStudentsbyCourse();
        displayOutput(toPrint);
    }

    private void printIndexStudents() {
        int result;

        // prompt user to select a course
        result = promptCourseSelection();
        if (result == -1) {
            return;
        }

        // prompt user to select an index
        result = promptIndexSelection();
        if (result == -1) {
            return;
        }

        // TODO: make try catch
        String toPrint = system.printStudentsbyIndex();
        displayOutput(toPrint);
    }

    private void addCourse() {
        // get the school that is teaching the course
        int choice = promptChoice("Select which school the course is under", School.values());
        School school = School.values()[choice];

        // get the course code of the new course
        displayOutput("Enter new course code");
        String courseCode = (String) getUserInput();

        // get the course name of the new course
        displayOutput("Enter new course name");
        String courseName = (String) getUserInput();

        // get the number of AUs the course will carry
        displayOutput("How many academic units does this course carry?");
        int acadU;
        do {
            acadU = promptIntegerInput();
            if (acadU <= 0) {
                displayOutput("Courses must carry at least 1 academic unit");
            }
        } while (acadU <= 0);

        try {
            system.addCourse(courseCode, courseName, school, acadU);
        } catch (KeyClashException e) {
            displayOutput(e.getMessage());
        }
    }

    private void updateCourse() {
        int result;

        // prompt user to select a course
        result = promptCourseSelection();
        if (result == -1) {
            return;
        }

        String newCourseCode = null;
        String newCourseName = null;
        School newSchool = null;
        String[] options = { "Change course code", "Change name of course", "Change school of course",
                "No further changes" };
        int choice;

        // prompt user for which detail of course to be changed
        do {
            displayOutput("Current course information:");
            displayOutput(system.getCourseInfo());
            choice = promptChoice("Which detail of the course needs to be changed", options);
            switch (choice) {
                case 0:
                    displayOutput("Enter new course code");
                    newCourseCode = (String) getUserInput();
                    break;
                case 1:
                    displayOutput("Enter new name");
                    newCourseName = (String) getUserInput();
                    break;
                case 2:
                    int schChoice = promptChoice("Choose the new school", School.values());
                    newSchool = School.values()[schChoice];
                    break;
                case 3:
                    break;
                default:
                    displayOutput("Please choose between options 1 to 4");
                    break;
            }
        } while (choice != 3);

        // pass the new values to the system to update
        system.updateCourse(newCourseCode, newCourseName, newSchool);
    }

    private void createLessonDetails() {
        int choice;
        String venue = null;
        String type = null;
        int dayOfWk = -1;
        int evenOdd = -1;
        LocalTime start = null;
        LocalTime end = null;

        String[] options = { "Venue", "Lesson Type", "Day of week", "Even or Odd week", "Start Time", "End Time",
                "No further Changes" };
        do {
            choice = promptChoice("Which detail of the lesson to edit", options);
            switch (choice) {
                case 0:
                    displayOutput("Enter venue name");
                    venue = (String) getUserInput();;
                    break;
                case 1:
                    displayOutput("Enter class type");
                    type = (String) getUserInput();;
                    break;
                case 2:
                    dayOfWk = promptChoice("Choose day of week (1 is Monday, 7 is Sunday)", DayOfWeek.values());
                    dayOfWk++; // since the prompt choice starts at 0 instead of 1
                    break;
                case 3:
                    evenOdd = promptChoice("Choose if lesson is even or odd type",
                            new String[] { "Even", "Odd", "Both" });
                    break;
                case 4:
                    start = getTimeInput();
                    break;
                case 5:
                    end = getTimeInput();
                    break;
                case 6:
                    try {
                        system.selectLessonDetails(venue, type, dayOfWk, evenOdd, start, end);
                    } catch (MissingParametersException m) {
                        displayOutput(m.getMessage());
                    } catch (OutOfRangeException o) {
                        displayOutput(o.getMessage());
                    }
                    break;
                default:
                    displayOutput("Please choose between options 1 to 7");
            }
        } while (choice != 6);
    }

    private void addIndex() {
        int result;

        // prompt user to select a course
        result = promptCourseSelection();
        if (result == -1) {
            return;
        }

        // get the indexNo of the course
        displayOutput("Please enter course index number");
        String indexNo = (String) getUserInput();

        // get the number of slots for the course
        displayOutput("Enter the number of vacancies");
        int slots = promptIntegerInput(1, 50); // each index can only have up to 50 people due to covid restrictions

        // create the new timetable
        int choice;
        do {
            choice = promptChoice("Would you like to add lessons to the index (lessons cannot be added after creation)",
                    new String[] { "Yes", "No" });
            if (choice == 0) {
                createLessonDetails();
            }
        } while (choice != 1);

        try {
            system.addIndex(indexNo, slots);
        } catch (KeyClashException e) {
            displayOutput(e.getMessage());
        }
    }

    private void updateIndex() {
        /**
         * Update the index's index No and the total number of slots
         * NOTE: other details like the timetable cannot be changed after creating the index
         */
        int result;

        // prompt user to select a course
        result = promptCourseSelection();
        if (result == -1) {
            return;
        }

        // prompt user to select an index
        result = promptIndexSelection();
        if (result == -1) {
            return;
        }

        String indexNo = null;
        int slots = -1;
        int choice;
        String[] options = {"Change index number", "change total capacity", "No further changes"};
        do {
            choice = promptChoice("Select information to change", options);
            switch(choice) {
                case 0:
                    displayOutput("Enter new index number");
                    indexNo = (String) getUserInput();;
                    break;
                case 1:
                    displayOutput("Enter new total capacity (from 1 to 50)");
                    slots = promptIntegerInput(1, 50);
                    break;
                case 2:
                    break;
                default:
                    displayOutput("Please choose between 1 to 3");
            }
        } while (choice != 2);

        try {
            system.updateIndex(indexNo, slots);
        } catch (OutOfRangeException e) {
            displayOutput(e.getMessage());
        }
    }

    private void addStudent() {
        String userId = null;
        String name = null;
        String gender = null;
        String nationality = null;
        String matricNo = null;
        LocalDateTime startAccess = null;
        LocalDateTime endAccess = null;
        String password = null;

        String[] options = {"userID", "name", "gender", "nationality", "matriculation number", "Access Period Start", "Access Period End", "Password", "No further changes"};
        int choice;
        do {
            choice = promptChoice("Select student info to edit", options);
            switch (choice) {
                case 0:
                    displayOutput("Enter userID");
                    userId = (String) getUserInput();
                    break;
                case 1:
                    displayOutput("Enter student name");
                    name = (String) getUserInput();
                    break;
                case 2:
                    displayOutput("Enter student gender");
                    gender = (String) getUserInput();
                    break;
                case 3:
                    displayOutput("Enter student nationality");
                    nationality = (String) getUserInput();
                    break;
                case 4:
                    displayOutput("Enter student matriculation number");
                    matricNo = (String) getUserInput();
                    break;
                case 5:
                    displayOutput("Student's access period - start");
                    startAccess = getDateInput();
                    break;
                case 6:
                    displayOutput("Student's access period - end");
                    endAccess = getDateInput();
                    break;
                case 7:
                    displayOutput("Enter password for student account");
                    password = (String) getUserInput();
                    break;
                case 8:
                    LocalDateTime[] accessPeriod = {startAccess, endAccess};
                    try {
                        system.addStudent(userId, name, gender, nationality, matricNo, accessPeriod, password);
                    } catch (Exception e) {
                        //TODO: handle exception
                        choice = -1;
                    }
            }
        } while (choice != 8);
    }

    private void shutDown() {
        StaffUI.scn = null;
        StaffUI.system = null;
        StaffUI.userId = null;
    }
}
