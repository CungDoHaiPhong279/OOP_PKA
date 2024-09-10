Phần 1:
import java.util.Arrays;

public class DiscreteSignal {
    private double[] values; // Mảng chứa các giá trị tín hiệu rời rạc

    // Constructor nhận vào các giá trị tín hiệu rời rạc
    public DiscreteSignal(double[] values) {
        this.values = values;
    }

    // Phương thức tính toán tín hiệu rời rạc theo định nghĩa toán học [1]
    public double calculateDiscreteSignal(int n) {
        double result = 0.0;
        for (int k = 0; k < values.length; k++) {
            result += values[k] * delta(n - k);
        }
        return result;
    }

    // Hàm xung đơn vị (delta function)
    private int delta(int n) {
        return (n == 0) ? 1 : 0;
    }

    // Phương thức hiển thị các giá trị tín hiệu
    public void displaySignal() {
        System.out.println("Discrete Signal values: " + Arrays.toString(values));
    }
}
Phần 2:
public class Radar {
    private DiscreteSignal discreteSignal;

    // Constructor để khởi tạo đối tượng Radar với tín hiệu rời rạc
    public Radar(DiscreteSignal discreteSignal) {
        this.discreteSignal = discreteSignal;
    }

    // Phương thức tính toán tín hiệu Radar theo phương trình [2]
    public double calculateRadarSignal(int n) {
        if (n >= 0 && n <= 15) {
            return 1 - ((double) n / 15);
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        // Mẫu tín hiệu rời rạc
        double[] sampleDiscreteSignal = {1.0, 0.5, 0.25, 0.125}; // Ví dụ với N = 4

        // Khởi tạo đối tượng DiscreteSignal
        DiscreteSignal discreteSignal = new DiscreteSignal(sampleDiscreteSignal);
        discreteSignal.displaySignal(); // Hiển thị tín hiệu rời rạc

        // Khởi tạo đối tượng Radar
        Radar radar = new Radar(discreteSignal);

        // Tính toán tín hiệu radar cho các giá trị n từ 0 đến 15
        for (int n = 0; n <= 15; n++) {
            double radarSignal = radar.calculateRadarSignal(n);
            System.out.println("Radar signal for n = " + n + ": " + radarSignal);
        }
    }
}