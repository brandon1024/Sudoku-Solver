import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SudokuSolver
{
    public static void main(String[] args)
    {
    	int[][] initGrid = new int[9][9];
    	
    	initGrid[0][0] = 8;
    	initGrid[1][2] = 3;
    	initGrid[1][2] = 8;
    	initGrid[1][3] = 6;
    	initGrid[2][1] = 7;
    	initGrid[2][4] = 9;
    	initGrid[2][6] = 2;
    	initGrid[3][1] = 5;
    	initGrid[3][5] = 7;
    	initGrid[4][4] = 4;
    	initGrid[4][5] = 5;
    	initGrid[4][6] = 7;
    	initGrid[5][3] = 1;
    	initGrid[5][7] = 3;
    	initGrid[6][2] = 1;
    	initGrid[6][7] = 6;
    	initGrid[6][8] = 8;
    	initGrid[7][2] = 8;
    	initGrid[7][3] = 5;
    	initGrid[7][7] = 1;
    	initGrid[8][1] = 9;
    	initGrid[8][6] = 4;
    	
    	SudokuGrid grid = new SudokuGrid(initGrid);
    	SudokuSolver.print(initGrid);
    	
    	for(int i = 0; i < 9; i++)
    	{
    		for(int o = 1; o < 10; o++)
    		{
    			if(grid.rowContains(i, o) > 1 || grid.colContains(i, o) > 1 || grid.quadrantContains(i, o) > 1)
    			{
    				System.out.println("Invalid Sudoku Puzzle. Cannot be solved.");
    				return;
    			}
    		}
    	}
    	
    	String timeStarted = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
    	long timeStart = Calendar.getInstance().get(Calendar.MILLISECOND);
    	(new SudokuSolver()).solve(grid);
    	String timeEnded = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
    	long timeEnd = Calendar.getInstance().get(Calendar.MILLISECOND);
    	SudokuSolver.print(grid.getGrid());
    	
    	System.out.println("Time Started: " + timeStarted);
    	System.out.println("Time Finished: " + timeEnded);
    	System.out.println("Time Elapsed: " + (timeEnd - timeStart) + "ms");
    	System.out.println("Iterations: " + iterations);
    }

    public SudokuSolver(){}

    private static int iterations = 0;
    
    public SudokuGrid solve(SudokuGrid grid)
    {
    	int[] xy = new int[]{0,0};
    	boolean backtrack = false;
    	
    	outerloop:
    	while(true)
    	{
    		iterations++;
    		
    		if(!grid.isCellModifiable(xy[0], xy[1]))
    		{
    			if(!backtrack)
    				this.stepForward(xy);
    			else
    				this.stepBackward(xy);
    			
    			continue;
    		}
    		
    		if(grid.getCell(xy[0], xy[1]) == 9)
    		{
    			backtrack = true;
    			grid.resetCell(xy[0], xy[1]);
    			this.stepBackward(xy);
    			continue;
    		}
    		else
    		{
    			backtrack = false;
    			
    			do
    			{
    				if(grid.getCell(xy[0], xy[1]) == 9)
    					continue outerloop;
    				grid.incrementCell(xy[0], xy[1]);
    				
    				if(grid.rowContains(xy[1], grid.getCell(xy[0], xy[1])) > 1)
    					continue;
    				
    				if(grid.colContains(xy[0], grid.getCell(xy[0], xy[1])) > 1)
    					continue;
    				
    				if(grid.quadrantContains(grid.getQuadrant(xy[0], xy[1]), grid.getCell(xy[0], xy[1])) > 1)
    					continue;
    				
    				break;
    			}
    			while(true);
    			
    			this.stepForward(xy);
    			if(grid.isValid())
    				break;
    			
    			continue;
    		}
    	}
    	
    	return grid;
    }
    
    private int[] stepForward(int[] xy)
    {
    	if(xy[0] >= 8 && xy[1] >= 8)
    		return xy;
    	
    	if(xy[0] == 8)
    	{
    		xy[0] = 0;
    		xy[1]++;
    	}
    	else
    	{
    		xy[0]++;
    	}
    	
    	return xy;
    }
    
    private int[] stepBackward(int[] xy)
    {
    	if(xy[0] <= 0 && xy[1] <= 0)
    		return xy;
    	
    	if(xy[0] == 0)
    	{
    		xy[0] = 8;
    		xy[1]--;
    	}
    	else
    	{
    		xy[0]--;
    	}
    	
    	return xy;
    }
    
    public static void print(int[][] grid)
    {
    	System.out.println("+===+===+===+===+===+===+===+===+===+");
    	for(int i = 0; i < grid.length; i++)
    	{
    		System.out.print("|");
    		for(int o = 0; o < grid[0].length; o++)
    		{
    			System.out.print(" " + grid[i][o] + " ");
    			
    			if((o+1) % 3 == 0)
    			{
    				System.out.print("|");
    			}
    			else
    			{
    				System.out.print("¦");
    			}
    		}
    		System.out.println();
    		
    		if((i+1) % 3 == 0)
    		{
    			System.out.println("+===+===+===+===+===+===+===+===+===+");
    		}
    		else
    		{
    			System.out.println("+---+---+---+---+---+---+---+---+---+");
    		}
    	}
    }
}