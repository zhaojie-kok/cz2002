import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class FileReader{
    private String filePath;

    public FileReader(String fp){
        filePath = fp;
    }
    public String getPassword(String userId){
        
        return "";
    }
    public String getInfo(String userId){
        return "";
    }
    public String getCourseInfo(String courseCode){
        
        String separator = "|";

        ArrayList stringArray = (ArrayList)read(filePath);
        ArrayList al = new ArrayList(); 

        for (int i=0; i<stringArray.size(); i++)
        {
            String st = (String)stringArray.get(i);
            StringTokenizer star = new StringTokenizer(st, separator);
            if (courseCode == star.nextToken().trim())
            {
                return star.nextToken();
            }

        }
    }
    public boolean changeUserDetails(String[] details){
        return true;
    }
}