/**
 *  COIT11134 Object Oriented Programming
 *  Assignment 2
 *  By George Price
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.MaskFormatter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;

public class SolarSystemGUI extends JFrame
{
	// all panels for GUI
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();
	JPanel panel4 = new JPanel();
	JPanel panel5 = new JPanel();
	JPanel selectTechPanel = new JPanel();
	JPanel addTechPanel = new JPanel();
	JPanel leftPanel = new JPanel();
	JPanel centerPanel = new JPanel();
	JPanel rightPanel = new JPanel();

	// arrays for temporarily storing and processing input data
	private ArrayList<NewInstallation> installations = new ArrayList<NewInstallation>();
    private ArrayList<Technician> techies = new ArrayList<Technician>();

	// strings arrays of data to populate JComboboxes
	private String[] solarTracker = new String[]{"--select--","Single axis", "Dual axis", "Stationary"};
	private String[] batteryType = new String[]{"Not Required", "50 Ah", "100 Ah", "150 Ah", "200 Ah"};

	// componants and controls for GUI
	private JComboBox<String> trackerBox = new JComboBox<>(solarTracker);
	private JComboBox<String> batteryBox = new JComboBox<>(batteryType);
	private JComboBox<String> technicianBox = new JComboBox<>();
	private JComboBox<String> searchTechieBox = new JComboBox<>();
	private JComboBox<String> searchInstallsBox = new JComboBox<>();
	private JRadioButton onGrid = new JRadioButton("On Grid", true);
	private JRadioButton offGrid = new JRadioButton("Off Grid");

	private JButton newTechButton = new JButton("Add Technician");
	private JButton newButton = new JButton("Save New Installation");
	private JButton clearFieldsButton = new JButton("Clear Inputs");
	private JButton saveDataButton = new JButton("Save to File");
	private JButton searchButton = new JButton("Display by Technician");
	private JButton displayButton = new JButton("Display All");
	private JButton clearButton = new JButton("Clear Text");
	private JButton exitButton = new JButton("Exit");

	private JTextField tName = new JTextField();
	private JFormattedTextField tPhone = new JFormattedTextField();
	private JLabel validationLabel = new JLabel();
	private JTextField cName = new JTextField();
	private JFormattedTextField cPhone = new JFormattedTextField();
	private JTextField jobAddress = new JTextField();
	private JFormattedTextField installStartDate = new JFormattedTextField();
	private JFormattedTextField installFinishDate = new JFormattedTextField();
	private JTextField wattsHour = new JTextField();
	private JLabel selectTechieLabel = new JLabel("SELECT INSTALLATION TECHNICIAN  ");

	private JTextArea textArea = new JTextArea(19, 60);
	DefaultCaret caret = (DefaultCaret) textArea.getCaret();


	//----------- methods for reading/write files, & deleting installs/technicians ---------//


	// read in techie txt file at program open
	private void readTechiesFile(String fileName)
	{
		techies.clear();
		try
		{
			FileReader fileReader = new FileReader(fileName);
			Scanner inFile = new Scanner(fileReader);

			while (inFile.hasNext()==true)
			{
				String[] parts = inFile.nextLine().split("#");
				if(parts.length == 2)
				{
					Technician newTechie = new Technician(parts[0], parts[1]);

					techies.add(newTechie);
				}
				else
				{
					throw new IOException();
				}
			}
			inFile.close();
			fileReader.close();
		}
		//a simple top level bad file read catch
		catch (IOException error)
		{
			textArea.append("\nERROR: the Technicians.txt file could not be read!");
		}
	}


	// read in installations txt file at program open
	private void readInstsFile(String fileName)
	{
		installations.clear();
		try
		{
			FileReader fileReader = new FileReader(fileName);
			Scanner inFile = new Scanner(fileReader);

			while (inFile.hasNext()==true)
			{
				String[] parts = inFile.nextLine().split("#");
				if(parts.length == 12)
				{
					NewInstallation newInstall = new NewInstallation(
						parts[0], parts[1], parts[2], parts[3], parts[4], parts[5],
							parts[6], parts[7], parts[8], parts[9], parts[10], parts[11] );

					installations.add(newInstall);
				}
				else
				{
					throw new IOException();
				}
			}
			inFile.close();
			fileReader.close();
		}

		//a simple top level bad file read catch
		catch (IOException error)
		{
			textArea.append("\nERROR: the Installations.txt file could not be read!");
		}
	}


	// add a new technician
	public void addTechie()
	{
		String techName = tName.getText();
		String techPhone = tPhone.getText();
		String emptyPhone = "(  )          ";
		boolean nameFound = false;

		Technician addTechie = new Technician(techName, techPhone);

		// check if technician already exist
		for(int i = 0; i < techies.size(); i = i + 1)
		{
			if(techName.equalsIgnoreCase(techies.get(i).getTechName()))
			{
				nameFound = true;
			}
		}

		//check if input name and phone fields are empty
		if(tPhone.getText().trim().isEmpty() || techPhone.contains(emptyPhone))
		{
			textArea.setText("\nINPUT ERROR: Please fill 'Name' and 'Phone'.");
		}

		// if technician already exists, alert user
		else if(nameFound == true)
		{
		JOptionPane.showMessageDialog(null, "Technician '" + techName + "' already exists!",
			"Error! Duplicate Technician", JOptionPane.ERROR_MESSAGE);
		}

		// otherwise add the new technician
		else
		{
			techies.add(addTechie);
			textArea.setText("\n\nNEW TECHNICIAN ADDED:"
							+ "\nName: " + tName.getText()
							+ "\nPhone: " + tPhone.getText()
							+ "\n\nYou may now select this technician from the list." + "\n");

			// save to file
			writeTechieFile("src/data/Technicians.txt");

			// refresh technicians in the technician combobox selector
			refreshTechie(technicianBox.getSelectedIndex(),tName.getText());

			// clear fields on success
			tName.setText("");
			tPhone.setValue(null);
		}
	} // end addTechie()


	// refresh select technician combobox after new one is added
	private void refreshTechie(int index, String name)
	{
		technicianBox.setSelectedItem(name);
	}


	// write new addTechie() to txt file
	public void writeTechieFile(String fileName)
	{
		String techName = tName.getText();
		String techPhone = tPhone.getText();

		try
		{
			Formatter outPutFile = new Formatter(fileName);

			for(int i=0; i < techies.size(); i++)
			{
				outPutFile.format( "%s#%s\n",
								techies.get(i).getTechName(),
								techies.get(i).getTechPhone() );
			}
			outPutFile.close();
		}
		//a simple top level bad file write catch
		catch (IOException error)
		{
			textArea.setText("\nERROR: the Technicians.txt file could not be written!"
				+ "\nPlease check the Technicians.txt file is in the data folder.");
		}
	}


	// write new addInstallation() entered by user, to txt file
	public void writeInstsFile(String fileName)
	{
		try
		{
			Formatter outPutFile = new Formatter(fileName);

			for(int i=0; i < installations.size(); i++)
			{
				outPutFile.format( "%s#%s#%s#%s#%s#%s#%s#%s#%s#%s#%s#%s\n",
								installations.get(i).getCustomerName(),
								installations.get(i).getCustomerPhone(),
								installations.get(i).getAddress(),
								installations.get(i).getInstallNumber(),
								installations.get(i).getStartDate(),
								installations.get(i).getFinishedDate(),
								installations.get(i).getWatts(),
								installations.get(i).getTracker(),
								installations.get(i).getGrid(),
								installations.get(i).getBattery(),
								installations.get(i).getTechName(),
								installations.get(i).getTechPhone() );
			}
			outPutFile.close();
		}
		//a simple top level bad file write catch
		catch (IOException error)
		{
			textArea.setText("\nERROR: the Installations.txt file could not be written!"
				+ "\nPlease check the Installations.txt file is in the data folder.");
		}
	}

	// searches for and displays installations by technician
	private void displayByTechie()
	{
		boolean installsFound = false;

		// fill search JCombobox with an updated Technicians list
		searchTechieBox.removeAllItems();
		for(Technician names : techies)
		{
			searchTechieBox.addItem(names.getTechName());
		}

		// prompt user to choose technician
		int input = JOptionPane.showConfirmDialog(null, searchTechieBox, "Select Technician",
					JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

		// close if x or close is selected instead
		if(input == JOptionPane.CANCEL_OPTION || input == JOptionPane.CLOSED_OPTION)
		{
			// just close JOptionPane
		}

		// check if a technicians name is selected
		else if(searchTechieBox.getSelectedIndex() == 0)
		{
			JOptionPane.showMessageDialog(null, "Please select a Technician!",
				"Name not Selected!", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			// get selected name from combobox
			String searchTechName = searchTechieBox.getSelectedItem().toString();

			// check if installations exits
			if(installations.size() == 0)
			{
				JOptionPane.showMessageDialog(null, "NO INSTALLATIONS ADDED YET!",
					"Error!", JOptionPane.ERROR_MESSAGE);
			}

			// check if selected technician has any installions, and ouput records if true
			else
			{
				textArea.setText("");
				for(int i = 0; i < installations.size(); i = i + 1)
				{
					if(searchTechName.equalsIgnoreCase(installations.get(i).getTechName()))
					{
						caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE); //turn off scrolling
						String installNumber = installations.get(i).getInstallNumber();

						textArea.append("\nINSTALLATION FOUND: ______________ INSTALLATION #"
								+ installNumber + " _______________\n\n"
								+ installations.get(i).toString() + "\n");

						installsFound = true;
					}
					caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); //turn on scrolling
				}
			}
			if (installsFound == false)
			{
				JOptionPane.showMessageDialog(null, "No installations by " + searchTechName
					+ " found.", "Not Found Error!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}// end displayByTechie()


	// remove a technician
	public void deleteTechie()
	{
		// fill search JCombobox with an updated Technicians list
		searchTechieBox.removeAllItems();
		for(Technician names : techies)
		{
			searchTechieBox.addItem(names.getTechName());
		}
		// prompt user to choose technician
		int input = JOptionPane.showConfirmDialog(null, searchTechieBox, "Select Technician to Delete",
					JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.ERROR_MESSAGE);

		// close if x or close is selected instead
		if(input == JOptionPane.CANCEL_OPTION || input == JOptionPane.CLOSED_OPTION)
		{
			// just close JOptionPane
		}

		// check if a technicians name is selected
		else if (searchTechieBox.getSelectedIndex() == 0)
		{
			JOptionPane.showMessageDialog(null, "Please select a Technician to remove.",
				"Name not Selected!", JOptionPane.ERROR_MESSAGE);
		}

		// proceed
		else
		{
			// get selected name from combobox
			String findTechName = searchTechieBox.getSelectedItem().toString();

			// find technicians index in techies array list
			textArea.setText("");
			for(int i = 0; i < techies.size(); i = i + 1)
			{
				if(findTechName.equals(techies.get(i).getTechName()))
				{
					// remove technician from techies array
					techies.remove(i);
					textArea.append("\nTechnician " + findTechName + " was successfully deleted.");

					// save amended technician array to file
					writeTechieFile("src/data/Technicians.txt");

					// refresh technicians in the intall technician combobox selector
					technicianBox.removeAllItems();
					for(Technician names : techies) {
						technicianBox.addItem(names.getTechName());
					}
				}
			}
		}
	}//deleteTechie()


	// deletes installations by their job number
	private void deleteInstallation()
	{
		// fill search JCombobox with an updated Technicians list
		searchInstallsBox.removeAllItems(); //refresh

		for(NewInstallation installs : installations)
		{
			searchInstallsBox.addItem(installs.getInstallNumber());
		}

		// prompt user to choose an install
		int input = JOptionPane.showConfirmDialog(null, searchInstallsBox, "Select Installation to Delete",
					JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.ERROR_MESSAGE);

		if(input == JOptionPane.CANCEL_OPTION || input == JOptionPane.CLOSED_OPTION)
		{
			// just close JOptionPane
		}
		else
		{
			// get selected installation number from combobox
			String instsNumber = searchInstallsBox.getSelectedItem().toString();

			// delete installation job by installNumber
			for(int i = 0; i < installations.size(); i = i + 1)
			{
				if(instsNumber.equals(installations.get(i).getInstallNumber()))
				{
					installations.remove(i);

					textArea.setText("\nInstallation (" + instsNumber + ") was successfully deleted.");

					writeInstsFile("src/data/Installations.txt");
				}
			}
		}
	}// end deleteInstallation()



	//---------------- methods for all other functions and buttons -------------------//


	// used to validate addInstallationJob()
	public boolean getVerifyInput()
	{
		boolean verifyInput = true;
		return verifyInput;
	}


	// saves new installations to the arraylist and text file
	private void saveNewInstallation()
	{
			textArea.setText("");
			addInstallation();
	}

	// clears all form inputs, & resets to default index on comboboxes
	public void clearFormFields()
	{
		tName.setText("");
		tPhone.setValue(null);
		cName.setText("");
		cPhone.setValue(null);
		jobAddress.setText("");
		installStartDate.setValue(null);
		installFinishDate.setValue(null);
		wattsHour.setText("3500 to 10000");
		onGrid.setSelected(true);
		trackerBox.setSelectedIndex(0);
		batteryBox.setSelectedIndex(0);
		technicianBox.setSelectedIndex(0);
	}

	// displays all saved installations
	public void displayAll()
	{
		String techName = "";
		String techPhone = "";
		String customerName = "";
		String customerPhone = "";
		String address = "";
		String startDate = "";
		String finishedDate = "";
		String watts = "";
		String tracker = "";
		String battery = "";
		String grid = "";
		String installNumber = "";

		NewInstallation newInstallation = new NewInstallation(customerName, customerPhone, address,
			installNumber, startDate, finishedDate, watts, tracker, grid, battery, techName, techPhone);


		// check for installations and display saved
		if (installations.size() == 0)
		{
			textArea.setText("\n ERROR: NO INSTALLATIONS ADDED YET!");
		}
		else
		{
			caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE); //turn off scrolling
			textArea.setText("");
			textArea.append("\nA total of (" + installations.size()+ ") Installation records displayed.\n");

			for(int i=0; i < installations.size(); i++)
				{
					installNumber = installations.get(i).getInstallNumber();
					textArea.append("\n_____________________________ INSTALLATION #"
						+ installNumber +" ____________________________\n\n");
							textArea.append(installations.get(i).toString() + "\n");
					//installNumber++;
			}
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); //turn on scrolling
		}
	}


	// clears main ouput TexArea
	public void clearTextArea()
	{
		textArea.setText("");
	}


	// improved exit method
	private void exit()
	{
		int select = JOptionPane.showConfirmDialog(null,
			"Do you want to exit the program?",
			"Confirm Exit", JOptionPane.YES_NO_OPTION,
			JOptionPane.INFORMATION_MESSAGE);
		if (select == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}


	public void help()
	{
		JOptionPane.showMessageDialog(null,
			"Version: 1.1"
			+ "\nCopyright CQ Solar Power System Installers (CQS)"
			+ "\n\nHOW TO ADD A NEW INSTALLATION JOB:"
			+ "\n1. Select a Technician or add new, then select."
			+ "\n2. Fill in the Building Details."
			+ "\n3. Fill in the Installation Requirements."
			+ "\n4. Click 'Save New Installation'."
			+ "\n\nProgrammed by George Price @CQU 12129164",
			"Program Help", JOptionPane.INFORMATION_MESSAGE);
	}


	//---------------- add installation plus methods to support  -------------- //


	private void addInstallation()
	{
		// find and set phone number of combobox selected technician
		String techName = technicianBox.getSelectedItem().toString();
		String techPhone = "";

		for(int i = 0; i < techies.size(); i = i + 1)
		{
			// get technician phone number
			if(techName.equalsIgnoreCase(techies.get(i).getTechName()))
			techPhone = techies.get(i).getTechPhone();
		}

		// set rest of input componants
		String customerName = cName.getText();
		String customerPhone = cPhone.getText();
		String address = jobAddress.getText();
		String startDate = installStartDate.getText();
		String finishedDate = installFinishDate.getText();
		String watts = wattsHour.getText();
		String tracker = trackerBox.getSelectedItem().toString();
		String battery = batteryBox.getSelectedItem().toString();
		String installNumber ="";
		final int LOW = 3500;
		final int HIGH = 10000;

		// set new installation job number
		int totalInstalls = (installations.size() + 1);
		installNumber = Integer.toString(totalInstalls);

		// setup what radio button is selected
		String grid = "";
		if (onGrid.isSelected())
		{
			grid = "On Grid";
		}
		else if (offGrid.isSelected())
		{
			grid = "Off Grid";
		}


		//----------------------input validation and excecption handling-------------------//


		// variables needed for data  validation
		final int PASSED = 5;
		int dataIsValid = 0;
		String emptyDate = "  /  /    ";
		String emptyPhone = "(  )          ";

		// check if text fields are filled correctly
		if( cName.getText().trim().isEmpty()
			|| customerPhone.contains(emptyPhone) ||  jobAddress.getText().trim().isEmpty()
			|| startDate.contains(emptyDate)   	  ||  finishedDate.contains(emptyDate)
			|| watts.contains("3500 to 10000") 	  ||  wattsHour.getText().trim().isEmpty())
		{
			textArea.append("\nINPUT ERROR: Please fill all Building and Installation fields.");

		}
		else
		{
			dataIsValid++;
		}

		// check if a comboBox solar tracker is selected
		if (technicianBox.getSelectedIndex() == 0)
		{
			textArea.append("\nINPUT ERROR: Please select a 'Technician'.");
		}
		else
		{
			dataIsValid++;
		}

		// check if a comboBox solar tracker is selected
		if (trackerBox.getSelectedItem().toString()== "--select--")
		{
			textArea.append("\nINPUT ERROR: Please select a 'Solar Tracker Type'.");
		}
		else
		{
			dataIsValid++;
		}

		// check if a comboBox battery type is selected
		if (offGrid.isSelected() && batteryBox.getSelectedItem().toString()== "Not Required")
		{
			textArea.append("\nINPUT ERROR: Please choose a 'Battery Type' for Off Grid Installation.");
		}
		else
		{
			dataIsValid++;
		}

		// reset battery box if radiobutton was changed back to OnGrid by user
		if (onGrid.isSelected())
		{
			batteryBox.setSelectedItem("Not Required");
		}

		// check if Watt p/h range between: 3,500 -> 10,000
		try
		{
			int wattPH = Integer.parseInt(watts);
			if (wattPH < LOW || wattPH > HIGH)
			{
				textArea.append("\nINPUT ERROR: Please enter a WH range between 3,500 -> 10,000.");
			}
			else
			{
				dataIsValid++;
			}

		}
		catch(Exception e)
		{
			textArea.append("\nINPUT ERROR: Only enter numbers for 'Watt per Hour (WH)'.");
		}


		//-----------Finally if all input data is validated, ouput to ArrayList-------------//


		NewInstallation newInstallation = new NewInstallation(customerName, customerPhone, address,
			installNumber, startDate, finishedDate, watts, tracker, grid, battery, techName, techPhone);

		if (dataIsValid == PASSED)
		{
			installations.add(newInstallation);

			for(int i=0; i < installations.size(); i++)
			{
				textArea.append("\n________________________ INSTALLATION #" + installNumber
					+  " _________________________\n\n");
				textArea.append(installations.get(i).toString() + "\n");
				clearFormFields();
			}
			// write installations to file
			writeInstsFile("src/data/Installations.txt");
		}

	} // end addInstallationJob()



	// Adding all the GUI SWING compants to Panels
	public SolarSystemGUI()
	{
		// on program load, read in Technician and Installation files
		readTechiesFile("src/data/Technicians.txt");
		readInstsFile("src/data/Installations.txt");
		displayAll();

		/********** Adding settings for GUI  **********/

		//add default Layout Manager type to JPanels
		setLayout(new FlowLayout());

		//set default input field font size
		Font fieldFont = new Font("Arial", Font.PLAIN, 14);

		//add top menu bar and its menu item componants
		JMenuBar menuBar = new JMenuBar();
		menuBar.setPreferredSize(new Dimension(700,30));
		JMenu menu1 = new JMenu("File");
		JMenu menu2 = new JMenu("Help");
		JMenuItem deleteInsts = new JMenuItem("Delete an Installation", KeyEvent.VK_E); // Mnemonics
		JMenuItem deleteTechies = new JMenuItem("Delete a Technician", KeyEvent.VK_D);
		JMenuItem help = new JMenuItem("Help", KeyEvent.VK_H);
		menu1.setMnemonic(KeyEvent.VK_F);
		menu2.setMnemonic(KeyEvent.VK_H);
		deleteInsts.addActionListener(event -> deleteInstallation());
		deleteTechies.addActionListener(event -> deleteTechie());
		help.addActionListener(event -> help());
		menu1.add(deleteInsts);
		menu1.add(deleteTechies);
		menu2.add(help);
		menuBar.add(menu1);
		menuBar.add(menu2);
		setJMenuBar(menuBar);

		//setting icons on buttons - free to use from Icons8
		selectTechieLabel.setIcon(new ImageIcon("src/icons/icons8-open-in-browser-32.png"));
		newTechButton.setIcon(new ImageIcon("src/icons/icons8-save-30.png"));
		newButton.setIcon(new ImageIcon("src/icons/icons8-save-30.png"));
		clearFieldsButton.setIcon(new ImageIcon("src/icons/icons8-clear-formatting-30.png"));
		saveDataButton.setIcon(new ImageIcon("src/icons/icons8-save-30.png"));
		searchButton.setIcon(new ImageIcon("src/icons/icons8-search-folder-30.png"));
		displayButton.setIcon(new ImageIcon("src/icons/icons8-open-in-browser-32.png"));
		clearButton.setIcon(new ImageIcon("src/icons/icons8-erase-30.png"));
		exitButton.setIcon(new ImageIcon("src/icons/icons8-close-window-30.png"));
		deleteInsts.setIcon(new ImageIcon("src/icons/icons8-close-program-48.png"));
		deleteTechies.setIcon(new ImageIcon("src/icons/icons8-close-program-48.png"));
		help.setIcon(new ImageIcon("src/icons/icons8-help-24.png"));
   		exitButton.setHorizontalTextPosition(SwingConstants.LEFT);
   		selectTechieLabel.setHorizontalTextPosition(SwingConstants.LEFT);

		//setting fonts for panel titles
		TitledBorder title1 = new TitledBorder(" TECHNICIAN DETAILS ");
		JLabel buildDetailsLabel = new JLabel("BUILDING DETAILS:");
		JLabel installRequireLabel = new JLabel("INSTALLATION REQUIREMENTS:");
		Font font = title1.getTitleFont();
		Font labelFont = new Font(font.getFamily(), Font.BOLD, 13);
		Font textAreaFont = new Font("Arial", Font.PLAIN, 14);
		Color titleColor = new Color(27, 29, 107);
		title1.setTitleFont(new Font (font.getFamily(), Font.BOLD, font.getSize()+1));
		title1.setTitleColor(titleColor);
		title1.setBorder(BorderFactory.createLineBorder(new Color(27, 29, 107)));
		buildDetailsLabel.setForeground(titleColor);
		installRequireLabel.setForeground(titleColor);
		buildDetailsLabel.setFont(labelFont);
		installRequireLabel.setFont(labelFont);
		textArea.setFont(textAreaFont);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBackground(new Color(250, 250, 250));

		//setting default background colours
		Color bgColor = new Color(242, 245, 245);  // Light blue grey
		getContentPane().setBackground(bgColor);
		panel1.setBackground(bgColor);
		panel2.setBackground(bgColor);
		panel3.setBackground(bgColor);
		panel4.setBackground(bgColor);
		panel5.setBackground(bgColor);
		addTechPanel.setBackground(bgColor);
		selectTechPanel.setBackground(bgColor);
		leftPanel.setBackground(bgColor);
		centerPanel.setBackground(bgColor);
		rightPanel.setBackground(bgColor);
		onGrid.setBackground(bgColor);
		offGrid.setBackground(bgColor);
		trackerBox.setBackground(Color.white);
		batteryBox.setBackground(Color.white);
		technicianBox.setBackground(Color.white);
		searchTechieBox.setBackground(Color.white);


		/***********  Add 1st GUI panel = Technician Details top panel  ***********/

		// phone formatter
		MaskFormatter techPhoneMask = null;
		try {
			techPhoneMask = new MaskFormatter("(##) #### ####");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// pre-fill techie combobox with Technicians.txt saved details
		for(Technician names : techies) {
			technicianBox.addItem(names.getTechName());
		}

		//update technician List when new technician is added
		newTechButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				technicianBox.removeAllItems();
				for(Technician names : techies) {
					technicianBox.addItem(names.getTechName());
				}
			}
		});

		// add techie componants to JPanel
		panel1.setLayout(new GridLayout(0,2));
		panel1.setPreferredSize(new Dimension(700,110));
		technicianBox.setPreferredSize( new Dimension(250, 26) );
		selectTechPanel.setLayout(new FlowLayout());
		selectTechPanel.add(selectTechieLabel);
		selectTechPanel.add(technicianBox);
		addTechPanel.setLayout(new GridLayout(3,0));
		tPhone = new JFormattedTextField(techPhoneMask);
		tPhone.setColumns(10);
		addTechPanel.add(new JLabel("Name: "));
		addTechPanel.add(tName);
		addTechPanel.add(new JLabel("Phone: "));
		addTechPanel.add(tPhone);
		addTechPanel.add(new JLabel("OR ADD A NEW ONE:"));
		addTechPanel.add(newTechButton);
		newTechButton.addActionListener(event -> addTechie());
		panel1.setBorder(title1);
		panel1.add(selectTechPanel);
		panel1.add(addTechPanel);
		tName.setFont(fieldFont);
		tPhone.setFont(fieldFont);
		add(panel1);


		/***********  Add 2nd GUI panel = Building + Installation columns ***********/

		panel2.setLayout(new GridLayout(0,3));
		panel2.setPreferredSize(new Dimension(700,210));
		add(panel2);

		//column 1 - Building Details sub panel
		MaskFormatter clientPhoneMask = null;
		try {
			clientPhoneMask = new MaskFormatter("(##) #### ####");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		leftPanel.setLayout(new GridLayout(0,1));
		leftPanel.add(buildDetailsLabel);
		leftPanel.add(new JLabel("Client's Name:"));
		leftPanel.add(cName);
		leftPanel.add(new JLabel("Client's Phone Number:"));
		cPhone = new JFormattedTextField(clientPhoneMask);
		leftPanel.add(cPhone);
		leftPanel.add(new JLabel("Installation Address:"));
		leftPanel.add(jobAddress);
		leftPanel.setBorder(new EmptyBorder(10, 0, 10, 10));
		cName.setFont(fieldFont);
		cPhone.setFont(fieldFont);
		jobAddress.setFont(fieldFont);
		panel2.add(leftPanel);

		//column 2 - Installation Design center sub panel
		MaskFormatter startDateMask = null;
		try {
			startDateMask = new MaskFormatter("##/##/####");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		MaskFormatter finishDateMask = null;
		try {
			finishDateMask = new MaskFormatter("##/##/####");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		wattsHour.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				wattsHour.setText("");
			}
		});
		centerPanel.setLayout(new GridLayout(0,1));
		centerPanel.add(installRequireLabel);
		centerPanel.add(new JLabel("Installation Start Date:"));
		installStartDate = new JFormattedTextField(startDateMask);
		centerPanel.add(installStartDate);
		centerPanel.add(new JLabel("Installation Finish Date:"));
		installFinishDate = new JFormattedTextField(startDateMask);
		centerPanel.add(installFinishDate);
		centerPanel.add(new JLabel("Watt per Hour (WH):"));
		centerPanel.add(wattsHour);
		wattsHour.setText("3500 to 10000");
		centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		installStartDate.setFont(fieldFont);
		installFinishDate.setFont(fieldFont);
		wattsHour.setFont(fieldFont);
		panel2.add(centerPanel);

		//column 3 - Installation Details right sub panel
		JLabel trackerLabel = new JLabel("Solar Tracker Type:");
		rightPanel.setLayout(new GridLayout(0,1));
		rightPanel.add(new JLabel(""));
		rightPanel.add(trackerLabel);
		rightPanel.add(trackerBox);
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(onGrid);
		radioGroup.add(offGrid);
		rightPanel.add(new JLabel("Installation Type:"));
		Box boxRadio = Box.createHorizontalBox();
		boxRadio.add(onGrid);
		boxRadio.add(offGrid);
		rightPanel.add(boxRadio);
		rightPanel.add(new JLabel("Battery Type (Off Grid Only):"));
		rightPanel.add(batteryBox);
		rightPanel.setBorder(new EmptyBorder(10, 10, 10, 0));
		panel2.add(rightPanel);


		/***********  Add 3rd GUI panel = 'Save New Installations' + "Clear Fields" buttons  ***********/

		panel3.add(newButton);
		panel3.add(clearFieldsButton);
		newButton.addActionListener(event -> saveNewInstallation());
		clearFieldsButton.addActionListener(event -> clearFormFields());
		add(panel3);


		/***********  Add 4th GUI panel = main ouput display TextArea  ***********/

		textArea.setEditable(false);
		textArea.setBorder(new EtchedBorder());
		JScrollPane scrollPane = new JScrollPane(textArea,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textArea.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		panel4.add(scrollPane);
		add(panel4);


		/***********  Add 5th GUI panel = all bottom panel buttons  ***********/

		panel5.add(searchButton);
		panel5.add(displayButton);
		panel5.add(clearButton);
		panel5.add(exitButton);
		searchButton.addActionListener(event -> displayByTechie());
		displayButton.addActionListener(event -> displayAll());
		clearButton.addActionListener(event -> clearTextArea());
		exitButton.addActionListener(event -> exit());
		JLabel studentName = new JLabel("  @George Price");
		panel5.add(studentName);
		add(panel5);


		/*********** Implementing improved close method ***********/

		//evoking exit() method and overriding the X icon on JFrame
		addWindowListener(
		new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					exit();
				}
			}
		);

	}//end SolarSystemGUI() method

}// end SolarSystemGUI class