package Week1.java;

public class PassObject {
    static void f(Number m) {
    m.i = 15;
    }
    public static void main(String[] args) {
    Number n = new Number();
    n.i = 14;
    f(n); // n.i = 15
    }
   }