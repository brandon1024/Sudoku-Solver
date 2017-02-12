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
    	this.reset();
    }
   
    /** Constructs a new SudokuSolver given an initial sudoku grid.
      * @param initGrid the initial sudoku grid, used to initialize the grid with
      * given cell values 
      */
    public SudokuSolver(int[][] initGrid)
    {
    	this.reset(initGrid);
    }
    
    /** Resets this object back to defaults. Clears all instance variables
      */
    public void reset()
    {
    	this.iterations = 0;
    	this.xy = new int[]{0,0};
    	this.initialGrid = null;
    	this.solvedGrid = null;
    }
    
    /** Resets the grid back to defaults, and initializes the new puzzle grid.
      * @param initGrid the initial sudoku grid, used to initialize the grid with
      * given cell values 
      */
    public void reset(int[][] initGrid)
    {
    	this.iterations = 0;
    	this.xy = new int[]{0,0};
    	this.initialGrid = initGrid;
    	this.solvedGrid = null;
    }
    
    /** Solves a given sudoku grid.
      * @param initGrid the sudoku grid to solve
      * @return the solved sudoku grid
      */
    public int[][] solve(int[][] initGrid)
    {
    	this.initialGrid = initGrid;
    	return this.solve();
    }
    
    /** Solve the sudoku grid represented by this SudokuSolver object.
      * @return the solved sudoku grid
      */
    public int[][] solve()
    {
    	//Build SudokuGrid Object
    	SudokuGrid grid = new SudokuGrid(this.initialGrid);
    	boolean backtrack = false;
    	
    	outerloop:
    	while(true)
    	{
    		//Increment running total of iterations
			iterations++;
    		
    		//If cell is not modifiable, skip
    		if(!grid.isCellModifiable(this.xy[0], this.xy[1]))
    		{
    			if(!backtrack)
    				this.stepForward();
    			else
    				this.stepBackward();
    			
    			continue;
    		}
    		
    		//Get possible cell values for current cell
    		backtrack = false;
    		int[] possibleCellValues = grid.getPossibleCellValues(xy[0], xy[1]);
    		
    		//Iterate possible cell values
    		for(int index = 0; index < possibleCellValues.length; index++)
    		{
    			//Catch up to cell value in possibleCellValues array
    			if(possibleCellValues[index] <= grid.getCell(xy[0], xy[1]))
    			{
    				//Step backwards if end of array is reached
    				if(index == possibleCellValues.length - 1)
    				{
    					backtrack = true;
    					grid.resetCell(xy[0], xy[1]);
    					this.stepBackward();
    					continue outerloop;
    				}
    				else
    					continue;
    			}
    			
    			//If value already exists in row
    			if(grid.rowContains(this.xy[1], possibleCellValues[index], true) == 1)
    			{
    				//Step backwards if end of array is reached
    				if(index == possibleCellValues.length - 1)
    				{
    					backtrack = true;
    					grid.resetCell(xy[0], xy[1]);
    					this.stepBackward();
    					continue outerloop;
    				}
    				else
    				{
    					grid.setCell(this.xy[0], this.xy[1], possibleCellValues[index]);
    					continue;
    				}
    			}
    			
    			//If value already exists in column
    			if(grid.colContains(this.xy[0], possibleCellValues[index], true) == 1)
    			{
    				//Step backwards if end of array is reached
    				if(index == possibleCellValues.length - 1)
    				{
    					backtrack = true;
    					grid.resetCell(xy[0], xy[1]);
    					this.stepBackward();
    					continue outerloop;
    				}
    				else
    				{
    					grid.setCell(this.xy[0], this.xy[1], possibleCellValues[index]);
    					continue;
    				}
    			}
    			
    			//If value already exists in quadrant
    			if(grid.quadrantContains(grid.getQuadrant(this.xy[0], this.xy[1]), possibleCellValues[index], true) == 1)
    			{
    				//Step backwards if end of array is reached
    				if(index == possibleCellValues.length - 1)
    				{
    					backtrack = true;
    					grid.resetCell(xy[0], xy[1]);
    					this.stepBackward();
    					continue outerloop;
    				}
    				else
    				{
    					grid.setCell(this.xy[0], this.xy[1], possibleCellValues[index]);
    					continue;
    				}
    			}
    			
    			//Set cell value to next possible value
    			grid.setCell(this.xy[0], this.xy[1], possibleCellValues[index]);
    				
    			break;
    		}
    		
    		//If reached, valid cell value found
    		this.stepForward();
    		
    		//If solution found, return
    		if(grid.isValid())
    			break;
    		
    		continue;
    	}

    	this.solvedGrid = grid.getGrid();
    	return this.solvedGrid;
    }
    
    /** Move the grid position handle to the next cell.
      */
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
    
    /** Move the grid position handle to the previous cell.
      */
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
    
    /** Get the column position of the grid position handle.
      * @return the column position of the position handle
      */
    public int getXPos()
    {
    	return this.xy[0];
    }
    
    /** Get the column position of the grid position handle.
     * @return the row position of the position handle
     */
    public int getYPos()
    {
    	return this.xy[1];
    }
    
    /** Get the number of iterations needed to solve the puzzle.
      * @return the number of iterations needed to solve the puzzle
      */
    public int getIterations()
    {
    	return this.iterations;
    }
    
    /** Get the solved sudoku grid.
      * @return the solved sudoku grid
      */
    public int[][] getSolvedGrid()
    {
    	return this.solvedGrid;
    }
    
    /** Get the initial sudoku grid.
      * @return the initial sudoku grid
     */
    public int[][] getInitialGrid()
    {
    	return this.initialGrid;
    }
}