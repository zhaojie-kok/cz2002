public interface UI {
    // a generalised UI interface so that other kinds of UI can be created from this

    // all kinds of UI should require login
    public int promptLogin();

    // all kinds of UI should be able to prompt user for input
    // and store it in some object
    public void getUserInput(Object storageObject);

    // alternatively, the UI should also be able to retrieve and return user response
    public Object getUserInput();

    // all kinds of UI should be able to show output to user
    public void displayOutput(Object toDisplay);
}
