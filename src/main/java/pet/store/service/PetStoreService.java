package pet.store.service;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.dao.PetStoreDao;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	@Autowired
	private PetStoreDao petStoreDao;
	
	private PetStore petStore;

	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		
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

	
	private PetStore findOrCreatePetStore(Long petStoreId) {
		
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
	
	
}
