package org.campagnelab.dl.genotype.performance;

import org.campagnelab.dl.genotype.predictions.AbstractGenotypePrediction;
import org.campagnelab.dl.genotype.predictions.GenotypePrediction;

import java.util.Arrays;

/**
 * Estimate genotype statistics.
 * Created by rct66 on 12/19/16.
 */
public class StatsAccumulator {


    int numCorrect;
    int numProcessed;
    int numTruePositive;
    int numTrueNegative;
    int numFalsePositive;
    int numFalseNegative;
    private int numVariants;

    public void initializeStats() {
        numCorrect = 0;
        numProcessed = 0;
        numTruePositive = 0;
        numTrueNegative = 0;
        numFalsePositive = 0;
        numFalseNegative = 0;
        numVariants = 0;
    }

    public void observe(GenotypePrediction fullPred) {
        observe(fullPred, fullPred.isVariant());
    }

    public void observe(GenotypePrediction fullPred, boolean isVariant) {
        numProcessed++;
        if (fullPred.isCorrect()) {
            numCorrect++;
            if (isVariant) {
                numTruePositive++;
            } else {
                numTrueNegative++;
            }
        } else {
            if (isVariant) {
                numFalseNegative++;
            } else {
                numFalsePositive++;
            }
        }
        numVariants += isVariant ? 1 : 0;
    }

    public double[] createOutputStatistics() {
        double accuracy = numCorrect / (double) numProcessed;
        double recall = numTruePositive / ((double) (numTruePositive + numFalseNegative));
        double precision = numTruePositive / ((double) (numTruePositive + numFalsePositive));
        double F1 = precision * recall / (precision + recall);
        return new double[]{accuracy, recall, precision, F1, numVariants};
    }

    public String[] createOutputHeader() {
        return new String[]{"accuracy", "sensitivity/recall", "PPV/precision", "F1", "numVariants",
        };
    }

    public static final int F1_INDEX = 3;

    public void reportStatistics(String prefix) {
        double[] statsArray = createOutputStatistics();
        System.out.println("Statistics estimated for " + prefix);
        System.out.println("Accuracy =" + statsArray[0]);
        System.out.println("Recall =" + statsArray[1]);
        System.out.println("Precision =" + statsArray[2]);
        System.out.println("F1 =" + statsArray[3]);
        System.out.println("numVariants =" + statsArray[4]);
        System.out.println("Printable: " + Arrays.toString(statsArray));
    }

}