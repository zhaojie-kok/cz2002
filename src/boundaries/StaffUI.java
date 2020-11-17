package boundaries;

import java.util.Calendar;
import java.util.Scanner;

import managers.StaffSystem;
import managers.StudentManager;

public class StaffUI extends GeneralUI {
    private static Scanner scn;
    private static StaffSystem system;

    public StaffUI(Scanner scn, String userId) {
        StaffUI.scn = scn;
        StaffUI.system = new StaffSystem(userId);
    }

    private void mainMenu() {
        int choice = 0;

        String[] options = {
            "Change student access period",
            "Add Student to system",
            "Update course information",
            "Add course to system",
            "Print Students in a course index",
            "Print Students in a course",
            "Exit"
        };

        while (choice != 6) {
            choice = promptChoice("++++++++++Main Menu++++++++++", options);
            switch (choice) {
                case 0:
                    updateAccessPeriod();
                    break;
                case 1:
                    addStudent();
                    break;
                case 2:
                    updateCourse();
                    break;
                case 3:
                    addCourse();
                    break;
                case 4:
                    printIndexStudents();
                    break;
                case 5:
                    printCourseStudents();
                    break;
                case 6:
                    break;
                default:
                    displayOutput("Choices must be between 1 and ");
                    break;
            }
        }
    }

    private int promptCourseSelection() {
        int result;
        String courseCode = "";
        displayOutput("Please Enter Course Code: ");
        do {
            getUserInput(courseCode);
            courseCode = courseCode.toUpperCase();
            result = system.selectCourse(courseCode);
            // TODO: change to try catch
            if (result != 1) {
                displayOutput("Course code is wrong, please re-enter or type \"exit\" to return to main menu");
            }
        } while (result != 1 && !courseCode.equals("exit"));

        if (courseCode.equals("exit")) {
            return -1;
        } else {
            return 1;
        }
    }

    private int promptIndexSelection() {
        int result;
        String indexNo= "";
        displayOutput("Please enter an Index");
        do {
            getUserInput(indexNo);
            indexNo = indexNo.toUpperCase();
            result = system.selectIndex(indexNo);

            // TODO: change to try catch
            if (result == -1) {
                displayOutput("Please select a course first");
                return -1;
            } else if (result !=1 && !indexNo.equals("exit")) {
                displayOutput("Index No. is wrong, please re-enter or type \"exit\" to return to main menu");
            }
        } while (result != 1 && !indexNo.equals("exit"));

        if (indexNo.equals("exit")) {
            return -1;
        } else {
            return 1;
        }
    }

    private int promptStudentSelection() {
        int result;
        String identifier = "";
        displayOutput("Please enter a student's user ID or matriculation number");
        do {
            getUserInput(identifier);
            identifier = identifier.toUpperCase();
            result = system.selectStudent(identifier);

            // TODO: change to try catch
            if (result != 1) {
                displayOutput("Unable to identify student. Please re-enter or type \"exit\" for return to main menu");
            }
        } while (result != 1 && !identifier.equals("exit"));

        if (identifier.equals("exit")) {
            return -1;
        } else {
            return 1;
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

    private Calendar getDateInput() {
        int day, mth, yr, hr, min;
        Calendar cal = Calendar.getInstance();
        // get new year;
        displayOutput("Enter new year");
        do {
            yr = promptIntegerInput();
            if (yr<cal.getWeekYear() || yr>(cal.getWeekYear() + 1)) {
                displayOutput("Year must be current year or up to 1 year in advance");
            }
        } while (yr<cal.getWeekYear() || yr>(cal.getWeekYear() + 1));

        // get new mth
        displayOutput("Enter new month:");
        do {
            mth = promptIntegerInput();
            if (!(mth>=1 && mth<=12)) {
                displayOutput("Month must be integer from 1 to 12");
            }
        } while (!(mth>=1 && mth<=12));

        int maxDay;
        switch(mth) {
            // months with 31 days
            case 1, 3, 5, 7, 8, 10, 12:
                maxDay = 31;
                break;
            case 2:
                boolean isLeapYear = yr%4 == 0 && (yr%100 != 0 || yr%400 == 0);
                maxDay = isLeapYear ? 29 : 28;
                break;
            default:
                maxDay = 30;
        }

        // get new day
        displayOutput("Enter day of month");
        do {
            day = promptIntegerInput();
            if (!(day>=1 && day<=maxDay)) {
                displayOutput("Day must be integer from 1 to " + maxDay);
            }
        } while (!(day>=1 && day<=maxDay));

        // get new hour
        displayOutput("Enter new hour of day (24 hour format)");
        do {
            hr = promptIntegerInput();
            if (!(hr>=0 && hr<=23)) {
                displayOutput("Hour must be integer from 1 to 23");
            }
        } while (!(hr>=0 && hr<=23));

        // get new minute
        displayOutput("Enter new minute");
        do {
            min = promptIntegerInput();
            if (!(min>=0 && min<=59)) {
                displayOutput("Minute must be integer from 1 to 59");
            }
        } while (!(min>=0 && min<=59));

        // set the date and time
        cal.set(yr, mth, day, hr, min);
        return cal;
    }_

    @Override
    public void getUserInput(Object storageObject) {
        storageObject = scn.nextLine();
    }

    @Override
    public Object getUserInput() {
        return scn.nextLine();
    }

    @Override
    public void displayOutput(Object toDisplay) {
        System.out.println(toDisplay.toString());
    }

    private void updateAccessPeriod() {
        int result;

        // prompt user for selection of student
        result = promptStudentSelection();
        if (result == -1) {
            return;
        }

        displayOutput("Start time for new access Period");
        Calendar start = getDateInput();
        displayOutput("End time for new access Period");
        Calendar end = getDateInput();
        Calendar[] newAccessPeriod = {start, end};

        if (system.updateAccessPeriod(newAccessPeriod)) {
            displayOutput("Access Period changed successfully");
        } else {
            displayOutput("Error occured, please inform system administrator");
        };
    }

    private void printCourseStudents() {
        int result;

        // prompt user to select a course
        result = promptCourseSelection();
        if (result == -1) {
            return;
        }

        system.printStudentsbyCourse(courseCode)
    }

    private void printIndexStudents() {
    }

    private void addCourse() {
    }

    private void updateCourse() {
    }

    private void addStudent() {
    }

    private void 

    @Override
    public void run() {
        mainMenu();
        shutDown();
    }
    
}
