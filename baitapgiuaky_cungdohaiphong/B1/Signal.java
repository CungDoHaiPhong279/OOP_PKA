
// Giao diện Signal
public interface Signal {
    // Phương thức để lấy biên độ
    double getAmplitude();
    
    // Phương thức để lấy chu kỳ
    double getPeriod();
    
    // Phương thức để lấy tần số
    double getFrequency();
    
    // Phương thức để lấy bước sóng
    double getWavelength();
    
    // Phương thức để hiển thị thông tin tín hiệu
    void displayInfo();
    }
    
    // Lớp DiscreteSignal kế thừa giao diện Signal
     class DiscreteSignal implements Signal {
    private final double amplitude;
    private final double period;
    private final double frequency;
    private final double wavelength;
    
    // Constructor
    public DiscreteSignal(double amplitude, double period, double frequency, double wavelength) {
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
    System.out.println("Discrete Signal Information:");
    System.out.println("Amplitude: " + amplitude);
    System.out.println("Period: " + period);
    System.out.println("Frequency: " + frequency);
    System.out.println("Wavelength: " + wavelength);
    }
    }
    
    // Lớp ContinuousSignal kế thừa giao diện Signal
     class ContinuousSignal implements Signal {
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
    
     class Main {
    public static void main(String[] args) {
    // Tạo đối tượng DiscreteSignal
    DiscreteSignal discreteSignal = new DiscreteSignal(5.0, 2.0, 1.0, 0.5);
    discreteSignal.displayInfo();
    
    // Tạo đối tượng ContinuousSignal
    ContinuousSignal continuousSignal = new ContinuousSignal(7.0, 1.5, 2.0, 0.3);
    continuousSignal.displayInfo();
    }
    }

