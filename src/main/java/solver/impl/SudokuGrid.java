package solver.impl;

import solver.Grid;

public class SudokuGrid implements Grid {

    private GridCell[][] grid;

    /**
	 * Initializes a 9x9 sudoku grid with values described by the parameter grid. The grid array must be an array of
	 * dimensions 9x9, where empty cells have a value of 0. Empty cells are modifiable, and cells with values 1-9 will
	 * be marked as unmodifiable, and will not respond to incrementCell(), setCell() or resetCell().
	 *
	 * Each cell is assigned an array of valid cell values based on the initial cell values. If the cell has only one
	 * possible value, the cell value is set to this value and is marked as unmodifiable. This will simplify the puzzle
	 * and decrease the amount of time needed to find a solution. This may have a domino effect, spawning other cells to
	 * have only a single valid cell value, which will simplify the puzzle even further.
	 *
	 * @param grid The initial sudoku cells given, used to construct a grid object
	 * */
    public SudokuGrid(int[][] grid) {
		//Reject invalid arguments
		if(grid == null)
			throw new IllegalArgumentException("provided grid is null");

		if(grid.length != 9)
			throw new IllegalArgumentException("provided grid has invalid dimensions");

		this.grid = new GridCell[9][9];
		for(int row = 0; row < this.grid.length; row++) {
			for(int col = 0; col < this.grid[0].length; col++) {
				this.grid[row][col] = new GridCell();
			}
		}
    	
    	//Build SudokuGrid from provided grid
    	for(int row = 0; row < grid.length; row++) {
        	//Reject Invalid Parameter Array
	    	if(grid[row].length != 9)
	        	throw new IllegalArgumentException("provided grid has invalid dimensions");
        	
	    	//Fill Grid from Provided Grid
        	for(int col = 0; col < grid[row].length; col++) {
    			if(grid[row][col] < 0 || grid[row][col] > 9)
    				throw new IllegalArgumentException("provided grid has invalid cell; value: " + grid[row][col]);
    			
    			if(grid[row][col] == 0) {
    				this.grid[row][col].modifiable = true;
    				continue;
    			}
    			
				this.grid[row][col].value = grid[row][col];
				this.grid[row][col].modifiable = false;
    		}
    	}
        
        if(!this.isValid()) {
			throw new IllegalArgumentException("invalid sudoku puzzle; cannot be solved");
		}

		this.simplyGrid();
    }

    /**
	 * Attempt to simply the sudoku grid by inference.
	 * */
    public void simplyGrid() {
		//Fill Possible Cell Values Array
		possibleCellArrayFind:
		do {
			for(int row = 0; row < grid.length; row++) {
				for(int col = 0; col < grid[row].length; col++) {
					int quadrant = this.getQuadrantId(col, row);
					int[] possibleCellValues = new int[9];

					int index = 0;
					for(int number = 1; number < 10; number++) {
						if(!this.isCellModifiable(col, row)) {
							this.grid[row][col].validCellValues = null;
							break;
						}

						if(this.rowContains(row, number, true) == 0
								&& this.colContains(col, number, true) == 0
								&& this.quadrantContains(quadrant, number, true) == 0)
							possibleCellValues[index++] = number;
					}

					int[] newArray = new int[index];
					System.arraycopy(possibleCellValues, 0, newArray, 0, index);
					possibleCellValues = newArray;

					//Attempt to simplify grid
					if(possibleCellValues.length == 1) {
						this.grid[row][col].value = possibleCellValues[0];
						this.grid[row][col].validCellValues = null;
						this.grid[row][col].modifiable = false;
						row = -1;
						break;
					}

					this.grid[row][col].validCellValues = possibleCellValues;
				}
			}

			//Iterate Over Number of Rows/Columns/Quadrants
			int count, index;
			int indexX, indexY;
			for(int identifier = 0; identifier < 9; identifier++) {
				//Check Row Frequency
				rowFreq:
				for(int value = 1; value < 10; value++) {
					count = 0;
					index = 0;
					for(int col = 0; col < this.grid[identifier].length; col++) {
						for(int possibleVal : this.grid[identifier][col].validCellValues) {
							if(possibleVal == value) {
								count++;
								index = col;
							}
						}

						if(count > 1)
							continue rowFreq;
					}

					if(count == 1) {
						this.grid[identifier][index].validCellValues = null;
						this.grid[identifier][index].value = value;
						this.grid[identifier][index].modifiable = false;
						continue possibleCellArrayFind;
					}
				}

				//Check Column Frequency
				colFreq:
				for(int value = 1; value < 10; value++)
				{
					count = 0;
					index = 0;
					for(int row = 0; row < this.grid.length; row++) {
						for(int possibleVal : this.grid[row][identifier].validCellValues) {
							if(possibleVal == value) {
								count++;
								index = row;
							}
						}

						if(count > 1)
							continue colFreq;
					}

					if(count == 1) {
						this.grid[identifier][index].validCellValues = null;
						this.grid[identifier][index].value = value;
						this.grid[identifier][index].modifiable = false;
						continue possibleCellArrayFind;
					}
				}

				//Check Quadrant Frequency
				int xOffset, yOffset;
				if(identifier <= 2) {
					xOffset = identifier * 3;
					yOffset = 0;
				} else if(identifier <= 5) {
					xOffset = ((identifier - 3) * 3);
					yOffset = 3;
				} else {
					xOffset = ((identifier - 6) * 3);
					yOffset = 6;
				}

				quadFreq:
				for(int value = 1; value < 10; value++) {
					count = 0;
					indexX = 0;
					indexY = 0;
					for(int row = yOffset; row < 3; row++) {
						for(int col = xOffset; col < 3; col++) {
							for(int possibleVal : this.grid[row][col].validCellValues) {
								if(possibleVal == value) {
									count++;
									indexY = row;
									indexX = col;
								}
							}

							if(count > 1)
								continue quadFreq;
						}
					}

					if(count == 1) {
						this.grid[indexY][indexX].validCellValues = null;
						this.grid[indexY][indexX].value = value;
						this.grid[indexY][indexX].modifiable = false;
						continue possibleCellArrayFind;
					}
				}
			}

			break;
		} while(true);
	}

