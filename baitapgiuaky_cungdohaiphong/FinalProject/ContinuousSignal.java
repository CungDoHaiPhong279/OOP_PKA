package baitapgiuaky_cungdohaiphong.FinalProject;

public class ContinuousSignal implements Signal {
    private final double amplitude;
    private final double period;
    private final double frequency;
    private final double wavelength;
    
    // Constructor
    public ContinuousSignal(double amplitude, double period, double frequency, double wavelength) {
    this.amplitude = amplitude;
    this.period = period;
    this.frequency = frequency;
    this.wavelength = wavelength;
    }
    
    @Override
    public double getAmplitude() {
    return amplitude;
    }
    
    @Override
    public double getPeriod() {
    return period;
    }
    
    @Override
    public double getFrequency() {
    return frequency;
    }
    
    @Override
    public double getWavelength() {
    return wavelength;
    }
    
    @Override
    public void displayInfo() {
    System.out.println("Continuous Signal Information:");
    System.out.println("Amplitude: " + amplitude);
    System.out.println("Period: " + period);
    System.out.println("Frequency: " + frequency);
    System.out.println("Wavelength: " + wavelength);
    }
    }