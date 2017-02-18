import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class SudokuSolverGUI extends JFrame implements ActionListener
{
	private static final long serialVersionUID = -3394395981172005040L;
	private CellButton[][] buttons;
	private JTextArea statsText;
	private JCheckBox stepThrough;
	private JTextField stepThroughTime;
	private JButton solve;
	private JButton reset;

	public static void main(String[] args)
	{
		//Set Look and Feel
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e){}
		
		//Construct Frame
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				SudokuSolverGUI userInterface = new SudokuSolverGUI();
				userInterface.setVisible(true);
			}
		});
	}
	
	private SudokuSolverGUI()
	{
		super("Sudoku Solver - Brandon Richardson, 2017");
		this.setSize(450,500);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.init();
	}
	
	private void init()
	{
		(this.getContentPane()).setLayout(new BorderLayout());
		
		this.buttons = new CellButton[9][9];
		for(int row = 0; row < this.buttons.length; row++)
		{
			for(int col = 0; col < this.buttons.length; col++)
			{
				this.buttons[row][col] = new CellButton();
				this.buttons[row][col].setFocusable(false);
				this.buttons[row][col].setMargin(new Insets(1,1,1,1));
				this.buttons[row][col].addActionListener(new CellButtonListener(row,col));
			}
		}
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(9,9));
		for(int row = 0; row < this.buttons.length; row++)
		{
			for(int col = 0; col < this.buttons.length; col++)
			{
				buttonPanel.add(this.buttons[row][col]);
			}
		}
		
		(this.getContentPane()).add(buttonPanel, BorderLayout.CENTER);
		
		this.solve = new JButton("Solve");
		this.solve.setFocusable(false);
		this.solve.addActionListener(this);
		this.reset = new JButton("Reset");
		this.reset.setFocusable(false);
		this.reset.addActionListener(this);
		
		this.statsText = new JTextArea();
		this.statsText.setRows(2);
		this.statsText.setEditable(false);
		this.statsText.setBackground(this.getBackground());
		this.stepThrough = new JCheckBox("Step Through");
		this.stepThroughTime = new JTextField("0.1");
		
		JPanel statsControlPanel = new JPanel();
		statsControlPanel.setLayout(new BorderLayout());
		statsControlPanel.add(this.statsText, BorderLayout.CENTER);
		
		JPanel step = new JPanel();
		step.setLayout(new BorderLayout());
		step.add(this.stepThrough, BorderLayout.PAGE_START);
		step.add(this.stepThroughTime, BorderLayout.PAGE_END);
		statsControlPanel.add(step, BorderLayout.LINE_END);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		controlPanel.add(statsControlPanel, BorderLayout.PAGE_END);
		
		JPanel optionsPane = new JPanel();
		optionsPane.setLayout(new GridLayout(1,2));
		optionsPane.add(this.solve);
		optionsPane.add(this.reset);
		
		controlPanel.add(optionsPane, BorderLayout.CENTER);
		(this.getContentPane()).add(controlPanel, BorderLayout.PAGE_END);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == this.solve)
		{
			try
			{
				if(this.stepThrough.isSelected())
				{
					double stepTime;
					try
					{
						stepTime = Double.valueOf(this.stepThroughTime.getText());
					}
					catch(NumberFormatException e)
					{
						stepTime = 0.1;
						this.stepThroughTime.setText(Double.toString(stepTime));
					}
				}
				
				int[][] grid = new int[9][9];
				for(int row = 0; row < grid.length; row++)
				{
					for(int col = 0; col < grid.length; col++)
					{
						grid[row][col] = this.buttons[row][col].value;
					}
				}
				long timeStart = System.nanoTime();
				SudokuSolver solver = new SudokuSolver(grid);
				grid = solver.solve();
				long timeEnd = System.nanoTime();
				for(int row = 0; row < grid.length; row++)
				{
					for(int col = 0; col < grid.length; col++)
					{
						this.buttons[row][col].value = grid[row][col];
						this.buttons[row][col].setText(grid[row][col] + "");
					}
				}
				double timeElapsed = timeEnd - timeStart;
				timeElapsed /= 1000000;
				
				//Output Metrics
				String stats = "Time Elapsed: " + timeElapsed + "ms" + "\n" + "Iterations: " + solver.getIterations();
				this.statsText.setText(stats);
			}
			catch (Exception e)
			{
				this.statsText.setText(e.getMessage());
			}
		}
		else if(event.getSource() == this.reset)
		{
			for(int row = 0; row < this.buttons.length; row++)
			{
				for(int col = 0; col < this.buttons.length; col++)
				{
					this.buttons[row][col].value = 0;
					this.buttons[row][col].setText("");
				}
			}
			
			this.statsText.setText("");
		}
	}
	
	public class CellButton extends JButton
	{
		private static final long serialVersionUID = -9162048024531308167L;
		public int value = 0;
		
		public CellButton()
		{
			super();
		}
	}
	
	public class CellButtonListener implements ActionListener
	{
		private final int row;
		private final int col;
		
		public CellButtonListener(int row, int col)
		{
			this.row = row;
			this.col = col;
		}

		@Override
		public void actionPerformed(ActionEvent event)
		{
			CellButton button = SudokuSolverGUI.this.buttons[row][col];
			button.value = (button.value == 9) ? 0 : button.value + 1;
			String val = Integer.toString(button.value);
			button.setText(val);
		}
	}
}