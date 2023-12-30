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
	/*                                  constructor                                    */
	/***********************************************************************************/
	
	/**
	 * Constructor for the controller
	 * @param algorithm the learning algorithm that will be used
	 */
	public Controller(LearningAlgorithm algorithm) {
		this.algorithm = algorithm;
	}
	
	/***********************************************************************************/
	/*                                  functions                                      */
	/***********************************************************************************/
	
	/**
	 * This function contains the code to proprely end the program.
	 */
	public void stop() {
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
	public void playPause() {
    	algorithm.playPause();
    	if (algorithm.isPaused()) {
    		while (algorithm.getState() != Thread.State.WAITING);
    		textArea.setText("program paused");
    	} else {
    		textArea.setText("program running...");
    	}
	}
	
	/**
	 * Function to start the learning algorithm from the code. <br>
	 * The program will start immediately.
	 */
	public void startAlgorithm() {
		this.algorithm.start();
		this.playPause();
	}
	
	/**
	 * This private function sets up the interraction of the buttons
	 */
	private void initInterractions() {
		//closing the window
		window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	stop();
            }
        });
		//stop button
		stop.addActionListener(e -> stop());
		//play/pause button
		playPause.addActionListener(e -> playPause());
	}
	
	/**
	 * This private function sets up the display
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
	 * Function that opens a window for a direct control of the use over the learning 
	 * algorithm. <br>
	 * The program will start in pause.
	 */
	public void openWindow() {
		this.initInterractions();
		this.initPannels();
		this.algorithm.start();
	}
}
