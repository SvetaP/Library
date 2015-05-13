package librarycontroller;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import librarydao.AuthorsDAO;
import librarymodel.Authors;

@ManagedBean(name = "authorsController")
@ViewScoped
public class AuthorsController implements GenericController<Authors> {
	
	private List<Authors> authors;

	@ManagedProperty("#authorsDAO}")
	private AuthorsDAO authorsdao;

	public AuthorsController() {
	}

	public void setAuthorsDAO(AuthorsDAO authorsdao) {
		this.authorsdao = authorsdao;
	}

	/*public List<Authors> getALL() {
		return authorsdao.getAll();
	}*/
	
	@PostConstruct
	public void getAll(){
		authors = authorsdao.getAll();
	}

	public Authors getById(BigInteger ID) {
		return authorsdao.getByID(ID);
	}

	public void delete(BigInteger ID) {
		authorsdao.delete(ID);
	}

	public void update(Authors entity, BigInteger ID) {
		authorsdao.update(entity, ID);
	}

}
