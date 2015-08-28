package com.ScoreFollowerLibrary.nl.metaphoric.scorefollower.lib.feature;

import com.ScoreFollowerLibrary.edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import com.ScoreFollowerLibrary.nl.metaphoric.scorefollower.lib.AudioBuffer;

/**
 * The logarithmic chroma vector is similar to the
 * linear chroma vector, but it uses logarithmic amplitudes instead
 * of linear ones.
 * This is how it is calculated in the "to catch a chorus" paper.
 * 
 * Normalization only involves subtracting the vector mean.
 * 
 * The match probability is calculated using a Pearson's correlation
 * coefficient.
 * 
 * @author Elte Hupkes
 *
 */
public class LogChromaVector extends LinearChromaVector {

	/**
	 * Does the same as the linear chroma vector
	 * @param parts
	 */
	public LogChromaVector(double[] parts) {
		super(parts);
	}
	
	/**
	 * Simply calls parent, which uses correct intensity through
	 * the intensity() method.
	 * @param buffer
	 * @param sampleRate
	 * @param transformer
	 */
	public LogChromaVector(AudioBuffer buffer, float sampleRate, DoubleFFT_1D transformer) {
		super(buffer, sampleRate, transformer);
	}
	
	/**
	 * Use logarithmic intensity
	 */
	protected double intensity(double real, double imaginary) {
		return Math.log10(super.intensity(real, imaginary));
	}
	
	/**
	 * Normalizes this vector; currently only calculates
	 * standard deviation.
	 */
	protected void normalize() {
		std = 0;
		for (int i = 0; i < 12; i++) {
			chroma[i] -= mean;
			
			// By subtracting the mean it becomes zero, meaning the deviation is the
			// chroma itself.
			std += chroma[i] * chroma[i];
		}
		std = Math.sqrt(std / 12.0);
	}
	
	/**
	 * Calculates the match probability between this
	 * vector and another vector. Returns the Pearson
	 * correlation coefficient, with the range -1 ... 1
	 * mapped to 0 ... 1 (Negative correlation is small
	 * probability).
	 */
	public double matchProbability(FrameVector v) {
		double r = 0;
		double[] vChroma = v.getChroma();
		// Include the mean in the calculation for clarity. These vectors are normalized
		// to zero mean though, so it could be left out altogether.
		double mean = 0;
        double vMean=0;
        double max=0;
        double vMax=0;
        double[] s = new double[12];
        for (int i = 0; i < 12; i++) {
            mean+=chroma[i];
            vMean+= vChroma[i];
            if (chroma[i] > max) {
                max = chroma[i];
            }
            if (vChroma[i] > vMax) {
                vMax = vChroma[i];
            }
        }
        for (int i = 0; i < 12; i++) {
			//r += ((chroma[i] - mean) / std) * ((vChroma[i] - mean) / v.std);
            r += ((chroma[i] - mean/12) / max) * ((vChroma[i] - vMean/12) / vMax);

            // Test the difference between shifted and unshifted vector
            s[i]=0;
            for (int j = i; j<12; j++) {
                s[i] += Math.max(0, chroma[j-i]) * Math.max(0, vChroma[j%12]);
            }
		}

        // Get the indice of the s max
        double maxCirc = s[0];
        int maxIndice = 0;
        for (int i = 0; i<12; i++) {
            if (s[i] > maxCirc) {
                maxIndice = i;
            }
        }

        // Accept only if the max is at the unshifted chroma
        if (maxIndice != 0) {
            return 0;
        } else {
            return r;
        }
	}
}
