/**
 *  COIT11134 Object Oriented Programming
 *  Assignment 2
 *  By George Price
 */

class Building
{
	private String customerName, customerPhone, address;

	public Building (String customerName, String customerPhone, String address)
	{
		this.customerName = customerName;
		this.customerPhone = customerPhone;
		this.address = address;
	}
	public String getCustomerName()
	{
		return customerName;
	}
	public String getCustomerPhone()
	{
		return customerPhone;
	}
	public String getAddress()
	{
		return address;
	}

	//toString to format data for print
	public String toString()
	{
		String header = String.format("%-23s%-24s%-50s\n", "Customer Name:", "Phone Number:", "Installation Address:");
		String data = String.format("%-25s%-26s%-50s\n", customerName, customerPhone, address);

		return "BUILDING DETAILS\n" + header + data;
	}
}