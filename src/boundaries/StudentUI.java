package boundaries;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import entities.School;
import managers.LoginMgr;
import managers.StudentSystem;

public class StudentUI extends Promptable {
    private static Scanner scn;
    private static StudentSystem system;

    public StudentUI(Scanner scn, String userId) {
        StudentUI.scn =  scn;
        StudentUI.system = new StudentSystem(userId);
    }

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

    public void mainMenu() {
        int choice = 0;
        
        String[] options = {
            "Show System Status",
            "Add Course", 
            "Drop Course",
            "Check Courses Registered",
            "Check course vacancies",
            "Print Courses",
            "Swop to another Index",
            "Swop index with another student",
            "Exit"
        };
        
        while (choice != 8) {
            choice = promptChoice("++++++++++Main Menu++++++++++", options);
            switch(choice) {
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
                    break;
                default:
                    displayOutput("Choices must be between 1 and ");
                    break;
            }
        }
        
    }

    private void checkSystemStatus() {
        displayOutput(system.getSystemStatus());
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
        String indexNo = "";
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

    private void addCourse(){
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
        //TODO: Change to try catch
        result = system.addCourse();
        switch(result) {
            case 1:
                displayOutput("Course Successfully registered");
                break;
            case 0:
                displayOutput("No available vacancies, registered for waitlist");
                break;
            case -1:
                displayOutput("Timetable clash! Please choose another index");
                break;
            case -2:
                displayOutput("You have already registered for this course");
                break;
            case -3:
                displayOutput("Insufficient Academic Units, please speak to school administrator");
                break;
        }
        //TODO: Deselect course and index
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
        // TODO: Change to try catch
        result = system.dropCourse();
        switch(result) {
            case 1:
                displayOutput("Course Successfully Dropped");
                break;
            case 0:
                displayOutput("Removed from course waitlist");
                break;
            case -1:
                displayOutput("Error Occurred, Please contact system administrator");
                break;
        }
        //TODO: Deselect course and index
    }

    private void printRegisteredCourses(){
        displayOutput(system.checkRegisteredCourses("Course: %s\n Index: %s\n"));
    }

    private void checkVacanciesAvailable(){
        int result = promptCourseSelection();
        if (result == -1) {
            return;
        }

        HashMap<String, Integer> compiled = system.checkVacanciesAvailable();
        for (Map.Entry<String, Integer> index: compiled.entrySet()) {
            System.out.println(index.getKey() + ": " + index.getValue());
        }
    }

    private void printCourses() {
        // print all, b sch, or by filter
        String[] options = {"All courses", "By School", "By keywords"};

        int choice = promptChoice("Select Printing Option", options);
        String result = "";
        switch(choice) {
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
        // TODO: change to try catch
        result = system.swopToIndex();
        switch (result) {
            case -1:
                displayOutput("You are already registered in this index");
                break;
            case -2:
                displayOutput("This index is full");
                break;
            case -3:
                displayOutput("New index clashes with existing timetable");
                break;
            default:
                displayOutput("Index Succesfully Changed");
                break;
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
        result = loginMgr.verifyLoginDetails(swopID, swopPassword);
        if (result != 1) { //  if the login details are not verified to be a student
            displayOutput("Swopping details invalid!");
            return;
        } else {
            result = system.swopIndexWithStudent(swopID);
            // TODO: change to try catch
            switch(result) {
                case 1:
                    displayOutput("Swop Successful");
                    break;
                case 0:
                    displayOutput("Swop Unsuccessful due to timetable clash");
                    break;
                case -1:
                    displayOutput("One of the students is not registered for this course");
                    break;
                case -2:
                    displayOutput("Both students are in the same index");
                    break;
                case -3:
                    displayOutput("Unknown error, please contact system administrator");
                    break;
            }
        }

    }

    private void shutDown() {
        // TODO: run shut down procedure
    }

    @Override
    public void run() {
        mainMenu();
        shutDown();
    }
}
