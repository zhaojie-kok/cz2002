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

/**
 * UI for student users. Meant to abstract student system from users
 */
public class StudentUI extends Promptable {
    private static Scanner scn;
    private static String userId;
    private static StudentSystem system;

    /**
     * Constructor
     * NOTE: Student system will not be instantiated until {@link #run()} method is called
     * 
     * @param scn Scanner object for receiving input from user
     * @param userId ID of user
     */
    public StudentUI(Scanner scn, String userId) {
        StudentUI.scn = scn;
        StudentUI.userId = userId;
    }

    /**
     * Method to get input from user using Scanner object
     */
    @Override
    public Object getUserInput() {
        return scn.nextLine();
    }

    /**
     * Method to display outputs to user
     */
    @Override
    public void displayOutput(Object toDisplay) {
        System.out.println(toDisplay.toString());
    }

    /**
     * Method to start up the UI
     */
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

    /**
     * Main menu of UI. All other functional methods are called from here
     */
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

    /**
     * Method to check the status of the system
     */
    private void checkSystemStatus() {
        displayOutput(system.getSystemStatus());
    }

    /**
     * Method to prompt user to select a course.Course selected will be recorded and
     * used for subsequent methods in Staff system until removed
     * 
     * @return int: 1 denoting course successfully selected, -1 denoting user's
     *         choice to exit selection
     */
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

    /**
     * Method to prompt user to select an index. Index selected will be recorded and
     * used for subsequent methods in Staff system until removed
     * 
     * @return int: 1 denoting index successfully selected, -1 denoting user's
     *         choice to exit selection
     */
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

    /**
     * Method to allow students to register for a course
     * user will need to enter arguments for {@link managers.StudentManager#addCourse()} method
     * user will be automatically added to waitlist if the index is full
     */
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
            result = system.addCourse();
            switch(result) {
                case 0:
                    displayOutput("Course is full, you have been added to waitlist");
                    break;
                case 1:
                    displayOutput("Successfully registered for course");
                    break;
            }
        } catch (Exception e) {
            displayOutput(e.getMessage());
            return;
        }
    }

    /**
     * Method to allow students to drop a course, whether from their registered or waitlisted courses
     */
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

    /**
     * Displays a list of courses the user has been registered for
     */
    private void printRegisteredCourses() {
        displayOutput(system.checkRegisteredCourses());
    }

    /**
     * Displays the list of vacancies available for each course in the system
     */
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

    /**
     * Method to display a list of courses
     * User can choose to filter by school with {@link managers.StudentSystem#printCoursesBySchool()} method
     * or users can filter using keywords with {@link managers.StudentSystem#printCoursesByStringFilter()} method
     */
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

    /**
     * Method to allow a student to change to another index in a course
     */
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

    /**
     * Method to allow 2 students to swop their indexes with each other
     * NOTE: userID and password of the swopping student is required
     */
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
        displayOutput("Enter the other student's password"); //TODO: hide password
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

    /**
     * Method for students to view their existing timetable
     */
    private void viewTimeTable() {
        try {
            displayOutput(system.getTimeTable());
        } catch (FileReadingException e) {
            displayOutput(e.getMessage());
        }
    }

    /**
     * Method to shut down the UI, to ensure all variables stored are deleted
     */
    private void shutDown() {
        StudentUI.scn = null;
        StudentUI.userId = null;
        StudentUI.system = null;
    }
}
