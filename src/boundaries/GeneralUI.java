package boundaries;

public interface GeneralUI {
    // a generalised UI interface so that other kinds of UI can be created from this

    // the UI should also be able to retrieve and return user response
    public abstract Object getUserInput();

    // all kinds of UI should be able to show output to user
    public abstract void displayOutput(Object toDisplay);

    // method to run the UI
    public abstract void run();

    
}
