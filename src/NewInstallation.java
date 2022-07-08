/**
 *  COIT11134 Object Oriented Programming
 *  Assignment 2
 *  By George Price
 */

// adds technician to new Installations, and writeFile to Installations.txt
class NewInstallation extends SolarSystemDesign
{
	private String techName, techPhone;

	public NewInstallation (String customerName, String customerPhone, String address,
		String installNumber, String startDate, String finishedDate, String watts, String tracker,
			String grid, String battery, String techName, String techPhone)
	{
		super(customerName, customerPhone, address, installNumber, startDate, finishedDate,
			watts, tracker, grid, battery);
		this.techName = techName;
		this.techPhone = techPhone;
	}

	//adding technician getters
	public String getTechName()
	{
		return techName;
	}
	public String getTechPhone()
	{
		return techPhone;
	}

	//appends to superclass toString()
	public String toString()
	{
		String data = String.format("%-28s%-28s", techName, techPhone);

		return ""+super.toString() + "\nTECHNICIAN DETAILS\n" + data;
	}
}
