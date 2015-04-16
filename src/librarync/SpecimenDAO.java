package librarync;

import java.util.*;

public class SpecimenDAO {
	private static List<Specimen> specimens;
	{
		specimens = new ArrayList<Specimen>();
	}
	
	public static void addSpecimen (Specimen specimen) {
		for(int i=0; i< specimens.size(); i++)
		{
			if(specimens.get(i).equals(specimen) == false)
			{
				if(i == (specimens.size()-1))
				{
					specimens.add(specimen);
				}
				i++;
			}
			System.out.println("No");
		}
	}
	
	public static String getSpecimens () {
		String str = null;
		for(int i=0; i< specimens.size(); i++)
		{
			str = str + specimens.get(i).toString() + "\t";
		}
		return str;
	}
	
	public static void updateSpecimen (Specimen specimen, String inventoryNumber) {
		for(int i=0; i< specimens.size(); i++)
		{
			if(specimens.get(i).equals(specimen) == true)
			{
				specimens.get(i).setInventoryNumber(inventoryNumber);
			}
			System.out.println("No");
		}
	}
	
	public static void deleteSpecimen (Specimen specimen) {
		for(int i=0; i< specimens.size(); i++)
		{
			if(specimens.get(i).equals(specimen) == true)
			{
				specimens.remove(specimens.get(i));
			}
			System.out.println("No");
		}
	}
}
