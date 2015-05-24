package librarycontroller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import librarydao.AvailabilityDAO;

/**
 * Availability Controller class allows users to do CRUD operations
 * 
 * @author SvetaP
 */
@ManagedBean(name = "availabilityController")
@ViewScoped
public class AvailabilityController implements Serializable {
	private static final long serialVersionUID = -7260548441276971456L;

	// Loading availability list
	private List<String> listavailability;

	@ManagedProperty("#{availabilityDAO}")
	private AvailabilityDAO availabilitydao;

	/**
	 * Default constructor
	 */
	public AvailabilityController() {
	}

	/**
	 * Getters, Setters
	 * 
	 * @return
	 */

	public List<String> getListavailability() {
		return listavailability;
	}

	/**
	 *
	 * @param listavailability
	 */
	public void setListavailability(List<String> listavailability) {
		this.listavailability = listavailability;
	}

	/**
	 *
	 * @return
	 */
	public AvailabilityDAO getAvailabilitydao() {
		return availabilitydao;
	}

	/**
	 *
	 * @param availabilitydao
	 */
	public void setAvailabilitydao(AvailabilityDAO availabilitydao) {
		this.availabilitydao = availabilitydao;
	}

	/**
	 * Create operations
	 */
	public void add() {
		availabilitydao.add();
	}

	/**
	 * Initializing Data Access Object for Availability
	 * 
	 */
	@PostConstruct
	public void getAll() {
		listavailability = availabilitydao.getAll();
	}

}
