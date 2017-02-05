public class SudokuSolver
{
    public static void main(String[] args)
    {
    	int[][] initGrid = new int[9][9];
    	initGrid[0][0] = 6;
    	initGrid[0][3] = 7;
    	initGrid[0][4] = 2;
    	initGrid[0][7] = 3;
    	initGrid[0][8] = 1;
    	initGrid[1][1] = 3;
    	initGrid[1][2] = 5;
    	initGrid[1][3] = 1;
    	initGrid[1][4] = 6;
    	initGrid[1][5] = 9;
    	initGrid[1][6] = 8;
    	initGrid[2][0] = 9;
    	initGrid[2][4] = 8;
    	initGrid[3][1] = 5;
    	initGrid[3][2] = 6;
    	initGrid[3][4] = 9;
    	initGrid[4][2] = 1;
    	initGrid[4][7] = 7;
    	initGrid[5][0] = 3;
    	initGrid[5][1] = 4;
    	initGrid[5][3] = 5;
    	initGrid[5][6] = 2;
    	initGrid[6][2] = 8;
    	initGrid[6][3] = 6;
    	initGrid[6][5] = 3;
    	initGrid[6][6] = 7;
    	initGrid[6][7] = 5;
    	initGrid[7][1] = 7;
    	initGrid[7][4] = 5;
    	initGrid[7][8] = 3;
    	initGrid[8][7] = 8;
    	
    	SudokuSolver.print(initGrid);
    	int[][] solvedGrid = (new SudokuSolver()).solve(initGrid);
    	
    	SudokuSolver.print(solvedGrid);
    }

    public SudokuSolver() {}

    public int[][] solve(int[][] initgrid)
    {
    	SudokuGrid grid = new SudokuGrid(initgrid);
    	int[] xy = new int[]{0,0};
    	
    	this.stepForward(xy);
    	
    	this.stepBackward(xy);
    	
    	
    	
    	
    	return grid.getGrid();
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
    	if(xy[0] <= 8 && xy[1] <= 0)
    		return xy;
    	
    	if(xy[0] == 0)
    	{
    		xy[0] = 8;
    		xy[1]--;
    	}
    	else
    	{
    		xy[0]++;
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