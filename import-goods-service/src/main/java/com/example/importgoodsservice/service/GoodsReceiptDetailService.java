package com.example.importgoodsservice.service;

import com.example.importgoodsservice.dto.GoodsReceiptDetailDTO;
import com.example.importgoodsservice.dto.ProductDTO;
import com.example.importgoodsservice.entity.GoodsReceipt;
import com.example.importgoodsservice.entity.GoodsReceiptDetail;
import com.example.importgoodsservice.entity.Product;
import com.example.importgoodsservice.repository.GoodsReceiptDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class GoodsReceiptDetailService {
    @Autowired
    private GoodsReceiptDetailRepository goodsReceiptDetailRepository;

    public void saveGoodsReceiptDetail(int goodsReceiptId, GoodsReceiptDetailDTO goodsReceiptDetailDTO) {
        GoodsReceiptDetail goodsReceiptDetail = new GoodsReceiptDetail();
        GoodsReceipt goodsReceipt = new GoodsReceipt();
        goodsReceipt.setId(goodsReceiptId);
        Product product = new Product();
        product.setId(goodsReceiptDetailDTO.getProductId());
        goodsReceiptDetail.setGoodsReceipt(goodsReceipt);
        goodsReceiptDetail.setProduct(product);
        goodsReceiptDetail.setQuantity(goodsReceiptDetailDTO.getQuantity());
        goodsReceiptDetail.setPrice(goodsReceiptDetailDTO.getPrice());
        goodsReceiptDetailRepository.save(goodsReceiptDetail);
    }

    public void deleteByGoodsReceiptId(int goodsReceiptId) {
        goodsReceiptDetailRepository.deleteByGoodsReceiptId(goodsReceiptId);
    }

    public List<ProductDTO> getAllProductNoWarehouseReceipt(int goodsReceiptId) {
        List<Product> listProduct = goodsReceiptDetailRepository.getAllProductNoWarehouseReceipt(goodsReceiptId);
        return listProduct.stream().map(this::convertProductToDTO).collect(Collectors.toList());
    }

    public ProductDTO convertProductToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getId());
        productDTO.setProductName(product.getName());
        return productDTO;
    }
}
