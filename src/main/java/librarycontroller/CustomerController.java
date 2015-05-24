package librarycontroller;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import librarydao.CustomerDAO;
import librarymodel.Customer;

/**
 * Customer Controller class allows users to do CRUD operations
 * 
 * @author SvetaP
 */
@ManagedBean(name = "customerController")
@ViewScoped
public class CustomerController implements Serializable {
	private static final long serialVersionUID = -2636720412534826018L;

	// Loading customers list
	private List<Customer> customers;
	// Creating new customer
	private Customer customer = new Customer();
	// ID
	private BigInteger idcustomer, idspecimen;

	@ManagedProperty("#{customerDAO}")
	private CustomerDAO customerdao;

	/**
	 * Default constructor
	 */
	public CustomerController() {
	}

	/**
	 * Getters, Setters
	 * 
	 * @return
	 */

	public List<Customer> getCustomers() {
		return customers;
	}

	/**
	 *
	 * @return
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 *
	 * @param customer
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 *
	 * @return
	 */
	public BigInteger getIdcustomer() {
		return idcustomer;
	}

	/**
	 *
	 * @param idcustomer
	 */
	public void setIdcustomer(BigInteger idcustomer) {
		this.idcustomer = idcustomer;
	}

	/**
	 *
	 * @return
	 */
	public BigInteger getIdspecimen() {
		return idspecimen;
	}

	/**
	 *
	 * @param idspecimen
	 */
	public void setIdspecimen(BigInteger idspecimen) {
		this.idspecimen = idspecimen;
	}

	/**
	 *
	 * @return
	 */
	public CustomerDAO getCustomerdao() {
		return customerdao;
	}

	/**
	 *
	 * @param customerdao
	 */
	public void setCustomerdao(CustomerDAO customerdao) {
		this.customerdao = customerdao;
	}

	/**
	 * Initializing Data Access Object for Customer
	 * 
	 */
	@PostConstruct
	public void getAll() {
		customers = customerdao.getAll();
		Collections.sort(customers, new Comparator<Customer>() {
			@Override
			public int compare(Customer customer1, Customer customer2) {
				return customer1.getId().compareTo(customer2.getId());
			}
		});
	}

	/**
	 * Create operations
	 */
	public void add() {
		customerdao.add(customer);
	}

	/**
	 * CreateSpecimen operations
	 */
	public void addSpecimen() {
		customerdao.addSpecimen(idcustomer, idspecimen);
	}

	/**
	 * DeleteSpecimen operations
	 */
	public void deleteSpecimen() {
		customerdao.deleteSpecimen(idcustomer, idspecimen);
	}

	/**
	 * Delete operations
	 */
	public void delete() {
		customerdao.delete(idcustomer);
	}

	/**
	 * Update operations
	 */
	public void update() {
		customerdao.update(customer, idcustomer);
	}

}
