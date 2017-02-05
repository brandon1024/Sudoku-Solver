public class SudokuSolverGUI
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
    	
    	SudokuSolver solver = new SudokuSolver(initGrid);
    	
    	SudokuSolverGUI.print(solver.getInitialGrid());
    	String timeStarted = new java.text.SimpleDateFormat("HH:mm:ss.SSS").format(java.util.Calendar.getInstance().getTime());
    	long timeStart = java.util.Calendar.getInstance().get(java.util.Calendar.MILLISECOND);
    	
    	solver.solve();
    	
    	String timeEnded = new java.text.SimpleDateFormat("HH:mm:ss.SSS").format(java.util.Calendar.getInstance().getTime());
    	long timeEnd = java.util.Calendar.getInstance().get(java.util.Calendar.MILLISECOND);
    	SudokuSolverGUI.print(solver.getSolvedGrid());
    	
    	System.out.println("Time Started: " + timeStarted);
    	System.out.println("Time Finished: " + timeEnded);
    	System.out.println("Time Elapsed: " + (timeEnd - timeStart) + "ms");
    	System.out.println("Iterations: " + solver.getIterations());
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
