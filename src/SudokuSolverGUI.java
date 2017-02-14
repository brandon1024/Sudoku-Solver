public class SudokuSolverGUI
{
	public static void main(String[] args)
    {
    	//Build Sudoku Puzzle
		int[][] initGrid = new int[][]
    	{
    		{8,0,0,0,0,0,0,0,0},
    		{0,0,3,6,0,0,0,0,0},
    		{0,7,0,0,9,0,2,0,0},
    		{0,5,0,0,0,7,0,0,0},
    		{0,0,0,0,4,5,7,0,0},
    		{0,0,0,1,0,0,0,3,0},
    		{0,0,1,0,0,0,0,6,8},
    		{0,0,8,5,0,0,0,1,0},
    		{0,9,0,0,0,0,4,0,0}
    	};
    	
    	//Initialize Solver and Print
    	SudokuSolver solver = new SudokuSolver(initGrid);
    	SudokuSolverGUI.print(solver.getInitialGrid());
    	
    	//Run Solver
    	String timeStarted = new java.text.SimpleDateFormat("HH:mm:ss.SSS").format(java.util.Calendar.getInstance().getTime());
    	long timeStart = System.nanoTime();
    	solver.solve();
    	String timeEnded = new java.text.SimpleDateFormat("HH:mm:ss.SSS").format(java.util.Calendar.getInstance().getTime());
    	long timeEnd = System.nanoTime();
    	double timeElapsed = timeEnd - timeStart;
    	timeElapsed /= 1000000;
    	
    	//Print Solution
    	SudokuSolverGUI.print(solver.getSolvedGrid());
    	
    	//Output Metrics
    	System.out.println("Time Started: " + timeStarted);
    	System.out.println("Time Finished: " + timeEnded);
    	System.out.println("Time Elapsed: " + timeElapsed + "ms");
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
    			if(grid[i][o] == 0)
    			{
    				System.out.print(" . ");
    			}
    			else
    			{
    				System.out.print(" " + grid[i][o] + " ");
    			}
    				
    			if((o+1) % 3 == 0)
    			{
    				System.out.print("|");
    			}
    			else
    			{
    				System.out.print(" ");
    			}
    		}
    		System.out.println();
    		
    		if((i+1) % 3 == 0)
    		{
    			System.out.println("+===+===+===+===+===+===+===+===+===+");
    		}
    		else
    		{
    			System.out.println("|                                   |");
    		}
    	}
    }
}
