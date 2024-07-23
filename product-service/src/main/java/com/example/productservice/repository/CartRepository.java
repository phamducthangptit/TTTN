package com.example.productservice.repository;

import com.example.productservice.entity.Cart;
import com.example.productservice.id.CartId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, CartId> {

    @Query("SELECT COALESCE(SUM(c.quantity), 0) FROM Cart c WHERE c.cartId.userId = :userId")
    int countProductInCart(@Param("userId") int userId);

    @Query("SELECT COUNT(c) FROM Cart c WHERE c.cartId.userId = :userId AND c.cartId.productId = :productId")
    int countProductByUserIdAndProductId(@Param("userId") int userId, @Param("productId") int productId);

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.quantity = c.quantity + :quantity WHERE c.cartId.userId = :userId AND c.cartId.productId = :productId")
    void updateQuantityInCart(@Param("userId") int userId, @Param("productId") int productId, @Param("quantity") int quantity);

    @Query("SELECT c FROM Cart c WHERE c.user.userId = :userId")
    List<Cart> getAllProductInCartByUserId(@Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.user.userId = :userId AND c.product.productId = :productId")
    void deleteProductInCart(@Param("userId") int userId, @Param("productId") int productId);

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.quantity = :quantity WHERE c.user.userId = :userId AND c.product.productId = :productId")
    void updateQuantityProductInCart(@Param("userId") int userId, @Param("productId") int productId, @Param("quantity") int quantity);
}
