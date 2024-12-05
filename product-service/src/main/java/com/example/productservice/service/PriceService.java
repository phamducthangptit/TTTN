package com.example.productservice.service;

import com.example.productservice.dto.PriceDTO;
import com.example.productservice.dto.PriceDetailDTO;
import com.example.productservice.dto.PriceUpdateRequest;
import com.example.productservice.entity.Price;
import com.example.productservice.entity.PriceDetail;
import com.example.productservice.repository.PriceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceService {
    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private PriceDetailService priceDetailService;

    public PriceDTO savePrice(String name) {
        Price price = new Price();
        price.setName(name);
        return convertToDTO(priceRepository.save(price));
    }

    public int getPriceIdByPriceName(String priceName) {
        return priceRepository.getPriceId(priceName);
    }

    public List<PriceDTO> getAllPrice(){
        return priceRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private PriceDTO convertToDTO(Price price) {
        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setPriceId(price.getId());
        priceDTO.setPriceName(price.getName());
        return priceDTO;
    }

    public void updatePriceProduct(PriceUpdateRequest priceUpdateRequest) {
        int priceIdNew = priceUpdateRequest.getPriceIdNew();
        int priceIdOld = priceUpdateRequest.getPriceIdOld();
        if(priceIdNew == priceIdOld) { // trường hợp thayddoi gia, ngày bd, ngay ket thuc
            // update price detail
            priceDetailService.updatePriceDetail(priceUpdateRequest);
        } else { // trường hợp thay đổi tên giá
            // check xem tên giá cũ đã có enddate chưa, nếu chưa thì cập nhật enddate, có rồi thì thêm pricedetail mới
            PriceDetail priceDetail = priceDetailService.getPriceDetailByProductIdAndPriceId(priceUpdateRequest.getProductId(), priceUpdateRequest.getPriceIdOld());
            if(priceDetail.getEndAt() == null) { // enddate chua co
                priceDetailService.updateEndDate(priceUpdateRequest.getProductId(), priceUpdateRequest.getPriceIdOld(), priceUpdateRequest.getStartDate());
            }
            // them price detail moi
            PriceDetailDTO priceDetailDTO = new PriceDetailDTO();
            priceDetailDTO.setProductId(priceUpdateRequest.getProductId());
            priceDetailDTO.setPriceId(priceIdNew);
            priceDetailDTO.setPrice(priceUpdateRequest.getPriceAsBigDecimal());
            priceDetailDTO.setStartAt(priceUpdateRequest.getStartDate());
            priceDetailDTO.setEndAt(priceUpdateRequest.getEndDate());
            priceDetailService.savePriceDetail(priceDetailDTO);
        }
    }
}