    /**
	 * Validate the entire grid. Validates each row, column and quadrant individually.
	 *
	 * @see SudokuGrid#isColumnComplete(int)
	 * @see SudokuGrid#isRowComplete(int)
	 * @see SudokuGrid#isQuadrantComplete(int)
	 * @return true if the grid is solved, false otherwise.
	 * */
    public boolean isSolved() {
        for(int identifier = 0; identifier < 9; identifier++) {
        	if(!this.isRowComplete(identifier))
                return false;
        	
        	if(!this.isColumnComplete(identifier))
                return false;
        	
        	if(!this.isQuadrantComplete(identifier))
                return false;
        }

        return true;
    }

    /**
	 * Return whether the grid is valid. A grid is valid if each row, col and quadrant do not contain duplicate values.
	 *
	 * An empty cell is still considered valid.
	 *
	 * @return true if the grid is valid, false otherwise.
	 * */
    public boolean isValid() {
		for(int identifier = 0; identifier < 9; identifier++) {
			for(int value = 1; value <= 9; value++) {
				if(this.rowContains(identifier, value, false) > 1)
					return false;

				if(this.colContains(identifier, value, false) > 1)
					return false;

				if(this.quadrantContains(identifier, value, false) > 1)
					return false;
			}
		}

		return true;
	}

    /**
	 * Access the value of the grid cell at the given row and column.
	 *
	 * @param col column
     * @param row row
     * @return the cell value at row and column
	 * */
    public int getCellValue(int col, int row) {
        return this.grid[row][col].value;
    }

    /**
	 * Compute the quadrant number specified by the row and column parameters.
	 *
	 * @param col column
     * @param row row
     * @return the quadrant number specified by row and col
	 * */
    public int getQuadrantId(int col, int row) {
    	if(col >= 0 && col <= 2) {
    		if(row >= 0 && row <= 2)
    			return 0;

    		if(row >= 3 && row <= 5)
    			return 3;

    		return 6;
    	}

    	if(col >= 3 && col <= 5) {
    		if(row >= 0 && row <= 2)
    			return 1;

    		if(row >= 3 && row <= 5)
    			return 4;

    		return 7;
    	}

		if(row >= 0 && row <= 2)
			return 2;

		if(row >= 3 && row <= 5)
			return 5;

		return 8;
    }
    
    /**
	 * Increment the cell at given row and column.
	 *
	 * @throws IllegalStateException if cell is not modifiable, or the cell value is 9.
     * @param col column
     * @param row row
	 * */
    public void incrementCell(int col, int row) {
    	if(!this.grid[row][col].modifiable)
    		throw new IllegalStateException("Cannot increment cell; user defined");

    	if(this.grid[row][col].value == 9)
            throw new IllegalStateException("Cannot increment cell; maximum possible cell value");

    	this.grid[row][col].value++;
    }

