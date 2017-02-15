import static org.junit.Assert.*;
import org.junit.Test;

public class SudokuGridTest
{
	@Test
	public void testSudokuGrid()
	{
		int[][] grid;
		SudokuGrid sudokuGrid;
	    
		try
		{
			grid = new int[9][10];
			new SudokuGrid(grid);
			fail("Expected a RuntimeException to be thrown");
		}
		catch(RuntimeException e){}
		
		try
		{
			grid = new int[9][9];
			grid[1][1] = 10;
			new SudokuGrid(grid);
			fail("Expected a RuntimeException to be thrown");
		}
		catch(RuntimeException e){}
		
		grid = new int[9][9];
		sudokuGrid = new SudokuGrid(grid);
		assertArrayEquals("sudoku grid does not match grid array parameter", grid, sudokuGrid.getGrid());
		
		grid = new int[9][9];
		grid[0][0] = 1;
		sudokuGrid = new SudokuGrid(grid);
		assertFalse("grid array parameter value not recognized", sudokuGrid.isCellModifiable(0, 0));
		assertTrue("grid array parameter value not recognized", sudokuGrid.getCell(0, 0) == 1);
		assertNull("grid array parameter value not recognized", sudokuGrid.getPossibleCellValues(0, 0));
		
		grid = new int[][]
    	{
    		{0,0,0,0,0,0,0,0,0},
    		{2,0,0,0,0,0,0,0,0},
    		{3,0,0,0,0,0,0,0,0},
    		{4,0,0,0,0,0,0,0,0},
    		{5,0,0,0,0,0,0,0,0},
    		{6,0,0,0,0,0,0,0,0},
    		{7,0,0,0,0,0,0,0,0},
    		{8,0,0,0,0,0,0,0,0},
    		{9,0,0,0,0,0,0,0,0}
    	};
		sudokuGrid = new SudokuGrid(grid);
		assertFalse("grid array parameter value not recognized", sudokuGrid.isCellModifiable(0, 0));
		assertTrue("grid array parameter value not recognized", sudokuGrid.getCell(0, 0) == 1);
		assertNull("grid array parameter value not recognized", sudokuGrid.getPossibleCellValues(0, 0));
    	
    	grid = new int[][]
    	{
    		{2,3,4,5,6,7,8,9,0},
    		{0,0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0}
    	};
		sudokuGrid = new SudokuGrid(grid);
		assertFalse("grid array parameter value not recognized", sudokuGrid.isCellModifiable(8, 0));
		assertTrue("grid array parameter value not recognized", sudokuGrid.getCell(8, 0) == 1);
		assertNull("grid array parameter value not recognized", sudokuGrid.getPossibleCellValues(8, 0));
		
		grid = new int[][]
		{
			{8,0,0,0,0,0,0,0,0},
			{0,0,3,6,0,0,0,7,0},
			{0,7,0,0,9,0,2,0,0},
			{0,5,0,0,0,7,0,0,0},
			{0,0,0,0,4,5,7,0,0},
			{0,0,0,1,0,0,0,3,0},
			{0,0,1,0,7,0,0,6,8},
			{0,0,8,5,0,0,0,1,7},
			{0,9,0,0,0,0,4,0,0}
		};
		
		sudokuGrid = new SudokuGrid(grid);
		assertFalse("grid array parameter value not recognized", sudokuGrid.isCellModifiable(3, 0));
		assertTrue("grid array parameter value not recognized", sudokuGrid.getCell(3, 0) == 7);
		assertNull("grid array parameter value not recognized", sudokuGrid.getPossibleCellValues(3, 0));
	}
}