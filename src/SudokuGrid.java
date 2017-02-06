public class SudokuGrid
{
    private GridCell[][] grid;

    /** Default SudokuGrid constructor. Initializes a 9x9 sudoku grid where
      * each cell is modifiable with initial value 0.
      */
    public SudokuGrid()
    {
    	this.buildEmptyGrid();
    }

    /** SudokuGrid constructor. Initializes a 9x9 sudoku grid with values
      * described by the parameter grid. The grid array must be 
      * an array of dimensions 9x9, where empty cells have a value of 0.
      * Empty cells are modifiable, and cells with values 1-9 will be
      * marked as unmodifiable, and will not respond to incrementCell()
      * or resetCell().
      * 
      * @param grid The initial sudoku cells given, used to construct a grid object
      */
    public SudokuGrid(int[][] grid)
    {
    	this.buildEmptyGrid();
    	
    	//Reject Invalid Parameter Array
    	if(grid.length != 9 || grid[0].length != 9)
        	throw new RuntimeException("provided grid has invalid dimensions");
    	
    	//Build SudokuGrid from Parameter Array
        for(int row = 0; row < grid.length; row++)
    	{
    		for(int col = 0; col < grid[0].length; col++)
    		{
    			if(grid[row][col] < 0 || grid[row][col] > 9)
    				throw new RuntimeException("provided grid has invalid cell; value: " + grid[row][col]);
    			
    			if(grid[row][col] == 0)
    			{
    				this.grid[row][col].modifiable = true;
    				continue;
    			}
    			else
    			{
    				this.grid[row][col].value = grid[row][col];
    				this.grid[row][col].modifiable = false;
    			}
    		}
    	}
    }
    
    /** Build an empty sudoku grid. Each cell has initial value 0, and is modifiable.
      */
    private void buildEmptyGrid()
    {
    	this.grid = new GridCell[9][9];
    	for(int i = 0; i < this.grid.length; i++)
    	{
    		for(int o = 0; o < this.grid[0].length; o++)
    		{
    			this.grid[i][o] = new GridCell();
    			this.grid[i][o].value = 0;
    			this.grid[i][o].modifiable = true;
    		}
    	}
    }

    /** Validate grid row. Validation is done in two steps, first with an initial
      * non-deterministic step, followed by a deterministic step.
      * 
      * The non-deterministic step rejects any grid whose sum of cell values across a
      * given row is not equal to 45, or one with an empty cell. This acts as a fast
      * way to invalidate a row.
      * 
      * The deterministic step tests the frequency of values in a row. The row is valid
      * iff the frequency of numbers across a row are exactly 1, and the only numbers 
      * that appear are digits 1 through 9.
      * 
      * @param y row
      * @return true if the row is valid, false otherwise.
      */
    public boolean isValidRow(int y)
    {
        //If Sum of Indices != 45 or Empty Cell Encountered, Row is Invalid
        int sum = 0;
        for(int i = 0; i < 9; i++)
        {
            if(this.grid[y][i].value == 0)
            	return false;
            
        	sum += this.grid[y][i].value;
        }

        if(sum != 45)
            return false;

        //If Sum of Indices == 45, Check Frequency of Numbers
        for(int i = 1; i < 10; i++)
        {
            int count = 0;

            for(int o = 0; o < 9; o++)
            {
                if(this.grid[y][o].value == i)
                    count++;
            }

            if(count > 1)
                return false;
        }

        return true;
    }

    /** Validate grid column. Validation is done in two steps, first with an initial
      * non-deterministic step, followed by a deterministic step.
      * 
      * The non-deterministic step rejects any grid whose sum of cell values across a
      * given column is not equal to 45, or one with an empty cell. This acts as a fast
      * way to invalidate a column.
      * 
      * The deterministic step tests the frequency of values in a column. The column is valid
      * iff the frequency of numbers across a column are exactly 1, and the only numbers 
      * that appear are digits 1 through 9.
      * 
      * @param x column
      * @return true if the column is valid, false otherwise.
      */
    public boolean isValidColumn(int x)
    {
        //If Sum of Indices != 45, Col is Invalid
        int sum = 0;
        for(int i = 0; i < 9; i++)
        {
            sum += this.grid[i][x].value;
        }

        if(sum != 45)
            return false;

        //If Sum of Indices == 45, Check Frequency of Numbers
        for(int i = 1; i < 10; i++)
        {
            int count = 0;

            for(int o = 0; o < 9; o++)
            {
                if(this.grid[o][x].value == i)
                    count++;
            }

            if(count > 1)
                return false;
        }

        return true;
    }

    /** Validate grid quadrant. Validation is done in two steps, first with an initial
      * non-deterministic step, followed by a deterministic step.
      * 
      * The non-deterministic step rejects any grid whose sum of cell values in a
      * given quadrant is not equal to 45, or one with an empty cell. This acts as a fast
      * way to invalidate a quadrant.
      * 
      * The deterministic step tests the frequency of values in a quadrant. The quadrant is valid
      * iff the frequency of numbers are exactly 1, and the only numbers 
      * that appear are digits 1 through 9.
      * 
      * @param quadrant
      * @return true if the quadrant is valid, false otherwise.
      */
    public boolean isValidQuadrant(int quadrant)
    {
        int xOffset = 0;
        int yOffset = 0;

        if(quadrant >= 0 && quadrant <= 2)
        {
            xOffset = quadrant * 3;
            yOffset = 0;
        }
        else if(quadrant >= 3 && quadrant <= 5)
        {
            xOffset = ((quadrant - 3) * 3);
            yOffset = 3;
        }
        else
        {
            xOffset = ((quadrant - 6) * 3);
            yOffset = 6;
        }

        //If Sum of Indices != 45, Quadrant is Invalid
        int sum = 0;
        for(int i = 0; i < 3; i++)
        {
            for(int o = 0; o < 3; o++)
            {
                sum += this.grid[i + yOffset][o + xOffset].value;
            }
        }

        if(sum != 45)
            return false;

        //If Sum of Indices == 45, Check Frequency of Numbers
        for(int n = 1; n < 10; n++)
        {
            int count = 0;

            for(int i = 0; i < 3; i++)
            {
                for(int o = 0; o < 3; o++)
                {
                    if(this.grid[i + yOffset][o + xOffset].value == n)
                        count++;
                }
            }

            if(count > 1)
                return false;
        }

        return true;
    }

    /** Validate the entire grid. Validates each row, column and quadrant individually.
      * @see isValidColumn(int x)
      * @see isValidRow(int y)
      * @see isValidQuadrant(int quadrant)
      * @return true if the grid is valid, false otherwise.
      */
    public boolean isValid()
    {
        //Test Rows
        for(int i = 0; i < 9; i++)
        {
            if(!this.isValidRow(i))
                return false;
        }

        //Test Columns
        for(int i = 0; i < 9; i++)
        {
            if(!this.isValidColumn(i))
                return false;
        }

        //Test Quadrants
        for(int i = 0; i < 9; i++)
        {
            if(!this.isValidQuadrant(i))
                return false;
        }

        return true;
    }

    /** Test the frequency of a given number across a row.
      * 
      * @param y row
      * @param num
      * @return the number of occurrences of num in row
      */
    public int rowContains(int y, int num)
    {
        int count = 0;
        for(int o = 0; o < 9; o++)
        {
            if(this.grid[y][o].value == num)
                count++;
        }

        return count;
    }

    /** Test the frequency of a given number across a column.
     * 
     * @param x column
     * @param num
     * @return the number of occurrences of num in col
     */
    public int colContains(int x, int num)
    {
        int count = 0;
        for(int o = 0; o < 9; o++)
        {
            if(this.grid[o][x].value == num)
                count++;
        }

        return count;
    }

    /** Test the frequency of a given number in a quadrant.
     * 
     * @param quadrant
     * @param num
     * @return the number of occurances of num in quadrant
     */
    public int quadrantContains(int quadrant, int num)
    {
        int xOffset = 0;
        int yOffset = 0;

        if(quadrant >= 0 && quadrant <= 2)
        {
            xOffset = quadrant * 3;
            yOffset = 0;
        }
        else if(quadrant >= 3 && quadrant <= 5)
        {
            xOffset = ((quadrant - 3) * 3);
            yOffset = 3;
        }
        else
        {
            xOffset = ((quadrant - 6) * 3);
            yOffset = 6;
        }

        int count = 0;
        for(int i = 0; i < 3; i++)
        {
            for(int o = 0; o < 3; o++)
            {
                if(this.grid[i + yOffset][o + xOffset].value == num)
                    count++;
            }
        }

        return count;
    }

    /** Access the value of the grid cell at the given row and column.
     * 
     * @param x column
     * @param y row
     * @return the cell value at row and column
     */
    public int getCell(int x, int y)
    {
        return this.grid[y][x].value;
    }

    /** Compute the quadrant number specified by the row and column parameters.
     * 
     * @param x column
     * @param y row
     * @return the quadrant number specified by row and col
     */
    public int getQuadrant(int x, int y)
    {
    	if(x >= 0 && x <= 2)
    	{
    		if(y >= 0 && y <= 2)
    			return 0;
    		else if(y >= 3 && y <= 5)
    			return 3;
    		else
    			return 6;
    	}
    	else if(x >= 3 && x <= 5)
    	{
    		if(y >= 0 && y <= 2)
    			return 1;
    		else if(y >= 3 && y <= 5)
    			return 4;
    		else
    			return 7;
    	}
    	else
    	{
    		if(y >= 0 && y <= 2)
    			return 2;
    		else if(y >= 3 && y <= 5)
    			return 5;
    		else
    			return 8;
    	}
    }
    
    /** Increment the cell at given row and column.
     * 
     * @throws RuntimeException if cell is not modifiable, or the cell value is 9.
     * @param x column
     * @param y row
     */
    public void incrementCell(int x, int y)
    {
    	if(!this.grid[y][x].modifiable)
    		 throw new RuntimeException("Cannot increment cell; user defined");
    	else if(this.grid[y][x].value == 9)
            throw new RuntimeException("Cannot increment cell; maximum possible cell value");
        else
            this.grid[y][x].value++;
    }

    /** Reset the cell to an empty cell with value 0.
     * 
     * @throws RuntimeException if cell is not modifiable.
     * @param x column
     * @param y row
     */
    public void resetCell(int x, int y)
    {
        if(!this.grid[y][x].modifiable)
        	 throw new RuntimeException("Cannot reset cell; user defined");
    	this.grid[y][x].value = 0;
    }
    
    /** Returns whether the cell is modifiable.
     * 
     * @param x column
     * @param y row
     * @return whether the cell is modifiable.
     */
    public boolean isCellModifiable(int x, int y)
    {
    	return this.grid[y][x].modifiable;
    }
    
    /** Build an array of integers that represents the sudoku grid at the moment of
     * being invoked.
     * 
     * @return an array of integers representing this sudoku grid.
     */
    public int[][] getGrid()
    {
    	int[][] newGrid = new int[9][9];
    	
    	for(int i = 0; i < this.grid.length; i++)
    	{
    		for(int o = 0; o < this.grid[0].length; o++)
    		{
    			newGrid[i][o] = this.grid[i][o].value;
    		}
    	}
    	
    	return newGrid;
    }
    
    /** A class representing a single cell in a sudoku grid.
      * Each cell has a value, and can be either modifiable or
      * not modifiable.
      */
    private class GridCell
    {
    	public boolean modifiable;
    	public int value;
    }
}