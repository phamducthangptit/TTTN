package com.example.productservice.service;

import com.example.productservice.dto.CartRequestDTO;
import com.example.productservice.dto.CartRequestUpdateDTO;
import com.example.productservice.dto.CartResponseDTO;
import com.example.productservice.dto.OrderRequestDTO;
import com.example.productservice.entity.*;
import com.example.productservice.id.CartId;
import com.example.productservice.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository repository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProductService productService;



    public int countProductInCart(String userName) {
        int userId = accountService.getUserId(userName);
        System.out.println("User id: " + userId);
        return repository.countProductInCart(userId);
    }

    public int countProductByUserIdAndProductId(int userId, int productId) {
        return repository.countProductByUserIdAndProductId(userId, productId);
    }

    public int addProductToCart(CartRequestDTO cartRequestDTO) {
        if (productService.getStockProduct(cartRequestDTO.getProductId()) < cartRequestDTO.getQuantity()) {
            return -1;
        } else {
            int userId = accountService.getUserId(cartRequestDTO.getUserName());
            Cart cart = new Cart();
            User user = new User();
            user.setUserId(userId);
            Product product = new Product();
            product.setProductId(cartRequestDTO.getProductId());
            cart.setUser(user);
            cart.setProduct(product);
            cart.setCartId(new CartId(userId, cartRequestDTO.getProductId()));
            cart.setQuantity(cartRequestDTO.getQuantity());
            if (countProductByUserIdAndProductId(userId, cartRequestDTO.getProductId()) == 0) { //nếu = 0 thì lưu mới
                repository.save(cart);
            } else {// nếu khác 0 thì cập nhật số lượng
                repository.updateQuantityInCart(userId, cartRequestDTO.getProductId(), cartRequestDTO.getQuantity());
            }
            return 1;
        }
    }

    public List<CartResponseDTO> getAllProductInCart(String userName){
        int userId = accountService.getUserId(userName);
        List<CartResponseDTO> listCartResponseDTO = new ArrayList<>();
        List<Cart> listProductInCart = repository.getAllProductInCartByUserId(userId);
        for(Cart cart : listProductInCart){
            CartResponseDTO cartResponseDTO = new CartResponseDTO();
            cartResponseDTO.setProductId(cart.getProduct().getProductId());
            cartResponseDTO.setName(cart.getProduct().getName());
            String image = cart.getProduct().getImages().stream()
                    .filter(img -> img.isAvatar()) // Lọc các ảnh có avatar là true
                    .findFirst() // Lấy ảnh đầu tiên thỏa mãn điều kiện
                    .map(Image::getUrl) // Lấy URL của ảnh từ Optional<Image>
                    .orElse(null);
            cartResponseDTO.setImage(image);
            cartResponseDTO.setQuantity(cart.getQuantity());
            cartResponseDTO.setPrice(cart.getProduct().getPrice().intValue());
            cartResponseDTO.setPriceString(cart.getProduct().getPrice());
            listCartResponseDTO.add(cartResponseDTO);
        }
        return listCartResponseDTO;
    }


    public void deleteProductInCart(String userName, int productId){
        int userId = accountService.getUserId(userName);
        repository.deleteProductInCart(userId, productId);
    }

    public void updateQuantityProductInCart(CartRequestUpdateDTO cartRequestUpdateDTO){
        int userId = accountService.getUserId(cartRequestUpdateDTO.getUserName());
        repository.updateQuantityProductInCart(userId, cartRequestUpdateDTO.getProductId(), cartRequestUpdateDTO.getQuantity());
    }

}
