package algorithm;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * This class allow us to display information about our simulation and control the pause 
 * and the stopping of the program.
 * @author jrl
 *
 */
public class Controller {
	
	/***********************************************************************************/
	/*                             Display objects                                     */
	/***********************************************************************************/
	
	/**
	 * the window displaying everything
	 */
	private JFrame window = new JFrame();
	
	/**
	 * the global panel containing the elements vertically
	 */
	private JPanel mainPanel = new JPanel();
	
	/**
	 * Panel that display the buttons horizontally
	 */
	private JPanel buttonsPanel = new JPanel();
	
	/**
	 * label that will hold the title
	 */
	private JLabel title = new JLabel();

	/**
	 * the button that ends the simulation
	 */
	private JButton stop = new JButton("stop");
	
    /**
     * The button that allows us to pause and resume the program
     */
	private JButton playPause = new JButton("play/pause");
	
	/**
	 * area for displaying whatever the simulation wants
	 */
	private JTextArea textArea = new JTextArea("click play/pause to start");
	
	/***********************************************************************************/
	/*                              others objects                                     */
	/***********************************************************************************/
	
	/**
	 * The learning algorithm
	 */
	private LearningAlgorithm algorithm;
	
	/***********************************************************************************/
	/*                           Initialization methods                                */
	/***********************************************************************************/
	
	private void initPannels() {
		//set the layout
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        //put the objects in their place
        mainPanel.add(title);
        mainPanel.add(buttonsPanel);
        mainPanel.add(textArea);
        buttonsPanel.add(playPause);
        buttonsPanel.add(stop);
        //others elements
        textArea.setEditable(false);
        //display the window
        window.add(mainPanel);
        window.setSize(400, 400);
        window.setVisible(true);
	}
	
	private void initButtons() {
		//stop button
		stop.addActionListener(e -> {
    		if(!algorithm.isPaused()) algorithm.playPause();
    		algorithm.endProgram();
    		window.dispose();
    		System.exit(0);
        });
		//play/pause button
		playPause.addActionListener(e -> {
        	algorithm.playPause();
        	if (algorithm.isPaused()) {
        		while (algorithm.getState() != Thread.State.WAITING);
        		textArea.setText("program paused");
        	} else {
        		textArea.setText("");
        	}
        });
	}
	
	/***********************************************************************************/
	/*                                  constructor                                    */
	/***********************************************************************************/
	
	/**
	 * Constructor that open a window and launch the simulation
	 * @param algorithm the learning algorithm that will be used
	 */
	public Controller(LearningAlgorithm algorithm) {
		this.algorithm = algorithm;
		this.initButtons();
		this.initPannels();
		algorithm.start();
	}
}
