package com.example.informationservice.service;

import com.example.informationservice.dto.StaffDTO;
import com.example.informationservice.entity.Staff;
import com.example.informationservice.repository.StaffRepository;
import com.example.informationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    public List<StaffDTO> getAllStaff(){
        List<Staff> staffs = staffRepository.getListStaff();
        return staffs.stream().map(this::convertToStaffDTO).collect(Collectors.toList());
    }

    private StaffDTO convertToStaffDTO(Staff staff) {
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setAccountId(staff.getAccount() != null ? staff.getAccount().getAccountId() : null);
        staffDTO.setStatus(staff.getAccount() != null ? staff.getAccount().getStatus() : null);
        staffDTO.setFirstName(staff.getFirstName());
        staffDTO.setLastName(staff.getLastName());
        staffDTO.setEmail(staff.getEmail());
        staffDTO.setGender(staff.getGender());
        staffDTO.setPhoneNumber(staff.getPhoneNumber());
        staffDTO.setCreateAt(staff.getCreateAt());
        staffDTO.setAddress(staff.getAddress());
        staffDTO.setBirthday(staff.getBirthday());
        return staffDTO;
    }

    public Optional<Staff> getStaffByEmail(String email){
        return staffRepository.findByEmail(email);
    }

    public StaffDTO getInformationStaff(String userName){
        Staff staff = staffRepository.findByuserName(userName).isPresent() ? staffRepository.findByuserName(userName).get() : null;
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setAccountId(staff.getAccount() != null ? staff.getAccount().getAccountId() : null);
        staffDTO.setUserName(staff.getUserName());
        staffDTO.setFirstName(staff.getFirstName());
        staffDTO.setLastName(staff.getLastName());
        staffDTO.setEmail(staff.getEmail());
        staffDTO.setGender(staff.getGender());
        staffDTO.setPhoneNumber(staff.getPhoneNumber());
        staffDTO.setCreateAt(staff.getCreateAt());
        staffDTO.setAddress(staff.getAddress());
        staffDTO.setBirthday(staff.getBirthday());
        return staffDTO;
    }

    public void updateInformation(StaffDTO staffDTO){
        staffRepository.updateInformation(staffDTO.getUserName(), staffDTO.getFirstName(),
                                        staffDTO.getLastName(), staffDTO.getGender(), staffDTO.getBirthdayUpdate(), staffDTO.getPhoneNumber(), staffDTO.getAddress());
    }
}
