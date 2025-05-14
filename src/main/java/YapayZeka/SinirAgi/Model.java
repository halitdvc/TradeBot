package YapayZeka.SinirAgi;

public class Model {
    // Model parametreleri
    private int inputSize;
    private int outputSize;
    private double[][] weights; // Ağırlıkları tutan matris
    private double[] biases; // Biasları tutan vektör

    public Model(int inputSize, int outputSize) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        initializeWeightsAndBiases();
    }

    private void initializeWeightsAndBiases() {
        // Ağırlık ve bias değerlerini başlatma
        weights = new double[inputSize][outputSize];
        biases = new double[outputSize];

        // Örnek olarak, ağırlıkları ve biasları rastgele küçük değerlerle başlatma
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weights[i][j] = Math.random() - 0.5;
            }
        }

        for (int i = 0; i < outputSize; i++) {
            biases[i] = Math.random() - 0.5;
        }
    }

    public double[] predict(double[] input) {
        // Modelin tahminini gerçekleştirme
        double[] output = new double[outputSize];

        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                output[i] += input[j] * weights[j][i];
            }
            output[i] += biases[i];
        }
        return output;
    }

    // Ağırlık ve bias güncelleme fonksiyonları buraya eklenecek
}
