package librarydao;

import java.math.BigInteger;
import java.sql.*;
import java.util.*;

import javax.faces.bean.*;

import org.apache.log4j.Logger;

import librarymodel.Availability;
import librarymodel.Customer;
import librarymodel.Specimen;

@ManagedBean(name = "customerDAO")
@ApplicationScoped
public class CustomerDAO implements GenericDbDAO<Customer> {
	private static final Logger log = Logger.getLogger(AuthorsDAO.class);

	public void add(Customer entity) {
		String SQL1 = "SELECT my_seq_customer.nextval FROM DUAL";
		String SQL2 = "INSERT INTO CUSTOMER (ID, NAME, NUMBER) "
				+ "VALUES (?, ?, ?)";
		String SQL3 = "INSERT INTO ISSUEBOOK (IDCUSTOMER, IDSPECIMEN) "
				+ "VALUES (?, ?)";
		String SQL4 = "UPDATE SPECIMEN SET IDAVAILABILITY = 2 WHERE ID = ?";
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			ResultSet rs = st.executeQuery(SQL1);
			while (rs.next()) {
				BigInteger b = BigInteger.valueOf(rs.getLong("NEXTVAL"));
				entity.setId(b);
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL2);) {
			con.setAutoCommit(false);
			pst.setLong(1, entity.getId().longValue());
			pst.setString(2, entity.getName());
			pst.setInt(3, entity.getNumber());
			pst.executeUpdate();
			con.commit();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst1 = con.prepareStatement(SQL3);
				PreparedStatement pst2 = con.prepareStatement(SQL4);) {
			for (int i = 0; i < entity.getSpecimens().size(); i++) {
				con.setAutoCommit(false);
				pst1.setLong(1, entity.getId().longValue());
				if (entity.getSpecimens().get(i).getAvailability() == Availability.AVAILABLE) {
					pst1.setLong(2, entity.getSpecimens().get(i).getId()
							.longValue());
					pst2.setLong(1, entity.getSpecimens().get(i).getId()
							.longValue());
				} else
					log.info("Specimen is NotAvailable");
				pst1.executeUpdate();
				pst2.executeUpdate();
				con.commit();
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Customer created");

	}

	public void delete(BigInteger ID) {
		String SQL1 = "SELECT IDSPECIMEN FROM ISSUEBOOK WHERE IDCUSTOMER ="
				+ ID;
		String SQL2 = "DELETE FROM CUSTOMER WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st1 = con.createStatement();
				Statement st2 = con.createStatement();) {
			ResultSet rs = st1.executeQuery(SQL1);
			if (rs == null) {
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

	public void update(Customer entity, BigInteger ID) {
		String SQL = "UPDATE CUSTOMER SET NAME = ? AND NUMBER = ? WHERE ID ="
				+ ID;
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL);) {
			con.setAutoCommit(false);
			pst.setString(1, entity.getName());
			pst.setInt(2, entity.getNumber());
			pst.executeUpdate(SQL);
			con.commit();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Customer updated");

	}

	public List<Customer> getAll() {
		List<Customer> customers = new ArrayList<Customer>();
		List<BigInteger> idspecimen = new ArrayList<BigInteger>();
		List<Specimen> specimen = null;
		SpecimenDAO specimendao = new SpecimenDAO();
		String SQL1 = "SELECT ID, NAME, NUMBER FROM CUSTOMER";
		String SQL2 = "SELECT IDSPECIMEN FROM ISSUEBOOK WHERE IDCUSTOMER = ?";
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();
				PreparedStatement pst = con.prepareStatement(SQL2);) {
			ResultSet rs1 = st.executeQuery(SQL1);
			while (rs1.next()) {
				BigInteger idcustomer = BigInteger.valueOf(rs1.getLong("ID"));
				String name = rs1.getString("NAME");
				int number = rs1.getInt("NUMBER");
				pst.setLong(1, idcustomer.longValue());
				ResultSet rs2 = pst.executeQuery();
				while (rs2.next()) {
					BigInteger id = BigInteger.valueOf(rs2
							.getLong("IDSPECIMEN"));
					idspecimen.add(id);
				}
				specimen = new ArrayList<Specimen>();
				for (BigInteger id : idspecimen) {
					specimen.add(specimendao.getByID(id));
				}
				Customer customer = new Customer(name, number, specimen);
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

	public Customer getByID(BigInteger ID) {
		Customer customer = null;
		List<BigInteger> idspecimen = new ArrayList<BigInteger>();
		List<Specimen> specimen = null;
		SpecimenDAO specimendao = new SpecimenDAO();
		String SQL1 = "SELECT NAME, NUMBER FROM SPECIMEN WHERE ID=" + ID;
		String SQL2 = "SELECT IDSPECIMEN FROM ISSUEBOOK WHERE IDCUSTOMER ="
				+ ID;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();
				PreparedStatement pst = con.prepareStatement(SQL2);) {
			ResultSet rs1 = st.executeQuery(SQL1);
			while (rs1.next()) {
				String name = rs1.getString("NAME");
				int number = rs1.getInt("NUMBER");
				pst.setString(1, name);
				pst.setInt(2, number);
				ResultSet rs2 = pst.executeQuery();
				while (rs2.next()) {
					BigInteger id = BigInteger.valueOf(rs2
							.getLong("IDSPECIMEN"));
					idspecimen.add(id);
				}
				specimen = new ArrayList<Specimen>();
				for (BigInteger id : idspecimen) {
					specimen.add(specimendao.getByID(id));
				}
				customer = new Customer(name, number, specimen);
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Customer by ID");
		return customer;
	}

	public void addSpecimen(List<Specimen> specimens, BigInteger ID) {
		String SQL1 = "INSERT INTO ISSUEBOOK (IDCUSTOMER, IDSPECIMEN) "
				+ "VALUES (?, ?)";
		String SQL2 = "UPDATE SPECIMEN SET IDAVAILABILITY = 2 WHERE ID = ?";
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst1 = con.prepareStatement(SQL1);
				PreparedStatement pst2 = con.prepareStatement(SQL2);) {
			for (int i = 0; i < specimens.size(); i++) {
				con.setAutoCommit(false);
				pst1.setLong(1, ID.longValue());
				if (specimens.get(i).getAvailability() == Availability.AVAILABLE) {
					pst1.setLong(2, specimens.get(i).getId().longValue());
					pst2.setLong(1, specimens.get(i).getId().longValue());
					log.info("Customer took the specimens");
				} else
					log.info("Specimen is NotAvailable");
				pst1.executeUpdate();
				pst2.executeUpdate();
				con.commit();
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

	}

	public void deleteSpecimen(List<Specimen> specimens, BigInteger ID) {
		String SQL1 = "DELETE FROM ISSUEBOOK WHERE IDSPECIMEN = ? AND IDCUSTOMER ="
				+ ID;
		String SQL2 = "UPDATE SPECIMEN SET IDAVAILABILITY = 1 WHERE ID = ?";
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst1 = con.prepareStatement(SQL1);
				PreparedStatement pst2 = con.prepareStatement(SQL2);) {
			for (int i = 0; i < specimens.size(); i++) {
				con.setAutoCommit(false);
				pst1.setLong(1, specimens.get(i).getId().longValue());
				if (pst1.executeUpdate() != 0) {
					pst2.setLong(1, specimens.get(i).getId().longValue());
					log.info("Customer returned the specimens");
				} else
					log.info("Customer hadn't specimen");
				pst2.executeUpdate();
				con.commit();
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

	}

}
