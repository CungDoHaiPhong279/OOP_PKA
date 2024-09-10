import java.util.Arrays;

interface Signal {
    double getAmplitude();
    double getFrequency();
    double getPhase();
}

class DiscreteSignal implements Signal {
    private double[] signal;
    private int length;

    public DiscreteSignal(double[] signal) {
        this.signal = signal;
        this.length = signal.length;
    }

    @Override
    public double getAmplitude() {
        return Arrays.stream(signal).max().orElse(0);
    }

    @Override
    public double getFrequency() {
        return 0;
    }

    @Override
    public double getPhase() {
        return 0;
    }

    public double calculateXn(int n) {
        double result = 0;
        for (int k = 0; k < length; k++) {
            result += signal[k] * delta(n - k);
        }
        return result;
    }

    private int delta(int n) {
        return (n == 0) ? 1 : 0;
    }

    @Override
    public String toString() {
        return "DiscreteSignal{signal=" + Arrays.toString(signal) + '}';
    }
}

class Radar {
    public double analyzeSignal(DiscreteSignal signal, int n) {
        double Xn;
        if (n >= 0 && n <= 15) {
            Xn = (1 - n / 15.0);
        } else {
            Xn = 0;
        }
        return Xn * signal.calculateXn(n);
    }
}

public class Main {
    public static void main(String[] args) {
        double[] sampleSignal = {1.0, 2.0, 3.0, 4.0, 5.0};
        DiscreteSignal discreteSignal = new DiscreteSignal(sampleSignal);

        Radar radar = new Radar();

        int n = 4;
        double result = radar.analyzeSignal(discreteSignal, n);
        System.out.println("Kết quả phân tích tín hiệu rời rạc tại n = " + n + " là: " + result);
    }
}
