package de.schenerator.util;

import de.schenerator.Schenerator;

public enum NoiseMode {
    UNIFORM, NORMAL;

    public static NoiseMode getEnum(String noiseString) {
        if (noiseString.equals(UNIFORM.name()) || noiseString
                .equals(Schenerator.getString("menu.noise.uniform"))) {

            return UNIFORM;

        } else if (noiseString.equals(NORMAL.name()) || noiseString
                .equals(Schenerator.getString("menu.noise.normal"))) {

            return NORMAL;
        }

        return null;
    }
}
