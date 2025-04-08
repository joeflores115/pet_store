package pet.store.entity;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;
	private String customerFirstName;
	private String customerLastName;
	private String customerEmail;
	
	
	@EqualsAndHashCode.Exclude
    @ToString.Exclude
	@ManyToMany(mappedBy = "customers", cascade = CascadeType.PERSIST)
	private Set<PetStore> petStores = new HashSet<>();


	//this methods is used to add a pet store to the customer
	//and also add the customer to the pet store
	//this is a many to many relationship
	//so we need to add the customer to the pet store
	//and the pet store to the customer
	public void addPetStore(PetStore petStore) {
		this.petStores.add(petStore);
		petStore.getCustomers().add(this);
		
	}

}
