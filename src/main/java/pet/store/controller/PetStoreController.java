package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/petstore")
@Slf4j
public class PetStoreController {
	@Autowired
	private PetStoreService petStoreService;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStore(@RequestBody PetStoreData petStoreData) 
	{
		log.info("Creating pet store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PutMapping("/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId, 
			@RequestBody PetStoreData petStoreData)
	{
		log.info("Updating pet store {}", petStoreData);
		petStoreData.setPetStoreId(petStoreId);
		return petStoreService.savePetStore(petStoreData);
	}
	
	
	//"/petstore" is not needed in the path because it is already in the class level
	//@PathVariable is used to pass the petStoreId from the URI to the method
	//The @RequestBody annotation for the petStoreEmployee object is used to pass the json data
	@PostMapping("/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee insertEmployee(@PathVariable Long petStoreId, 
			@RequestBody PetStoreEmployee petStoreEmployee)
	{
		log.info("Creating pet store employee {}", petStoreEmployee);
		
		return petStoreService.saveEmployee(petStoreId ,petStoreEmployee);
	}
	
	
	@PostMapping("/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer insertCustomer(@PathVariable Long petStoreId, 
			@RequestBody PetStoreCustomer petStoreCustomer)
	{
		log.info("Creating pet store customer {}", petStoreCustomer);
		
		return petStoreService.saveCustomer(petStoreId ,petStoreCustomer);
	}
	
	
	@GetMapping
	public List<PetStoreData> retrieveAllPetStores()
	{	//	@RequestBody is not needed because we are not passing any data
		log.info("Retrieving all pet stores");
		return petStoreService.retrieveAllPetStores();
	}
	
	@GetMapping("/{petStoreId}")
	public PetStoreData retrievePetStore(@PathVariable Long petStoreId)
	{
		log.info("Retrieving pet store {}", petStoreId);
		return petStoreService.retrievePetStore(petStoreId);
	}
	
	@DeleteMapping("/{petStoreId}")
	public Map<String,String> deletePetStoreById(@PathVariable Long petStoreId)
	{
		log.info("Deleting pet store {}", petStoreId);
		petStoreService.deletePetStoreById(petStoreId);
		return Map.of("message", "Pet store with id=" + petStoreId + " deleted successfully");
	}
	
	
}
