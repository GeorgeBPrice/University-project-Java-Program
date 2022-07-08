/**
 *  COIT11134 Object Oriented Programming
 *  Assignment 2
 *  By George Price
 */
import javax.swing.*;

public class CQSProgramRunner
{
	// Launching the CQS Program
	public static void main(String [] args)
	{
		SolarSystemGUI system = new SolarSystemGUI();
		system.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		system.setSize(800, 850);
		system.setTitle("CQ Solar Power System Installers (CQS)");
		system.setResizable(false);
		system.setVisible(true);
	}
}