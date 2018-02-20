package de.schenerator;

import de.schenerator.util.NoiseMode;

public class Configuration {

    public enum XMode {
        IGNORE, MEAN, ADAPT
    }

    public enum YMode {
        SCALE, TRIM
    }

//    public enum NoiseMode {
    // UNIFORM, NORMAL
//    }

    private int leftChangeRange;
    private int rightChangeRange;
    private int steps;
    private int noiseStrength;
    private int smoothLevel;

    private XMode xMode;
    private YMode yMode;
    private NoiseMode noiseMode;
    private boolean isNoisePercentage;

    public Configuration() {
        leftChangeRange = 0;
        rightChangeRange = 0;
        steps = 96;
        noiseStrength = 10;
        smoothLevel = 5;
        xMode = XMode.IGNORE;
        yMode = YMode.SCALE;
        noiseMode = NoiseMode.UNIFORM;
        isNoisePercentage = false;
    }

    public synchronized int getLeftChangeRange() {
        return leftChangeRange;
    }

    public synchronized void setLeftChangeRange(int newRange) {
        leftChangeRange = newRange;
    }

    public synchronized int getRightChangeRange() {
        return rightChangeRange;
    }

    public synchronized void setRightChangeRange(int newRange) {
        rightChangeRange = newRange;
    }

    public synchronized int getSteps() {
        return steps;
    }

    public synchronized void setSteps(int newStepsize) {
        steps = newStepsize;
    }

    public synchronized int getNoiseStrength() {
        return noiseStrength;
    }

    public synchronized void setNoiseStrength(int newNoiseStrength) {
        noiseStrength = newNoiseStrength;
    }

    public synchronized int getSmoothLevel() {
        return smoothLevel;
    }

    public synchronized void setSmoothLevel(int newSmoothLevel) {
        smoothLevel = newSmoothLevel;
    }

    public synchronized XMode getXMode() {
        return xMode;
    }

    public synchronized void setXMode(XMode newXMode) {
        xMode = newXMode;
    }

    public synchronized YMode getYMode() {
        return yMode;
    }

    public synchronized void setYMode(YMode newYMode) {
        yMode = newYMode;
    }

    public synchronized NoiseMode getNoiseMode() {
        return noiseMode;
    }

    public synchronized void setNoiseMode(NoiseMode newNoiseMode) {
        noiseMode = newNoiseMode;
    }

    public synchronized void setNoiseMode(String newNoiseMode) {
        if (newNoiseMode.equals(Schenerator.getString("menu.noise.uniform"))) {
            setNoiseMode(NoiseMode.UNIFORM);
        } else if (newNoiseMode
                .equals(Schenerator.getString("menu.noise.normal"))) {
            setNoiseMode(NoiseMode.NORMAL);
        }
    }

    public synchronized boolean isNoisePercentage() {
        return isNoisePercentage;
    }

    public synchronized void setNoisePercentage(
            boolean newNoisePercentageFlag) {
        isNoisePercentage = newNoisePercentageFlag;
    }
}
