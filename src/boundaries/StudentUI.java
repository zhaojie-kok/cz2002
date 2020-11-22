package boundaries;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import entities.School;
import exceptions.FileReadingException;
import exceptions.KeyNotFoundException;
import exceptions.MissingSelectionException;
import managers.LoginMgr;
import managers.StudentSystem;

public class StudentUI extends Promptable {
    private static Scanner scn;
    private static String userId;
    private static StudentSystem system;

    public StudentUI(Scanner scn, String userId) {
        StudentUI.scn = scn;
        StudentUI.userId = userId;
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
            StudentUI.system = new StudentSystem(userId);
            mainMenu();
        } catch (Exception e) {
            displayOutput(e.getMessage());
        }
        shutDown();
    }

    public void mainMenu() {
        int choice = 0;

        String[] options = { "Show System Status", "Add Course", "Drop Course", "Check Courses Registered",
                "Check course vacancies", "Print Courses", "Swop to another Index", "Swop index with another student",
                "View TimeTable", "Exit" };

        while (choice != 9) {
            choice = promptChoice("++++++++++Main Menu++++++++++", options);
            switch (choice) {
                case 0:
                    checkSystemStatus();
                    break;
                case 1:
                    addCourse();
                    break;
                case 2:
                    dropCourse();
                    break;
                case 3:
                    printRegisteredCourses();
                    break;
                case 4:
                    checkVacanciesAvailable();
                    break;
                case 5:
                    printCourses();
                    break;
                case 6:
                    swopIndex();
                    break;
                case 7:
                    swopStudentIndex();
                    break;
                case 8:
                    viewTimeTable();
                    break;
                case 9:
                    break;
                default:
                    displayOutput("Choices must be between 1 and 8");
                    break;
            }
        }

    }

    private void checkSystemStatus() {
        displayOutput(system.getSystemStatus());
    }

    private int promptCourseSelection() {
        String courseCode = "";
        displayOutput("Please Enter Course Code: ");
        while (true) {
            courseCode = (String) getUserInput();
            courseCode = courseCode.toUpperCase();
            if (courseCode.equals("EXIT")) {
                return -1;
            }
            try {
                system.selectCourse(courseCode);
                return 1;
            } catch (KeyNotFoundException e) {
                displayOutput("No such course code exists, please re-enter or type \"exit\" to return to main menu");
            }
        }
    }

    private int promptIndexSelection() {
        String indexNo = "";
        displayOutput("Please enter an Index");
        while (true) {
            indexNo = (String) getUserInput();
            indexNo = indexNo.toUpperCase();
            if (indexNo.equals("EXIT")) {
                return -1;
            }
            try {
                system.selectIndex(indexNo);
                return 1;
            } catch (KeyNotFoundException e) {
                displayOutput("No such index no. exists, please re-enter or type \"exit\" to return to main menu");
            } catch (MissingSelectionException e) {
                displayOutput("Please select a course first");
                return -1;
            }
        }
    }

    private void addCourse() {
        int result;
        // prompt user for choice of course and index
        result = promptCourseSelection();
        if (result == -1) {
            return;
        }

        result = promptIndexSelection();
        if (result == -1) {
            return;
        }

        // try to add course in the system
        try {
            system.addCourse();
        } catch (Exception e) {
            displayOutput(e.getMessage());
            return;
        }
    }

    private void dropCourse() {
        int result;
        // prompt user for choice of course and index
        result = promptCourseSelection();
        if (result == -1) {
            return;
        }

        result = promptIndexSelection();
        if (result == -1) {
            return;
        }

        // try to remove the course in the system
        try {
            system.dropCourse();
            displayOutput("Successfully dropped course");
        } catch (Exception e) {
            displayOutput(e.getMessage());
        }
    }

    private void printRegisteredCourses() {
        displayOutput(system.checkRegisteredCourses("Course: %s\n Index: %s\n"));
    }

    private void checkVacanciesAvailable() {
        int result = promptCourseSelection();
        if (result == -1) {
            return;
        }

        HashMap<String, Integer> compiled = system.checkVacanciesAvailable();
        for (Map.Entry<String, Integer> index : compiled.entrySet()) {
            System.out.println(index.getKey() + ": " + index.getValue());
        }
    }

    private void printCourses() {
        // print all, b sch, or by filter
        String[] options = { "All courses", "By School", "By keywords" };

        int choice = promptChoice("Select Printing Option", options);
        String result = "";
        switch (choice) {
            case 0:
                result = system.printAllCourses();
                break;
            case 1:
                // prompt for options using the values of public enum School
                School[] allSch = School.values();
                choice = promptChoice("Select a school", allSch);
                result = system.printCoursesBySchool(allSch[choice], "%s\n");
                break;
            case 2:
                // prompt for key words to search for
                String filter = (String) getUserInput();
                result = system.printCoursesByStringFilter(filter, "%s\n");
                break;
        }

        displayOutput(result);
    }

    private void swopIndex() {
        int result;

        // prompt user for choice of course and index
        result = promptCourseSelection();
        if (result == -1) {
            return;
        }

        displayOutput("Select new Index to swop to");
        result = promptIndexSelection();
        if (result == -1) {
            return;
        }

        // try to swop index
        try {
            system.swopToIndex();
            displayOutput("Index Succesfully Changed");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void swopStudentIndex() {
        int result;

        // prompt user for choice of course
        result = promptCourseSelection();
        if (result == -1) {
            return;
        }

        // get the other student's login details
        displayOutput("Enter user ID of the student you are swopping with");
        String swopID = (String) getUserInput();
        displayOutput("Enter the other student's password");
        String swopPassword = (String) getUserInput();

        // verify the login details of the other student
        LoginMgr loginMgr = new LoginMgr();
        try {
            result = loginMgr.verifyLoginDetails(swopID, swopPassword);
        } catch (Exception e) {
            displayOutput(e.getMessage());
            ;
            return;
        }
        if (result != 1) { // if the login details are not verified to be a student
            displayOutput("Swopping details invalid!");
            return;
        } else {
            try {
                system.swopIndexWithStudent(swopID);
                displayOutput("Swop Successful");
            } catch (Exception e) {
                displayOutput(e.getMessage());
            }
        }
    }

    private void viewTimeTable() {
        try {
            displayOutput(system.getTimeTable());
<<<<<<< HEAD
        } catch (Exception e) {
            displayOutput(e.getMessage());
=======
        } catch (FileReadingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
>>>>>>> ab0be1a9caa95e91bcead216158e0a0fd585073f
        }
    }

    private void shutDown() {
        StudentUI.scn = null;
        StudentUI.userId = null;
        StudentUI.system = null;
    }
}
