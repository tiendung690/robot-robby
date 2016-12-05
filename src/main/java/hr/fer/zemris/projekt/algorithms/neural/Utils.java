package hr.fer.zemris.projekt.algorithms.neural;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Random;

/**
 * Created by Dominik on 4.12.2016..
 */
public class Utils {
    public static final Random RANDOM = new Random();

    public static RealMatrix createRandomMatrix(int rows, int columns, double lowerBound, double upperBound) {
        double[][] matrix = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = RANDOM.nextDouble() * (upperBound - lowerBound) + lowerBound;
            }
        }

        return MatrixUtils.createRealMatrix(matrix);
    }

    public static RealVector createRandomVector(int size, double lowerBound, double upperBound) {
        double[] vector = new double[size];
        for (int i = 0; i < size; i++) {
            vector[i] = RANDOM.nextDouble() * (upperBound - lowerBound) + lowerBound;
        }

        return new ArrayRealVector(vector);
    }
}
