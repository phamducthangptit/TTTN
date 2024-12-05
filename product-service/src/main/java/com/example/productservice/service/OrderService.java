package com.example.productservice.service;

import com.example.productservice.dto.*;
import com.example.productservice.entity.Image;
import com.example.productservice.entity.Order;
import com.example.productservice.entity.OrderDetail;
import com.example.productservice.entity.User;
import com.example.productservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AddressService addressService;


    public Order addNewOrder(OrderRequestDTO orderRequestDTO){
        Order order = new Order();
        int userId = accountService.getUserId(orderRequestDTO.getUserName());
        User user = new User();
        user.setUserId(userId);
        order.setUser(user);
        order.setConsigneeName(orderRequestDTO.getName());
        order.setTotalCostOfGoods(orderRequestDTO.getTotalCostOfGoods());
        order.setShippingFee(orderRequestDTO.getShippingFee());
        order.setAddress(orderRequestDTO.getAddress());
        order.setStatus("Mới");
        order.setPhone(orderRequestDTO.getPhone());
        order.setOrderDate(LocalDateTime.now());
        return repository.save(order);
    }
    @Transactional
    public int addOrderGuest(OrderRequestDTO orderRequestDTO){
        // check số lượng còn lại của sản phẩm
        List<CartRequestOrderDTO> cartRequestOrderDTOList = orderRequestDTO.getListProducts();
        for(CartRequestOrderDTO cartRequestOrderDTO : cartRequestOrderDTOList){
            if(productService.getStockProduct(cartRequestOrderDTO.getProductId()) < cartRequestOrderDTO.getQuantity()){
                return -1; // có 1 sản phẩm đã hết
            }
        }
        // add khóa chính trước --> add order
        Order order = addNewOrder(orderRequestDTO);
        if(order != null){
            // add khóa phụ sau
            orderDetailService.addNewOrderDetail(orderRequestDTO, order);
        }
        int userId = accountService.getUserId(orderRequestDTO.getUserName());
        // addAddress
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setUserId(userId);
        addressRequestDTO.setHouseNumber(orderRequestDTO.getHouseNumber());
        addressRequestDTO.setProvinceCode(orderRequestDTO.getProvinceCode());
        addressRequestDTO.setDistrictCode(orderRequestDTO.getDistrictCode());
        addressRequestDTO.setWardCode(orderRequestDTO.getWardCode());
        addressService.saveNewAddress(addressRequestDTO);

        // delete product in cart
        for (CartRequestOrderDTO cartRequestOrderDTO : orderRequestDTO.getListProducts()){
            cartService.deleteProductInCart(orderRequestDTO.getUserName(), cartRequestOrderDTO.getProductId());
            // trừ số lượng sản phẩm đó
            productService.updateStockProduct(cartRequestOrderDTO.getQuantity(), cartRequestOrderDTO.getProductId());
        }
        return order != null ? order.getOrderId() : 0;
    }

    public List<OrderResponseDTO> getAllOrderByUserName(String userName){
        int userId = accountService.getUserId(userName);
        return repository.findAllByUserIdOrderByOrderDateDesc(userId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private OrderResponseDTO convertToDTO(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(order.getOrderId());
        orderResponseDTO.setTotalAmountPaid(order.getTotalCostOfGoods());
        orderResponseDTO.setOrderDate(order.getOrderDate());
        orderResponseDTO.setStatus(order.getStatus());
        orderResponseDTO.setAddress(order.getAddress());
        orderResponseDTO.setPhone(order.getPhone());
        orderResponseDTO.setTotalProduct(totalProduct(order.getOrderDetails()));
        if(order.getStatus().equals("Đã xác nhận")) orderResponseDTO.setCheckStatus(1); else orderResponseDTO.setCheckStatus(0);
        orderResponseDTO.setStatusPayment(order.getStatusPayment());
        return orderResponseDTO;
    }

    private int totalProduct(List<OrderDetail> orderDetails){
        int count = 0;
        for(OrderDetail orderDetail : orderDetails){
            count += orderDetail.getQuantity();
        }
        return count;
    }

    public List<OrderResponseEmployeeDTO> getAllOrderByType(String type){
        return repository.getAllOrderByType(type).stream().map(this::convertOrderEmployeeToDTO).collect(Collectors.toList());
    }

    public OrderResponseEmployeeDTO convertOrderEmployeeToDTO(Order order) {
        OrderResponseEmployeeDTO orderResponseEmployeeDTO = new OrderResponseEmployeeDTO();
        orderResponseEmployeeDTO.setOrderId(order.getOrderId());
        orderResponseEmployeeDTO.setTotalAmountPaid(order.getTotalCostOfGoods());
        orderResponseEmployeeDTO.setOrderDate(order.getOrderDate());
        orderResponseEmployeeDTO.setConsigneeName(order.getConsigneeName());
        orderResponseEmployeeDTO.setAddress(order.getAddress());
        orderResponseEmployeeDTO.setPhone(order.getPhone());
        orderResponseEmployeeDTO.setCheckStatus(order.getStatus().equals("Mới") ? 1 : 0);
        orderResponseEmployeeDTO.setListProducts(order.getOrderDetails().stream().map(this::convertOrderDetailToProduct).collect(Collectors.toList()));
        return orderResponseEmployeeDTO;
    }

    public ProductResponseOrderDTO convertOrderDetailToProduct(OrderDetail orderDetail) {
        ProductResponseOrderDTO productResponseOrderDTO = new ProductResponseOrderDTO();
        productResponseOrderDTO.setProductId(orderDetail.getProduct().getProductId());
        productResponseOrderDTO.setName(orderDetail.getProduct().getName());
//        productResponseOrderDTO.setPrice(orderDetail.getProduct().getPrice());
        String image = orderDetail.getProduct().getImages().stream()
                .filter(img -> img.isAvatar()) // Lọc các ảnh có avatar là true
                .findFirst() // Lấy ảnh đầu tiên thỏa mãn điều kiện
                .map(Image::getUrl) // Lấy URL của ảnh từ Optional<Image>
                .orElse(null);
        productResponseOrderDTO.setImage(image);
        productResponseOrderDTO.setQuantity(orderDetail.getQuantity());
        return productResponseOrderDTO;
    }

    public void employeeUpdateStatusOrder(OrderIdDTO orderIdDTO){
        if(orderIdDTO.getType() == 1){
            repository.updateStatusOrder(orderIdDTO.getOrderId(), "Đã xác nhận");
        }
    }

    @Transactional
    public void guestUpdateStatusOrder(OrderIdDTO orderIdDTO){
        if(orderIdDTO.getType() == 1){// chuyển trạng thái sang hoàn thành
            repository.updateStatusPayment(orderIdDTO.getOrderId(), 1);
            repository.updateStatusOrder(orderIdDTO.getOrderId(), "Hoàn thành");
        }

        if(orderIdDTO.getType() == 2){// chuyển trạng thái sang hủy, nếu hủy thì cộng lại số lượng sản phẩm
            repository.updateStatusPayment(orderIdDTO.getOrderId(), 0);
            repository.updateStatusOrder(orderIdDTO.getOrderId(), "Hủy");
            // cộng lại số lượng sản phẩm
            Order order = repository.findByorderId(orderIdDTO.getOrderId());
            List<OrderDetail> orderDetails = order.getOrderDetails();
            for(OrderDetail orderDetail : orderDetails){
                productService.updateStockProduct(- orderDetail.getQuantity(), orderDetail.getProduct().getProductId());
            }
        }
    }

    public void updateStatusPayment(int orderId){
        repository.updateStatusPayment(orderId, 1);
    }

    public RevenueResponseDTO getRevenue(RevenueRequestDTO revenueRequestDTO){
        List<Object[]> statistics = repository.getDailyCompletedOrderStatistics(revenueRequestDTO.getStartDate(), revenueRequestDTO.getEndDate());
        RevenueResponseDTO revenueResponseDTO = new RevenueResponseDTO();
        List<RevenueDay> listRevenue = new ArrayList<>();
        BigDecimal revenue = BigDecimal.valueOf(0);
        for (Object[] record : statistics) {
            RevenueDay revenueDay = new RevenueDay();
            revenueDay.setDate((LocalDate) record[0]);
            revenueDay.setRevenue((BigDecimal) record[1]);
            listRevenue.add(revenueDay);
            revenue = revenue.add((BigDecimal) record[1]);
        }
        revenueResponseDTO.setRevenue(revenue);
        revenueResponseDTO.setListRevenueDay(listRevenue);
        return revenueResponseDTO;
    }

    public OrderStatisticsDTO getOrderStatistic(RevenueRequestDTO revenueRequestDTO){
        OrderStatisticsDTO orderStatisticsDTO = new OrderStatisticsDTO();
        List<Object[]> result = repository.getOrderStatistics(revenueRequestDTO.getStartDate(), revenueRequestDTO.getEndDate());
        Object[] tmp = result.get(0);
        System.out.println(Arrays.deepToString(tmp));
        // Chuyển tmp[0] (Long) thành int
        orderStatisticsDTO.setCompletedOrders(((Number) tmp[0]).intValue());

        // Chuyển tmp[1] (Long) thành int
        orderStatisticsDTO.setNewOrders(((Number) tmp[1]).intValue());

        // Chuyển tmp[2] (Long) thành int
        orderStatisticsDTO.setCancelledOrders(((Number) tmp[2]).intValue());
        return orderStatisticsDTO;
    }

    public List<Order> getAllOrderPrev7Days(){
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return repository.getAllOrderInPrev7Days(sevenDaysAgo);
    }
}
