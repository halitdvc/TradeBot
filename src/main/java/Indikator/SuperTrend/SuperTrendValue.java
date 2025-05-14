package Indikator.SuperTrend;

public class SuperTrendValue {
    private double superTrend;
    private double direction;

    public SuperTrendValue(double superTrend, double direction) {
        this.superTrend = superTrend;
        this.direction = direction;
    }

    public double getSuperTrend() {
        return superTrend;
    }

    public double getDirection() {
        return direction;
    }
}
