package Socket.Student;

public class Utils {
    public static boolean isValidNonNumbericString(String str) {
        for (char character : str.toCharArray()) {
            if(Character.isDigit(character))
                return false;
        }
        return true;
    }

    public static boolean isInteger(String str) {
        for (char character : str.toCharArray()) {
            if(character < '0' || character > '9')
                return false;
        }
        return true;
    }

    public static boolean isDouble(String str) {
        String[] splitStr = str.split("\\.");
        if(splitStr.length > 2) return false;
        
        if(splitStr.length == 1)
            return isInteger(splitStr[0]);
        else return isInteger(splitStr[0]) && isInteger(splitStr[1]);
    }
}
