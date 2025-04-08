package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	@Autowired
	private PetStoreDao petStoreDao;
	
	private PetStore petStore;

	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) 
	{
		
		PetStore petStore=findOrCreatePetStore(petStoreData.getPetStoreId());
		
		copyPetStoreFields(petStore, petStoreData);
		
		PetStore dbPetStore=petStoreDao.save(petStore);
		
		return new PetStoreData(dbPetStore);
	}

	@Transactional(readOnly = false)
	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
		
	}

	
	private PetStore findOrCreatePetStore(Long petStoreId) 
	{
		
		if(Objects.isNull(petStoreId))
		{
			return new PetStore();
		}
		else 
		{
			return findPetStoreById(petStoreId);
			
		}
		
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId).orElseThrow(
				()->new NoSuchElementException("Pet Store with id=" 
		+ petStoreId + " does not exist."));			
	}
	
	

	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId,PetStoreEmployee petStoreEmployee) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long employeeId = petStoreEmployee.getEmployeeId();
		
		Employee employee = findOrCreateEmployee(employeeId, petStoreId);
		
		copyEmployeeFields(employee, petStoreEmployee);
		
		//set pet store for employee
		employee.setPetStore(petStore);
		
		//add employee to pet store list of employees
		petStore.getEmployees().add(employee);
		
		//dbEmployee save the employee (employeeDao.save)
		
		Employee dbEmployee = employeeDao.save(employee);
		//needs to return a petStoreEmployee object
		
		return new PetStoreEmployee(dbEmployee);
		
	}
	
	private Employee findOrCreateEmployee(Long employeeId,Long petStoreId)
	{
		if(Objects.isNull(employeeId))
		{
			return new Employee();
		}
		else 
		{
			return findEmployeeById(employeeId, petStoreId);
			
		}
	}

	private Employee findEmployeeById(Long employeeId, Long petStoreId) 
	{
		return employeeDao.findById(employeeId).orElseThrow(
				()->new IllegalArgumentException("Employee with id=" 
		+ employeeId + " does not exist."));			
	}
	
	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) 
	{
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
	}

	
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		
		Customer customer = findOrCreateCustomer(customerId, petStoreId);
		
		copyCustomerFields(customer, petStoreCustomer);
		
		//set pet store for employee
		customer.addPetStore(petStore);
		
		//add employee to pet store list of employees
		petStore.getCustomers().add(customer);
		
		//dbEmployee save the employee (employeeDao.save)
		
		Customer dbCustomer = customerDao.save(customer);
		//needs to return a petStoreEmployee object
		
		return new PetStoreCustomer(dbCustomer);
	}

	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
		
	}

	private Customer findOrCreateCustomer(Long customerId, Long petStoreId) {
		if(Objects.isNull(customerId))
		{
			return new Customer();
		}
		else 
		{
			return findCustomerById(customerId, petStoreId);
			
		}
	}

	private Customer findCustomerById(Long customerId, Long petStoreId) {
		//retrieving customer by id
		Customer customer = customerDao.findById(customerId).orElseThrow(
				()->new IllegalArgumentException("Customer with id=" + 
						customerId + " does not exist."));
		//checking to see if the customer belongs to the passed pet store id
		boolean petStoreExists = customer.getPetStores().stream()
				.anyMatch(petStore -> petStore.getPetStoreId().equals(petStoreId));
		
		if(!petStoreExists)
		{
			throw new IllegalArgumentException("Customer with id=" + 
					customerId + " does not belong to pet store with id=" + petStoreId);
		}
		
		return customer;
		
	}
	@Transactional
	public List<PetStoreData> retrieveAllPetStores() {
		//retrieving all pet stores from dao
		List<PetStore> petStores = petStoreDao.findAll();

		List<PetStoreData> result= new LinkedList<>();
		
		
		for(PetStore petStore : petStores)
		{
			PetStoreData psd = new PetStoreData(petStore);
			
			psd.getCustomers().clear();
			psd.getEmployees().clear();
			result.add(psd);
		}
		return result;
	}

	@Transactional
	public PetStoreData retrievePetStore(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		PetStoreData petStoreData = new PetStoreData(petStore);
		return petStoreData;
	}

	public void deletePetStoreById(Long petStoreId) {
		PetStore petstore=findPetStoreById(petStoreId);
		petStoreDao.delete(petstore);
	}
	
	
}
