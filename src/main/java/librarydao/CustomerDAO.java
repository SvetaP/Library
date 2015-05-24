package librarydao;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;

import javax.faces.bean.*;

import org.apache.log4j.Logger;

import librarymodel.Customer;
import librarymodel.Specimen;

/**
 * Data Access Object All CRUD (create, read, update, delete) basic data access
 * operations
 * 
 * @author SvetaP
 */
@ManagedBean(name = "customerDAO")
@ApplicationScoped
public class CustomerDAO implements GenericDbDAO<Customer>, Serializable {
	private static final long serialVersionUID = -3274761237017793968L;
	private static final Logger log = Logger.getLogger(CustomerDAO.class);

	/**
	 * Stores an instance of the entity class in the database
	 * 
	 * @param entity
	 * @return
	 */
	public void add(Customer entity) {
		String SQL1 = "SELECT my_seq_customer.nextval FROM DUAL";
		String SQL2 = "INSERT INTO CUSTOMER (ID, NAME, PHONE) "
				+ "VALUES (?, ?, ?)";
		String SQL3 = "SELECT ID FROM CUSTOMER WHERE NAME = '"
				+ entity.getName() + "' AND PHONE = " + entity.getNumber();
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();
				PreparedStatement pst = con.prepareStatement(SQL2);) {
			ResultSet rs = st.executeQuery(SQL3);
			if (!rs.next()) {
				rs = st.executeQuery(SQL1);
				while (rs.next()) {
					BigInteger b = BigInteger.valueOf(rs.getLong("NEXTVAL"));
					entity.setId(b);
				}
				con.setAutoCommit(false);
				pst.setLong(1, entity.getId().longValue());
				pst.setString(2, entity.getName());
				if (entity.getNumber() != null) {
					pst.setLong(3, entity.getNumber());
				} else
					pst.setString(3, "''");
				pst.executeUpdate();
				con.commit();
				log.info("Customer created");
			} else
				log.info("Customer already exists");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Removes the record that is associated with the entity instance
	 * 
	 * @param ID
	 * 
	 */
	public void delete(BigInteger ID) {
		String SQL1 = "SELECT IDSPECIMEN FROM ISSUEBOOK WHERE IDCUSTOMER ="
				+ ID;
		String SQL2 = "DELETE FROM CUSTOMER WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st1 = con.createStatement();
				Statement st2 = con.createStatement();) {
			ResultSet rs = st1.executeQuery(SQL1);
			if (!rs.next()) {
				st2.executeUpdate(SQL2);
				log.info("Customer deleted");
			} else
				log.info("Customer didn't return specimen");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

	}

	/**
	 * Updates the entity instance by ID
	 * 
	 * @param entity
	 * @param ID
	 * 
	 */
	public void update(Customer entity, BigInteger ID) {
		String SQL1 = "UPDATE CUSTOMER SET NAME = ?, PHONE = ? WHERE ID =" + ID;
		String SQL2 = "SELECT NAME FROM CUSTOMER WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();
				PreparedStatement pst = con.prepareStatement(SQL1);) {
			ResultSet rs = st.executeQuery(SQL2);
			if (rs.next()) {
				con.setAutoCommit(false);
				pst.setString(1, entity.getName());
				pst.setLong(2, entity.getNumber());
				pst.executeUpdate();
				con.commit();
				log.info("Customer updated");
			} else
				log.info("Customer does not exist");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

	}

	/**
	 * Retrieves an entity instances that was previously persisted to the
	 * database
	 * 
	 * @return List<Customer>
	 */
	public List<Customer> getAll() {
		List<Customer> customers = new ArrayList<Customer>();
		SpecimenDAO specimendao = new SpecimenDAO();
		String SQL1 = "SELECT * FROM CUSTOMER";
		String SQL2 = "SELECT IDSPECIMEN FROM ISSUEBOOK WHERE IDCUSTOMER = ?";
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL2);
				Statement st = con.createStatement();) {
			ResultSet rs1 = st.executeQuery(SQL1);
			while (rs1.next()) {
				BigInteger idcustomer = BigInteger.valueOf(rs1.getLong("ID"));
				String name = rs1.getString("NAME");
				Long number = rs1.getLong("PHONE");
				pst.setLong(1, idcustomer.longValue());
				ResultSet rs2 = pst.executeQuery();
				List<BigInteger> idspecimen = new ArrayList<BigInteger>();
				while (rs2.next()) {
					BigInteger id = BigInteger.valueOf(rs2
							.getLong("IDSPECIMEN"));
					idspecimen.add(id);
				}
				List<Specimen> specimens = new ArrayList<Specimen>();
				for (BigInteger id : idspecimen) {
					Specimen specimen = specimendao.getByID(id);
					specimen.setId(id);
					specimens.add(specimen);
				}
				Customer customer = new Customer(name, number, specimens);
				customer.setId(idcustomer);
				customers.add(customer);
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("All customers");
		return customers;
	}

	/**
	 * Returns the record by ID
	 * 
	 * @param ID
	 * 
	 * @return Customer
	 */
	public Customer getByID(BigInteger ID) {
		Customer customer = null;
		List<BigInteger> idspecimen = new ArrayList<BigInteger>();
		List<Specimen> specimens = null;
		SpecimenDAO specimendao = new SpecimenDAO();
		String SQL1 = "SELECT * FROM CUSTOMER WHERE ID = " + ID;
		String SQL2 = "SELECT IDSPECIMEN FROM ISSUEBOOK WHERE IDCUSTOMER = "
				+ ID;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			ResultSet rs1 = st.executeQuery(SQL1);
			while (rs1.next()) {
				String name = rs1.getString("NAME");
				Long number = rs1.getLong("PHONE");
				ResultSet rs2 = st.executeQuery(SQL2);
				if (!rs2.next()) {
					customer = new Customer(name, number);
					customer.setId(ID);
				} else {
					while (rs2.next()) {
						BigInteger id = BigInteger.valueOf(rs2
								.getLong("IDSPECIMEN"));
						idspecimen.add(id);
					}
					specimens = new ArrayList<Specimen>();
					for (BigInteger id : idspecimen) {
						Specimen specimen = specimendao.getByID(id);
						specimen.setId(id);
						specimens.add(specimen);
					}
					customer = new Customer(name, number, specimens);
					customer.setId(ID);
				}
				log.info("Customer by ID");
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return customer;
	}

	/**
	 * Stores an instance of the entity class the Specimen in the database to
	 * the list for the Customer
	 * 
	 * @param IDCustomer
	 * @param IDSpecimen
	 * @return
	 */
	public void addSpecimen(BigInteger IDCustomer, BigInteger IDSpecimen) {
		String SQL1 = "SELECT IDAVAILABILITY FROM SPECIMEN WHERE ID = "
				+ IDSpecimen;
		String SQL2 = "INSERT INTO ISSUEBOOK (IDCUSTOMER, IDSPECIMEN) "
				+ "VALUES (?, ?)";
		String SQL3 = "UPDATE SPECIMEN SET IDAVAILABILITY = 2 WHERE ID = "
				+ IDSpecimen;
		String SQL4 = "SELECT InventoryNumber FROM SPECIMEN WHERE ID = "
				+ IDSpecimen;
		String SQL5 = "SELECT NAME FROM CUSTOMER WHERE ID = " + IDCustomer;
		try (Connection con = Connect.connectionDb();
				Statement st1 = con.createStatement();
				Statement st2 = con.createStatement();
				Statement st3 = con.createStatement();
				PreparedStatement pst = con.prepareStatement(SQL2);) {
			ResultSet rs2 = st3.executeQuery(SQL5);
			if (rs2.next()) {
				rs2 = st3.executeQuery(SQL4);
				if (rs2.next()) {
					ResultSet rs1 = st1.executeQuery(SQL1);
					while (rs1.next()) {
						if (rs1.getInt("IDAVAILABILITY") == 1) {
							con.setAutoCommit(false);
							pst.setLong(1, IDCustomer.longValue());
							pst.setLong(2, IDSpecimen.longValue());
							st2.executeUpdate(SQL3);
							pst.executeUpdate();
							con.commit();
							log.info("Customer took the specimens");
						} else
							log.info("Specimen is NotAvailable");
					}
				} else
					log.info("Specimen exist");
			} else
				log.info("Customer exist");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

	}

	/**
	 * Delete an instance of the entity class the Specimen in the database from
	 * list for the Customer
	 * 
	 * @param IDCustomer
	 * @param IDSpecimen
	 * @return
	 */
	public void deleteSpecimen(BigInteger IDCustomer, BigInteger IDSpecimen) {
		String SQL1 = "DELETE FROM ISSUEBOOK WHERE IDSPECIMEN = " + IDSpecimen
				+ " AND IDCUSTOMER = " + IDCustomer;
		String SQL2 = "UPDATE SPECIMEN SET IDAVAILABILITY = 1 WHERE ID = "
				+ IDSpecimen;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			if (st.executeUpdate(SQL1) != 0) {
				st.executeUpdate(SQL2);
				log.info("Customer returned the specimens");
			} else
				log.info("Customer hadn't specimen");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

	}

}
