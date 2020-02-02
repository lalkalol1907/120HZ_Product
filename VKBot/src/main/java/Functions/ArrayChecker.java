package Functions;

import java.util.ArrayList;

public class ArrayChecker {

    public static boolean ArrayCheck(ArrayList<String> array, String act){

        int n = array.size();
        for(int i = 0;i<n;i++){
            if (array.get(i).equals(act)){
                return (true);
            }
        }
        return (false);
    }
}
