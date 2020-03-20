/**
 * @author {Piotr Kacprzak}
 * @version {1.0.0}
 */

public class Levenshtein {

    private char[] pattern;

    /**
     * Constructor setting pattern as character array
     *
     * @param pattern
     */
    public Levenshtein(String pattern) {
        this.pattern = pattern.toCharArray();
    }

    /**
     * Function comparing passed String with pattern
     *
     * @param expressionString
     * @return
     */
    public int compareWithPattern(String expressionString) {
        char[] expression = expressionString.toCharArray();
        return levenshteinImpl(expression);
    }

    /**
     * Levenshtein distance algorithm implementation
     *
     * @param expression
     * @return
     */
    private int levenshteinImpl(char[] expression) {
        int[][] distanceMatrix = initMatrix(pattern.length, expression.length);
        int cost;
        for (int i = 1; i <= pattern.length; i++) {
            for (int j = 1; j <= expression.length; j++) {
                cost = calculateCost(pattern[i - 1], expression[j - 1]);
                distanceMatrix[i][j] = pickMinimum(distanceMatrix[i - 1][j] + 1,
                        distanceMatrix[i][j - 1] + 1,
                        distanceMatrix[i - 1][j - 1] + cost);
            }
        }
        return distanceMatrix[pattern.length][expression.length];
    }

    /**
     * Matrix initialization for algorithm
     * and setting firs col and row proper values
     *
     * @param matrixRows
     * @param matrixCols
     * @return
     */
    private int[][] initMatrix(int matrixRows, int matrixCols) {
        int[][] matrix = new int[matrixRows + 1][matrixCols + 1];
        for (int i = 0; i <= matrixRows; i++) matrix[i][0] = i;
        for (int i = 1; i <= matrixCols; i++) matrix[0][i] = i;
        return matrix;
    }

    /**
     * Cost calculation 1 if values are different
     *
     * @param a
     * @param b
     * @return
     */
    private int calculateCost(char a, char b) {
        return a == b ? 0 : 1;
    }

    /**
     * Picking minimum from three numbers
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    private int pickMinimum(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }
}
