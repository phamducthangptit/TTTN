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


    public Order addNewOrder(OrderRequestDTO orderRequestDTO){
        Order order = new Order();
        int userId = accountService.getUserId(orderRequestDTO.getUserName());
        User user = new User();
        user.setUserId(userId);
        order.setUser(user);
        order.setConsigneeName(orderRequestDTO.getName());
        order.setTotalAmount(BigDecimal.valueOf(orderRequestDTO.getTotalAmount()));
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
        orderResponseDTO.setTotalAmountPaid(order.getTotalAmount());
        orderResponseDTO.setOrderDate(order.getOrderDate());
        orderResponseDTO.setStatus(order.getStatus());
        orderResponseDTO.setAddress(order.getAddress());
        orderResponseDTO.setPhone(order.getPhone());
        orderResponseDTO.setTotalProduct(totalProduct(order.getOrderDetails()));
        if(order.getStatus().equals("Đã xác nhận")) orderResponseDTO.setCheckStatus(1); else orderResponseDTO.setCheckStatus(0);
        if(order.getStatus().equals("Mới")) orderResponseDTO.setCheckStatusInvoice(0); else orderResponseDTO.setCheckStatusInvoice(1);
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
        orderResponseEmployeeDTO.setTotalAmountPaid(order.getTotalAmount());
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
        productResponseOrderDTO.setPrice(orderDetail.getProduct().getPrice());
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

    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    public Map<String, BigDecimal> getRevenueForThisWeek() {
        ZonedDateTime now = ZonedDateTime.now(VIETNAM_ZONE);

        // Xác định ngày bắt đầu và kết thúc tuần hiện tại
        ZonedDateTime startOfWeek = now.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime endOfWeek = startOfWeek.plusDays(6)
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // Chuyển đổi thành LocalDateTime
        LocalDateTime startDateTime = startOfWeek.toLocalDateTime();
        LocalDateTime endDateTime = endOfWeek.toLocalDateTime();

        // Tạo danh sách các ngày trong tuần hiện tại
        Map<String, BigDecimal> revenueMap = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.toLocalDate().plusDays(i);
            revenueMap.put(date.toString(), BigDecimal.ZERO);
        }

        // Lấy doanh thu từ cơ sở dữ liệu
        List<Object[]> results = repository.getRevenueForDateRange(startDateTime, endDateTime);

        for (Object[] result : results) {
            LocalDate date = ((LocalDateTime) result[0]).toLocalDate();
            BigDecimal revenue = (BigDecimal) result[1];
            revenueMap.put(date.toString(), revenueMap.getOrDefault(date.toString(), BigDecimal.ZERO).add(revenue));
        }

        return revenueMap;
    }

    public Map<String, BigDecimal> getRevenueForLast5Weeks() {
        ZonedDateTime now = ZonedDateTime.now(VIETNAM_ZONE);
        ZonedDateTime startDate = now.minusWeeks(4).with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startDateTime = startDate.toLocalDateTime();

        // Tạo danh sách các tuần trong 5 tuần qua
        Map<String, BigDecimal> revenueMap = new LinkedHashMap<>();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        for (int i = 0; i < 5; i++) {
            LocalDate startOfWeek = startDate.toLocalDate().plusWeeks(i).with(DayOfWeek.MONDAY);
            LocalDate endOfWeek = startOfWeek.plusDays(6);
            String week = startOfWeek.getYear() + "-W" + startOfWeek.get(weekFields.weekOfWeekBasedYear());
            revenueMap.put(week, BigDecimal.ZERO);
        }

        List<Object[]> results = repository.getRevenueForWeekRange(startDateTime, now.toLocalDateTime());

        for (Object[] result : results) {
            int year = (int) result[0];
            int week = (int) result[1];
            BigDecimal revenue = (BigDecimal) result[2];
            String weekString = year + "-W" + week;
            revenueMap.put(weekString, revenue);
        }

        return revenueMap;
    }

    public Map<String, BigDecimal> getRevenueForLast3Months() {
        ZonedDateTime now = ZonedDateTime.now(VIETNAM_ZONE);
        ZonedDateTime startDate = now.minusMonths(2).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startDateTime = startDate.toLocalDateTime();

        // Tạo danh sách các tháng trong 3 tháng qua
        Map<String, BigDecimal> revenueMap = new LinkedHashMap<>();
        for (int i = 0; i < 3; i++) {
            LocalDate monthStart = startDate.toLocalDate().plusMonths(i).withDayOfMonth(1);
            String month = monthStart.getYear() + "-" + String.format("%02d", monthStart.getMonthValue());
            revenueMap.put(month, BigDecimal.ZERO);
        }

        List<Object[]> results = repository.getRevenueForMonthRange(startDateTime, now.toLocalDateTime());

        for (Object[] result : results) {
            int year = (int) result[0];
            int month = (int) result[1];
            BigDecimal revenue = (BigDecimal) result[2];
            String monthString = year + "-" + String.format("%02d", month);
            revenueMap.put(monthString, revenue);
        }

        return revenueMap;
    }
    public Map<String, Map<String, Long>> getOrderCountByStatusForThisWeek() {
        ZonedDateTime now = ZonedDateTime.now(VIETNAM_ZONE);
        ZonedDateTime startOfWeek = now.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime endOfWeek = startOfWeek.plusDays(6)
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        LocalDateTime startDateTime = startOfWeek.toLocalDateTime();
        LocalDateTime endDateTime = endOfWeek.toLocalDateTime();

        return getOrderCountByStatusGroupedByDate(startDateTime, endDateTime);
    }

    public Map<String, Map<String, Long>> getOrderCountByStatusForLast5Weeks() {
        ZonedDateTime now = ZonedDateTime.now(VIETNAM_ZONE);
        ZonedDateTime startDate = now.minusWeeks(4).with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startDateTime = startDate.toLocalDateTime();

        ZonedDateTime endDate = now.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        LocalDateTime endDateTime = endDate.toLocalDateTime();

        return getOrderCountByStatusGroupedByWeek(startDateTime, endDateTime);
    }

    public Map<String, Map<String, Long>> getOrderCountByStatusForLast3Months() {
        ZonedDateTime now = ZonedDateTime.now(VIETNAM_ZONE);
        ZonedDateTime startDate = now.minusMonths(2).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startDateTime = startDate.toLocalDateTime();

        ZonedDateTime endDate = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        LocalDateTime endDateTime = endDate.toLocalDateTime();

        return getOrderCountByStatusGroupedByMonth(startDateTime, endDateTime);
    }

    private Map<String, Map<String, Long>> getOrderCountByStatusGroupedByDate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Object[]> results = repository.getOrderCountByStatusForDateRange(startDateTime, endDateTime);
        Map<String, Map<String, Long>> groupedByDate = new LinkedHashMap<>();

        // Initialize the map with 0 for each day in the range
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            groupedByDate.put(date.toString(), initializeStatusCountsMap());
        }

        for (Object[] result : results) {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate();
            String status = (String) result[1];
            Long count = ((Number) result[2]).longValue();
            groupedByDate.get(date.toString()).put(status, count);
        }

        return groupedByDate;
    }

    private Map<String, Map<String, Long>> getOrderCountByStatusGroupedByWeek(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Object[]> results = repository.getOrderCountByStatusForDateRange(startDateTime, endDateTime);
        Map<String, Map<String, Long>> groupedByWeek = new LinkedHashMap<>();

        // Initialize the map with 0 for each week in the range
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();
        LocalDate firstDayOfWeek = startDate.with(WeekFields.ISO.dayOfWeek(), 1); // Monday of the first week
        LocalDate lastDayOfWeek = endDate.with(WeekFields.ISO.dayOfWeek(), 7); // Sunday of the last week

        // Create a list of all weeks in the range
        while (!firstDayOfWeek.isAfter(lastDayOfWeek)) {
            int weekOfYear = firstDayOfWeek.get(WeekFields.ISO.weekOfWeekBasedYear());
            int year = firstDayOfWeek.getYear();
            String weekLabel = "Week " + weekOfYear + "-" + year;
            groupedByWeek.putIfAbsent(weekLabel, initializeStatusCountsMap());
            firstDayOfWeek = firstDayOfWeek.plusWeeks(1);
        }

        // Process results
        for (Object[] result : results) {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate();
            int weekOfYear = date.get(WeekFields.ISO.weekOfWeekBasedYear());
            int year = date.getYear();
            String weekLabel = "Week " + weekOfYear + "-" + year;
            String status = (String) result[1];
            Long count = ((Number) result[2]).longValue();

            // Update count
            groupedByWeek.computeIfAbsent(weekLabel, k -> initializeStatusCountsMap())
                    .merge(status, count, Long::sum);
        }

        return groupedByWeek;
    }

    private Map<String, Map<String, Long>> getOrderCountByStatusGroupedByMonth(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Object[]> results = repository.getOrderCountByStatusForDateRange(startDateTime, endDateTime);
        Map<String, Map<String, Long>> groupedByMonth = new LinkedHashMap<>();

        // Initialize the map with 0 for each month in the range
        YearMonth startMonth = YearMonth.from(startDateTime.toLocalDate());
        YearMonth endMonth = YearMonth.from(endDateTime.toLocalDate());
        for (YearMonth month = startMonth; !month.isAfter(endMonth); month = month.plusMonths(1)) {
            groupedByMonth.put(month.toString(), initializeStatusCountsMap());
        }

        // Process results
        for (Object[] result : results) {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate();
            YearMonth month = YearMonth.from(date);
            String status = (String) result[1];
            Long count = ((Number) result[2]).longValue();

            // Update count
            groupedByMonth.computeIfAbsent(month.toString(), k -> initializeStatusCountsMap())
                    .merge(status, count, Long::sum);
        }

        return groupedByMonth;
    }

    private Map<String, Long> initializeStatusCountsMap() {
        Map<String, Long> statusCounts = new LinkedHashMap<>();
        statusCounts.put("Mới", 0L);
        statusCounts.put("Đã xác nhận", 0L);
        statusCounts.put("Hoàn thành", 0L);
        statusCounts.put("Hủy", 0L);
        return statusCounts;
    }
}
