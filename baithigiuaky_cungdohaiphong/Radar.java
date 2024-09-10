public class Radar {
    public double X(int n) {
        if (n >= 0 && n <= 15) {
            return (1 - (n / 15.0)) * delta(n);
        }
        return 0;
    }
    private int delta(int n) {
        return (n == 0) ? 1 : 0;
    }
    public void testSignal() {
        int n = 4;  
        double result = X(n);  
        System.out.println("Ket qua cua X(" + n + ") = " + result);
    }
    public static void main(String[] args) {
        Radar radar = new Radar();
        radar.testSignal(); 
    }
}