    /**
	 * Set the value of a given cell.
	 *
	 * @param col column
	 * @param row row
	 * @param value the desired cell value
	 * @throws RuntimeException if the cell is not modifiable or the value does not fall within the range 1-9
	 * */
    public void setCellValue(int col, int row, int value) {
    	if(!this.grid[row][col].modifiable)
    		throw new RuntimeException("Cannot set cell value to" + value + "; cell not modifiable");

    	if(value > 9 || value < 1)
           throw new RuntimeException("Cannot set cell value to" + value + "; must be 1-9");

    	this.grid[row][col].value = value;
    }
    
    /**
	 * Reset the cell to an empty cell with value 0.
	 *
	 * @throws IllegalStateException if cell is not modifiable.
     * @param col column
     * @param row row
     */
    public void resetCellValue(int col, int row) {
        if(!this.grid[row][col].modifiable)
        	 throw new IllegalStateException("Cannot reset user-defined cell");

    	this.grid[row][col].value = 0;
    }
    
    /** Returns whether the cell is modifiable.
     * @param col column
     * @param row row
     * @return whether the cell is modifiable.
     */
    public boolean isCellModifiable(int col, int row) {
    	return this.grid[row][col].modifiable;
    }
    
    /** Access an array containing all the possible values for a given cell.
      * Based only on on the initial grid unmodifiable cell values.
      * @param col column
      * @param row row
      * @return an array of all possible values for that cell
      * */
    public int[] getPossibleCellValues(int col, int row) {
    	return this.grid[row][col].validCellValues;
    }
    
    /**
	 * Build an array of integers that represents the sudoku grid at the moment of being invoked.
	 *
     * @return a 2d array of integers representing this sudoku grid.
	 * */
    public int[][] getGrid() {
    	int[][] newGrid = new int[9][9];
    	
    	for(int row = 0; row < this.grid.length; row++) {
    		for(int col = 0; col < this.grid[row].length; col++) {
    			newGrid[row][col] = this.grid[row][col].value;
    		}
    	}
    	
    	return newGrid;
    }

    /**
     * Test the frequency of a given number across a row.
     *
     * @param row row number
     * @param num number to find
     * @param lazy if true, method only returns 1 on the existence of num in row, 0 otherwise.
     * @return the number of occurrences of num in row, or if lazy is true return 1.
     * */
    public int rowContains(int row, int num, boolean lazy) {
        return entityContains(row, null, null, num, lazy);
    }

    /**
     * Test the frequency of a given number across a column.
     *
     * @param col column
     * @param num
     * @return the number of occurrences of num in col
     * */
    public int colContains(int col, int num, boolean lazy) {
        return entityContains(null, col, null, num, lazy);
    }

    /**
     * Test the frequency of a given number in a quadrant.
     *
     * @param quadrant
     * @param num
     * @return the number of occurances of num in quadrant
     * */
    public int quadrantContains(int quadrant, int num, boolean lazy) {
        return entityContains(null, null, quadrant, num, lazy);
    }


    /**
	 * Validate grid row. Validation is done in two steps, first with an initial non-deterministic step, followed by a
	 * deterministic step.
	 *
	 * The non-deterministic step rejects any grid whose sum of cell values across a given row is not equal to 45, or
	 * one with an empty cell. This acts as a fast way to invalidate a row.
	 *
	 * The deterministic step tests the frequency of values in a row. The row is valid iff the frequency of numbers
	 * across a row are exactly 1, and the only numbers that appear are digits 1 through 9.
	 *
	 * @param row row
	 * @return true if the row is valid, false otherwise.
	 * */
	private boolean isRowComplete(int row) {
		return isCompleteRowColQuadrant(row, null, null);
	}

	/** Validate grid column. Validation is done in two steps, first with an initial non-deterministic step, followed
	 * by a deterministic step.
	 *
	 * The non-deterministic step rejects any grid whose sum of cell values across a given column is not equal to 45, or
	 * one with an empty cell. This acts as a fast way to invalidate a column.
	 *
	 * The deterministic step tests the frequency of values in a column. The column is valid iff the frequency of
	 * numbers across a column are exactly 1, and the only numbers that appear are digits 1 through 9.
	 *
	 * @param col column
	 * @return true if the column is valid, false otherwise.
	 * */
	private boolean isColumnComplete(int col) {
		return isCompleteRowColQuadrant(null, col, null);
	}

