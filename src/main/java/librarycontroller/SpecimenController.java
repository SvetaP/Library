package librarycontroller;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import librarydao.SpecimenDAO;
import librarymodel.Specimen;

/**
 * Specimen Controller class allows users to do CRUD operations
 * 
 * @author SvetaP
 */
@ManagedBean(name = "specimenController")
@ViewScoped
public class SpecimenController implements Serializable {
	private static final long serialVersionUID = 3121754148257014598L;

	// Loading specimens list
	private List<Specimen> specimens;
	// Creating new specimen
	private Specimen specimen = new Specimen();
	// ID
	private BigInteger id;

	@ManagedProperty("#{specimenDAO}")
	private SpecimenDAO specimendao;

	/**
	 * Default constructor
	 */
	public SpecimenController() {
	}

	/**
	 * Getters, Setters
	 * 
	 * @return
	 */

	public List<Specimen> getSpecimens() {
		return specimens;
	}

	/**
	 *
	 * @return
	 */
	public Specimen getSpecimen() {
		return specimen;
	}

	/**
	 *
	 * @param specimen
	 */
	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
	}

	/**
	 *
	 * @return
	 */
	public BigInteger getId() {
		return id;
	}

	/**
	 *
	 * @param id
	 */
	public void setId(BigInteger id) {
		this.id = id;
	}

	/**
	 *
	 * @return
	 */
	public SpecimenDAO getSpecimendao() {
		return specimendao;
	}

	/**
	 *
	 * @param specimendao
	 */
	public void setSpecimendao(SpecimenDAO specimendao) {
		this.specimendao = specimendao;
	}

	/**
	 * Initializing Data Access Object for Specimen
	 * 
	 */
	@PostConstruct
	public void getAll() {
		specimens = specimendao.getAll();
		Collections.sort(specimens, new Comparator<Specimen>() {
			@Override
			public int compare(Specimen specimen1, Specimen specimen2) {
				return specimen1.getId().compareTo(specimen2.getId());
			}
		});
	}

	/**
	 * Create operations
	 */
	public void add() {
		specimendao.add(specimen, id);
	}

	/**
	 * Delete operations
	 */
	public void delete() {
		specimendao.delete(id);
	}

	/**
	 * Update operations
	 */
	public void update() {
		specimendao.update(specimen, id);
	}

}
