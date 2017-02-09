
public class test
{
	public static void main(String[] args)
	{
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
		    	
    	SudokuGrid grid = new SudokuGrid(initGrid);
    	
    	for(int i = 0; i < 9; i++)
    	{
    		for(int o = 0; o < 9; o++)
    		{
    			for(int n : grid.getPossibleCellValues(o, i))
    			{
    				System.out.print(n);
    			}
    			System.out.print("\t");
    		}
    		System.out.print("\n");
    	}
	}
}
