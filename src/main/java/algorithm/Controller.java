package algorithm;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
	/*                               action methods                                    */
	/***********************************************************************************/
	
	/**
	 * This function contains the code to proprely end the program.
	 */
	private void endProgram() {
		algorithm.endProgram();
		try {
		    algorithm.join();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		    algorithm.interrupt();
		}
		window.dispose();
	}
	
	/**
	 * This function contains the pause to proprely pause and resume the program.
	 */
	private void pauseProgram() {
    	algorithm.playPause();
    	if (algorithm.isPaused()) {
    		while (algorithm.getState() != Thread.State.WAITING);
    		textArea.setText("program paused");
    	} else {
    		textArea.setText("program running...");
    	}
	}
	
	/***********************************************************************************/
	/*                           Initialization methods                                */
	/***********************************************************************************/
	
	/**
	 * This private function concentrate the setting up of the display.
	 */
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
	
	/**
	 * This private function sets up the interraction with the user
	 */
	private void initInterractions() {
		//closing the window
		window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	endProgram();
            }
        });
		//stop button
		stop.addActionListener(e -> {
    		endProgram();
        });
		//play/pause button
		playPause.addActionListener(e -> {
			pauseProgram();
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
		this.initInterractions();
		this.initPannels();
		algorithm.start();
	}
}
