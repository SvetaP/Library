package librarync;

import java.util.*;

public class CustomerDAO {
	private static List<Customer> customers;
	{
		customers = new ArrayList<Customer>();
	}
	
	public static void addCustomer (Customer customer) {
		for(int i=0; i< customers.size(); i++)
		{
			if(customers.get(i).equals(customer) == false)
			{
				if(i == (customers.size()-1))
				{
					customers.add(customer);
				}
				i++;
			}
			System.out.println("No");
		}
	}
	
	public static String getCustomers () {
		String str = null;
		for(int i=0; i< customers.size(); i++)
		{
			str = str + customers.get(i).toString() + "\t";
		}
		return str;
	}
	
	public static void updateCustomer (Customer customer, int number) {
		for(int i=0; i< customers.size(); i++)
		{
			if(customers.get(i).equals(customer) == true)
			{
				customers.get(i).setNumber(number);
			}
			System.out.println("No");
		}
	}
	
	public static void deleteCustomer (Customer customer) {
		for(int i=0; i< customers.size(); i++)
		{
			if(customers.get(i).equals(customer) == true)
			{
				if(customer.getSpecimens() == null){
					customers.remove(customers.get(i));
				}
				System.out.println("Return specimens");
			}
			System.out.println("No");
		}
	}
	
	public void returnSpecimens(Customer customer, List<Specimen> specimens) {
		for(int i=0; i< customers.size(); i++)
		{
			if(customers.get(i).equals(customer) == true)
			{
				for(Specimen s : specimens){
					for(int j=0; j<customer.getSpecimens().size(); j++){
						if(customer.getSpecimens().get(j).equals(s) == true){
							customer.getSpecimens().remove(j);
							s.setAvailable();
						}
					}
				}
			}
		}
		
	}
}
