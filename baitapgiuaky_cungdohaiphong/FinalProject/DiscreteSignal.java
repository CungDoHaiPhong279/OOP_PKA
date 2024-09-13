package baitapgiuaky_cungdohaiphong.FinalProject;

public class DiscreteSignal {
    public double x(int n, double[] xk) {
        double sum = 0;
        for (int k = 0; k < xk.length; k++) {
            sum += xk[k] * delta(n - k);
        }
        return sum;
    }
    
    private int delta(int n) {
        return (n == 0) ? 1 : 0;
    }
}