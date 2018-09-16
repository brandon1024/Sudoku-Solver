package solver;

public interface Grid {
    int rowContains(int row, int num, boolean lazy);

    int colContains(int col, int num, boolean lazy);

    int quadrantContains(int quadrant, int num, boolean lazy);

    boolean isSolved();

    boolean isValid();

    boolean isCellModifiable(int col, int row);

    int getCellValue(int row, int col);

    void setCellValue(int col, int row, int value);

    void resetCellValue(int col, int row);

    int getQuadrantId(int col, int row);

    int[][] getGrid();
}
