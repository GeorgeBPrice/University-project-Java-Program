/**
 *  COIT11134 Object Oriented Programming
 *  Assignment 2
 *  By George Price
 */

class Technician
{
	private String techName, techPhone;

	// Technician Constructor
	public Technician (String techName, String techPhone)
	{
		this.techName = techName;
		this.techPhone = techPhone;
	}

	// Technician name
	public String getTechName()
	{
		return techName;
	}

	// Technician phone
	public String getTechPhone()
	{
		return techPhone;
	}

    // String representation
    @Override
	public String toString()
	{
		return "\nName: " + techName
			+ "\nPhone: " + techPhone;
	}
}
