package baitapgiuaky_cungdohaiphong.FinalProject;

public class Main {
    public static void main(String[] args) {
        // Tạo đối tượng DiscreteSignal
        DiscreteSignal discreteSignal = new DiscreteSignal(5.0, 2.0, 1.0, 0.5);
        discreteSignal.displayInfo();
        
        // Tạo đối tượng ContinuousSignal
        ContinuousSignal continuousSignal = new ContinuousSignal(7.0, 1.5, 2.0, 0.3);
        continuousSignal.displayInfo();
     }
}
