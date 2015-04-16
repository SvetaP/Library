package librarync;

public class Specimen {
	private String inventoryNumber;
	private Book book;
	private Availability availability; 
	
	public void setInventoryNumber (String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}
	
	public void setBook (Book book) {
		this.book = book;
	}
	
	public void setAvailable() {
		this.availability = Availability.AVAILABLE;
	}
	
	public void setNotAvailable() {
		this.availability = Availability.NOTAVAILABLE;
	}
	
	public String getInventoryNumber () {
		return this.inventoryNumber;
	}
	
	public Book getBook () {
		return this.book;
	}
	
	public Availability getAvailability () {
		return this.availability;
	}
	
	public Specimen () {
		super();
	}
	
	public Specimen (String inventoryNumber, Book book) {
		this.inventoryNumber = inventoryNumber;
		this.book = book;
		this.availability = Availability.AVAILABLE;
	}
	
	public String toString () {
		return this.inventoryNumber + " " + book.toString() + " " + availability.toString();
	}
	
	public int hashCode () {
		return (int) (((inventoryNumber == null) ? 0 : inventoryNumber.hashCode()) + book.hashCode() +  availability.hashCode());
	}
	
	public boolean equals (Specimen specimen, Object o) {
		if (o != null && (o instanceof Specimen) && o.hashCode() == hashCode()) {
			return ((Specimen) o).inventoryNumber.equals(this.inventoryNumber) && ((Specimen) o).book.equals(this.book) && ((Specimen) o).availability.equals(this.availability);
		}
		else {
			return false;
		}
	}
}
