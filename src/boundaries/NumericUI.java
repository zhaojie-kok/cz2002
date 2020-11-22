package boundaries;

public interface NumericUI {
    public abstract int promptIntegerInput();

    public abstract int promptIntegerInput(int upperlim, int lowerlim);
}
