package main.java.com.mycompany.app;
public class Dog {
    public void bark() {
        System.out.println("toi xau trai");
    }

    public void bark(double height) {
        System.out.println("A cao hon B " + height + " cm");
    }

    public void bark(int times) {
        for (int i = 0; i < times; i++) {
            bark();
        }
    }

    public void bark(String sound) {
        System.out.println(sound);
    }

    public void bark(int times, double height) {
        for (int i = 0; i < times; i++) {
            System.out.print("Woof! ");
        }
        System.out.println("Woof! Woof! at volume " + height);
    }

}