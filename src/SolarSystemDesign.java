/**
 *  COIT11134 Object Oriented Programming
 *  Assignment 2
 *  By George Price
 */

class SolarSystemDesign extends Building
{
	private String installNumber, startDate, finishedDate, watts, tracker, grid, battery;

	public SolarSystemDesign (String customerName, String customerPhone, String address,
		String installNumber, String startDate,String finishedDate, String watts, String tracker,
			String grid, String battery)
	{
		super(customerName, customerPhone, address);
		this.installNumber = installNumber;
		this.startDate = startDate;
		this.finishedDate = finishedDate;
		this.watts = watts;
		this.tracker = tracker;
		this.grid = grid;
		this.battery = battery;
	}

	//adding getters
	public String getInstallNumber()
	{
		return installNumber;
	}
	public String getStartDate ()
	{
		return startDate;
	}
	public String getFinishedDate ()
	{
		return finishedDate;
	}
	public String getWatts ()
	{
		return watts;
	}
	public String getTracker ()
	{
		return tracker;
	}
	public String getGrid ()
	{
		return grid;
	}
	public String getBattery ()
	{
		return battery;
	}

	//appends to superclass toString()
	public String toString()
	{
		String header1 = String.format("%-30s%-29s%-30s\n", "Start Date:", "End Date:", "Watts(WH) required:");
		String header2 = String.format("%-27s%-28s%-30s\n", "Solar Tracker:", "Solar Setup:", "Off Grid Battery:");
		String data1 = String.format("%-28s%-28s%-30s\n", startDate, finishedDate, watts);
		String data2 = String.format("%-30s%-30s%-30s\n", tracker, grid, battery);

		return ""+super.toString() + "\nINSTALLATION DETAILS\n"
								   + header1 + data1 + "\n"
								   + header2 + data2;
	}
}
