package com.example.importgoodsservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "goods_receipt", schema = "sell_electronics_online")
public class GoodsReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_receipt_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "is_complete", nullable = false)
    private Boolean isComplete;

    @OneToMany(mappedBy = "goodsReceipt")
    private List<GoodsReceiptDetail> goodsReceiptDetails;

}