public class SudokuGrid
{
    private GridCell[][] grid;

    public SudokuGrid()
    {
    	this.grid = new GridCell[9][9];
    	for(int i = 0; i < this.grid.length; i++)
    	{
    		for(int o = 0; o < this.grid[0].length; o++)
    		{
    			this.grid[i][o] = new GridCell();
    		}
    	}
    }

    public SudokuGrid(int[][] grid)
    {
    	this.grid = new GridCell[9][9];
    	for(int i = 0; i < this.grid.length; i++)
    	{
    		for(int o = 0; o < this.grid[0].length; o++)
    		{
    			this.grid[i][o] = new GridCell();
    		}
    	}
    	
    	if(grid.length != 9 || grid[0].length != 9)
        	throw new RuntimeException("provided grid is invalid");
    	
        for(int i = 0; i < grid.length; i++)
    	{
    		for(int o = 0; o < grid[0].length; o++)
    		{
    			if(grid[i][o] == 0)
    			{
    				this.grid[i][o].modifiable = true;
    				continue;
    			}
    			else
    			{
    				this.grid[i][o].value = grid[i][o];
    				this.grid[i][o].modifiable = false;
    			}
    		}
    	}
    }

    public boolean isValidRow(int y)
    {
        //If Sum of Indices != 45, Row is Invalid
        int sum = 0;
        for(int i = 0; i < 9; i++)
        {
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

    public int rowContains(int y, int n)
    {
        int count = 0;
        for(int o = 0; o < 9; o++)
        {
            if(this.grid[y][o].value == n)
                count++;
        }

        return count;
    }

    public int colContains(int x, int n)
    {
        int count = 0;
        for(int o = 0; o < 9; o++)
        {
            if(this.grid[o][x].value == n)
                count++;
        }

        return count;
    }

    public int quadrantContains(int quadrant, int n)
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
                if(this.grid[i + yOffset][o + xOffset].value == n)
                    count++;
            }
        }

        return count;
    }

    public int getCell(int x, int y)
    {
        return this.grid[y][x].value;
    }
    
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

    public void incrementCell(int x, int y)
    {
    	if(!this.grid[y][x].modifiable)
    		 throw new RuntimeException("Cannot increment cell; user defined");
    	else if(this.grid[y][x].value == 9)
            throw new RuntimeException("Cannot increment cell; maximum possible cell value");
        else
            this.grid[y][x].value++;
    }

    public void resetCell(int x, int y)
    {
        if(!this.grid[y][x].modifiable)
        	 throw new RuntimeException("Cannot reset cell; user defined");
    	this.grid[y][x].value = 0;
    }
    
    public boolean isCellModifiable(int x, int y)
    {
    	return this.grid[y][x].modifiable;
    }
    
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
    
    private class GridCell
    {
    	public boolean modifiable;
    	public int value;
    }
}