	/**
	 * Validate grid quadrant. Validation is done in two steps, first with an initial non-deterministic step, followed
	 * by a deterministic step.
	 *
	 * The non-deterministic step rejects any grid whose sum of cell values in a given quadrant is not equal to 45, or
	 * one with an empty cell. This acts as a fast way to invalidate a quadrant.
	 *
	 * The deterministic step tests the frequency of values in a quadrant. The quadrant is valid iff the frequency of
	 * numbers are exactly 1, and the only numbers that appear are digits 1 through 9.
	 *
	 * @param quadrant quadrant number
	 * @return true if the quadrant is valid, false otherwise.
	 * */
	private boolean isQuadrantComplete(int quadrant) {
		return isCompleteRowColQuadrant(null, null, quadrant);
	}

	private boolean isCompleteRowColQuadrant(Integer row, Integer col, Integer quadrant) {
		//If Sum of Cell Values != 45 or Empty Cell Encountered, Row is Invalid
		int sum = 0;
		int[] valFrequency = new int[9];

		if(quadrant != null) {
			int xOffset = this.getQuadrantIndexingOffset(quadrant, true);
			int yOffset = this.getQuadrantIndexingOffset(quadrant, false);

			for(int rowIndex = 0; rowIndex < 3; rowIndex++) {
				for(int colIndex = 0; colIndex < 3; colIndex++) {
					int value = this.grid[rowIndex + yOffset][colIndex + xOffset].value;
					if(value == 0)
						return false;

					valFrequency[value - 1]++;
					sum += value;
				}
			}
		} else {
			for(int index = 0; index < 9; index++) {
				int value;
				if(row != null)
					value = this.grid[row][index].value;
				else
					value = this.grid[index][col].value;

				if(value == 0)
					return false;

				valFrequency[value - 1]++;
				sum += value;
			}
		}

		if(sum != 45)
			return false;

		//If Sum of Cell Values == 45, Check Frequency of Numbers
		for(int freq : valFrequency) {
			if(freq != 1)
				return false;
		}

		return true;
	}

	/**
	 * Get an indexing offset for iterating over cells in a quadrant.
	 *
	 * @param quadrant the quadrant number
	 * @param xOffset if true, returns the xOffset
	 * @return an indexing offset
	 * */
	private int getQuadrantIndexingOffset(int quadrant, boolean xOffset) {
		if(quadrant >= 0 && quadrant <= 2) {
			if(xOffset)
				return quadrant * 3;

			return 0;
		}

		if(quadrant >= 3 && quadrant <= 5) {
			if(xOffset)
				return ((quadrant - 3) * 3);

			return 3;
		}

		if(xOffset)
			return ((quadrant - 6) * 3);

		return 6;
	}

	private int entityContains(Integer row, Integer col, Integer quadrant, int num, boolean lazy) {
		if(lazy) {
			if(quadrant != null) {
				int xOffset = this.getQuadrantIndexingOffset(quadrant, true);
				int yOffset = this.getQuadrantIndexingOffset(quadrant, false);

				for(int rowIndex = 0; rowIndex < 3; rowIndex++) {
					for(int colIndex = 0; colIndex < 3; colIndex++) {
						if(this.grid[rowIndex + yOffset][colIndex + xOffset].value == num)
							return 1;
					}
				}

				return 0;
			}

			if(row != null) {
				for(int index = 0; index < 9; index++) {
					if(this.grid[row][index].value == num)
						return 1;
				}

				return 0;
			}

			for(int index = 0; index < 9; index++) {
				if(this.grid[index][col].value == num)
					return 1;
			}

			return 0;
		}

		int count = 0;
		if(row != null) {
			for(int colIndex = 0; colIndex < 9; colIndex++) {
				if(this.grid[row][colIndex].value == num)
					count++;
			}
		}

		if(col != null) {
			for(int rowIndex = 0; rowIndex < 9; rowIndex++) {
				if(this.grid[rowIndex][col].value == num)
					count++;
			}
		}

		if(quadrant != null) {
			int xOffset = this.getQuadrantIndexingOffset(quadrant, true);
			int yOffset = this.getQuadrantIndexingOffset(quadrant, false);

			for(int rowIndex = 0; rowIndex < 3; rowIndex++) {
				for(int colIndex = 0; colIndex < 3; colIndex++) {
					if(this.grid[rowIndex + yOffset][colIndex + xOffset].value == num)
						count++;
				}
			}
		}

		return count;
	}
    
    /**
	 * A class representing a single cell in a sudoku grid.
	 * Each cell has a value, and can be either modifiable or not modifiable.
	 * */
    private class GridCell
    {
    	public boolean modifiable = true;
    	public int value = 0;
    	public int[] validCellValues = null;
    }
}