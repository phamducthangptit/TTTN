package com.example.productservice.service;

import com.example.productservice.dto.AddressRequestDTO;
import com.example.productservice.dto.AddressResponseDTO;
import com.example.productservice.entity.Address;
import com.example.productservice.entity.User;
import com.example.productservice.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public List<AddressResponseDTO> getAllAddressByUserName(String userName) {
        return addressRepository.getAllAddressByUserName(userName).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public AddressResponseDTO convertToDTO(Address address) {
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO();
        addressResponseDTO.setAddressId(address.getId());
        addressResponseDTO.setHouseNumber(address.getHouseNumber());
        addressResponseDTO.setProvinceCode(address.getProvince());
        addressResponseDTO.setDistrictCode(address.getDistrict());
        addressResponseDTO.setWardCode(address.getWards());
        return addressResponseDTO;
    }

    public void saveNewAddress(AddressRequestDTO addressRequestDTO) {
        Address address = new Address();
        // check exists add
        if(!addressRepository.existsAddress(addressRequestDTO.getUserId(), addressRequestDTO.getProvinceCode(), addressRequestDTO.getDistrictCode(), addressRequestDTO.getWardCode(), addressRequestDTO.getHouseNumber())) {
            User user = new User();
            user.setUserId(addressRequestDTO.getUserId());
            address.setUser(user);
            address.setHouseNumber(addressRequestDTO.getHouseNumber());
            address.setProvince(addressRequestDTO.getProvinceCode());
            address.setDistrict(addressRequestDTO.getDistrictCode());
            address.setWards(addressRequestDTO.getWardCode());
            addressRepository.save(address);
        }
    }

    public void deleteAddress(int addressId) {
        addressRepository.deleteById(addressId);
    }
}
