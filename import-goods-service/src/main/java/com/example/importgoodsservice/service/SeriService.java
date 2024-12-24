package com.example.importgoodsservice.service;


import com.example.importgoodsservice.entity.Product;
import com.example.importgoodsservice.entity.Seri;
import com.example.importgoodsservice.repository.SeriRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SeriService {
    @Autowired
    private SeriRepository seriRepository;

    @Transactional
    public void createSeriAuto(int quantity, int productId){
        List<Seri> seris = new ArrayList<>();
        Product product = new Product();
        product.setId(productId);
        for (int i = 0; i < quantity; i++) {
            Seri seri = new Seri();
            seri.setProduct(product);
            seri.setSeriNumber(UUID.randomUUID().toString());
            seri.setCreateAt(LocalDateTime.now());
            seris.add(seri);
        }
        seriRepository.saveAll(seris);
    }
}
