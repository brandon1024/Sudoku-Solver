public class SudokuSolver
{
	private int iterations;
	private int[] xy;
	private int[][] initialGrid;
	private int[][] solvedGrid;

	/** Default SudokuSolver constructor. Initializes objects fields
	  * to default values.
	  */
    public SudokuSolver()
    {
    	this.iterations = 0;
    	this.xy = new int[]{0,0};
    	this.initialGrid = null;
    	this.solvedGrid = null;
    }
    
    public SudokuSolver(int[][] initGrid)
    {
    	this.iterations = 0;
    	this.xy = new int[]{0,0};
    	this.initialGrid = initGrid;
    	this.solvedGrid = null;
    }
    
    public void reset()
    {
    	this.iterations = 0;
    	this.xy = new int[]{0,0};
    	this.initialGrid = null;
    	this.solvedGrid = null;
    }
    
    public void reset(int[][] initGrid)
    {
    	this.iterations = 0;
    	this.xy = new int[]{0,0};
    	this.initialGrid = initGrid;
    	this.solvedGrid = null;
    }
    
    public int[][] solve(int[][] initGrid)
    {
    	this.initialGrid = initGrid;
    	return this.solve();
    }
    
    public int[][] solve()
    {
    	if(this.initialGrid == null)
    		throw new RuntimeException("Initial Grid Null. Initialize SudokuSolver with Initial Grid.");
    	else if(this.initialGrid.length != 9 || this.initialGrid[0].length != 9)
    		throw new RuntimeException("Invalid Grid Dimensions.");
    	
    	SudokuGrid grid = new SudokuGrid(this.initialGrid);
    	
    	for(int i = 0; i < 9; i++)
    	{
    		for(int o = 1; o < 10; o++)
    		{
    			if(grid.rowContains(i, o) > 1 || grid.colContains(i, o) > 1 || grid.quadrantContains(i, o) > 1)
    			{
    				throw new RuntimeException("invalid sudoku puzzle; cannot be solved");
    			}
    		}
    	}
    	
    	boolean backtrack = false;
    	
    	outerloop:
    	while(true)
    	{
    		iterations++;
    		
    		if(!grid.isCellModifiable(this.xy[0], this.xy[1]))
    		{
    			if(!backtrack)
    				this.stepForward();
    			else
    				this.stepBackward();
    			
    			continue;
    		}
    		
    		if(grid.getCell(this.xy[0], this.xy[1]) == 9)
    		{
    			backtrack = true;
    			grid.resetCell(this.xy[0], this.xy[1]);
    			this.stepBackward();
    			continue;
    		}
    		else
    		{
    			backtrack = false;
    			
    			do
    			{
    				if(grid.getCell(this.xy[0], this.xy[1]) == 9)
    					continue outerloop;
    				grid.incrementCell(this.xy[0], this.xy[1]);
    				
    				if(grid.rowContains(this.xy[1], grid.getCell(this.xy[0], this.xy[1])) > 1)
    					continue;
    				
    				if(grid.colContains(this.xy[0], grid.getCell(this.xy[0], this.xy[1])) > 1)
    					continue;
    				
    				if(grid.quadrantContains(grid.getQuadrant(this.xy[0], this.xy[1]), grid.getCell(this.xy[0], this.xy[1])) > 1)
    					continue;
    				
    				break;
    			}
    			while(true);
    			
    			this.stepForward();
    			if(grid.isValid())
    				break;
    			
    			continue;
    		}
    	}

    	this.solvedGrid = grid.getGrid();
    	return this.solvedGrid;
    }
    
    private void stepForward()
    {
    	if(this.xy[0] >= 8 && this.xy[1] >= 8)
    		return;
    	
    	if(this.xy[0] == 8)
    	{
    		this.xy[0] = 0;
    		this.xy[1]++;
    	}
    	else
    	{
    		this.xy[0]++;
    	}
    }
    
    private void stepBackward()
    {
    	if(xy[0] <= 0 && xy[1] <= 0)
    		return;
    	
    	if(xy[0] == 0)
    	{
    		xy[0] = 8;
    		xy[1]--;
    	}
    	else
    	{
    		xy[0]--;
    	}
    }
    
    public int getXPos()
    {
    	return this.xy[0];
    }
    
    public int getYPos()
    {
    	return this.xy[1];
    }
    
    public int getIterations()
    {
    	return this.iterations;
    }
    
    public int[][] getSolvedGrid()
    {
    	return this.solvedGrid;
    }
    
    public int[][] getInitialGrid()
    {
    	return this.initialGrid;
    }
}