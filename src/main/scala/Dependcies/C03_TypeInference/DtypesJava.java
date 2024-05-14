package C03_TypeInference;

public class DtypesJava {
    public static void main(String[] args)
    {
        // You have to manually infer data types in Java
        int courseDays = 4;

        // Java is statically typed, since you defined courseDays as int you cant assign "2" to it
        courseDays = 2;
    }
}
