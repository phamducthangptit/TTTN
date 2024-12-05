package com.example.productservice.service;

import com.example.productservice.dto.PriceDetailDTO;
import com.example.productservice.dto.PriceUpdateRequest;
import com.example.productservice.entity.Price;
import com.example.productservice.entity.PriceDetail;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.PriceDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PriceDetailService {
    @Autowired
    private PriceDetailRepository priceDetailRepository;

    public PriceDetail savePriceDetail(PriceDetailDTO priceDetailDTO) {
        PriceDetail priceDetail = new PriceDetail();
        Price price = new Price();
        price.setId(priceDetailDTO.getPriceId());
        Product product = new Product();
        product.setProductId(priceDetailDTO.getProductId());
        priceDetail.setPrice(price);
        priceDetail.setProduct(product);
        priceDetail.setPrice1(priceDetailDTO.getPrice());
        priceDetail.setStartAt(priceDetailDTO.getStartAt());
        priceDetail.setEndAt(priceDetailDTO.getEndAt());
        return priceDetailRepository.save(priceDetail);
    }

    @Transactional
    public void deletePriceDetail(int productId){
        priceDetailRepository.deletePriceDetailByProductId(productId);
    }

    public int getPriceIdByProductId(int productId){
        return priceDetailRepository.getPriceIdByProductId(productId).get(0);
    }

    public PriceDetail getPriceDetailByProductIdAndPriceId(int productId, int priceId){
        return priceDetailRepository.getPriceDetailByProductIdAndPriceId(productId, priceId);
    }

    public void updateEndDate(int productId, int priceId, LocalDateTime endAt){
        priceDetailRepository.updateEndDate(productId, priceId, endAt);
    }

    public void updatePriceDetail(PriceUpdateRequest priceUpdateRequest){
        priceDetailRepository.updatePriceDetail(priceUpdateRequest.getProductId(), priceUpdateRequest.getPriceIdNew(),
                priceUpdateRequest.getPriceAsBigDecimal(), priceUpdateRequest.getStartDate(), priceUpdateRequest.getEndDate());
    }
}
