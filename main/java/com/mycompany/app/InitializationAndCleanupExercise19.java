public class InitializationAndCleanupExercise19 {
    public static void printStrings(String... strings) {
        for (String s : strings) {
            System.out.println(s);
        }
    }
    public static void main(String[] args) {
        printStrings("apple", "banana", "cherry");

        String[] fruits = {"kiwi", "mango", "pineapple"};
        printStrings(fruits);
    }

}
//giống spreach trong js 