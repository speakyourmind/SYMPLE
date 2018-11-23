/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.text.prediction.local.util;

import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public class EditDistance {

    private static final String NAME = EditDistance.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     * Returns levenshtein edit distance between two strings, where each
     * operation has a cost of 1. Using dynamic programming method, creating
     * matrix of edit costs between two words and finding least cost path.
     *
     * @param string1
     * @param string2
     * @return
     */
    public static int findEditDistance(String string1, String string2) {
        string1 = string1.toLowerCase();
        string2 = string2.toLowerCase();
        //create matrix and find 
        int[][] costs = new int[string1.length() + 1][string2.length() + 1];
        //base case: populate first row/col
        for (int i = 0; i < string1.length() + 1; i++) {
            costs[i][0] = i;
        }
        for (int j = 1; j < string2.length() + 1; j++) {
            costs[0][j] = j;
        }
        //populate distance/cost matrix
        for (int i = 1; i < string1.length() + 1; i++) {
            for (int j = 1; j < string2.length() + 1; j++) {
                if (string1.charAt(i - 1) == string2.charAt(j - 1)) {
                    costs[i][j] = costs[i - 1][j - 1];
                } else {
                    costs[i][j] = 1 + minimum(costs[i][j - 1],
                            costs[i - 1][j], costs[i - 1][j - 1]);
                }
            }
        }
//		printMatrix(costs);			
        //edit distance is last unit in matrix
        return costs[string1.length()][string2.length()];
    }

    //HELPER: just prints 2D matrix, for testing

    /**
     *
     * @param mat
     */
    public static void printMatrix(int mat[][]) {
        for (int[] mat1 : mat) {
            for (int j = 0; j < mat[0].length; j++) {
                LOGGER.info(mat1[j] + " ");
            }
        }
    }

    //HELPER: just extends Math's min to find minimum of three ints

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
}
