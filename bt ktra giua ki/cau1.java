interface Signal 
{
    double getAmplitude();
    double getFrequency();
    double getPhase();
}
class DiscreteSignal implements Signal {
    private double amplitude;
    private double frequency;
    private double phase;

    public DiscreteSignal(double amplitude, double frequency, double phase) {
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phase = phase;
    }

    @Override
    public double getAmplitude() {
        return amplitude;
    }

    @Override
    public double getFrequency() {
        return frequency;
    }

    @Override
    public double getPhase() {
        return phase;
    }

    @Override
    public String toString() {
        return "DiscreteSignal{" + "amplitude=" + amplitude + ", frequency=" + frequency + ", phase=" + phase + '}';
    }
}

class ContinuousSignal implements Signal {
    private double amplitude;
    private double frequency;
    private double phase;

    public ContinuousSignal(double amplitude, double frequency, double phase) {
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phase = phase;
    }

    @Override
    public double getAmplitude() {
        return amplitude;
    }

    @Override
    public double getFrequency() {
        return frequency;
    }

    @Override
    public double getPhase() {
        return phase;
    }

    @Override
    public String toString() {
        return "ContinuousSignal{" + "amplitude=" + amplitude + ", frequency=" + frequency + ", phase=" + phase + '}';
    }
}

public class Main {
    public static void main(String[] args) {
        Signal discreteSignal = new DiscreteSignal(1.0, 5.0, 0.0);
        System.out.println(discreteSignal);

        Signal continuousSignal = new ContinuousSignal(2.0, 10.0, 1.0);
        System.out.println(continuousSignal);
    }
}
