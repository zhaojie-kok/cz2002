package exceptions;

public class MissingSelectionException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -7358761558962669134L;
    String cName;
    public MissingSelectionException(Class c){
        cName = c.getSimpleName();
    }

    public String toString(){
        return "Object of class " + cName + " not yet selected";
    }

}