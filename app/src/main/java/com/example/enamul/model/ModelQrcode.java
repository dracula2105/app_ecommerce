package com.example.enamul.model;

import java.util.Date;
import java.util.List;

public class ModelQrcode {
    private Long idOrder;

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    private String username;
    private Long totalPrice;
    private boolean bank;
    private String address;
    private List<String> product;
    private Date timeOrder;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isBank() {
        return bank;
    }

    public void setBank(boolean bank) {
        this.bank = bank;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getProduct() {
        return product;
    }

    public void setProduct(List<String> product) {
        this.product = product;
    }

    public Date getTimeOrder() {
        return timeOrder;
    }

    public void setTimeOrder(Date timeOrder) {
        this.timeOrder = timeOrder;
    }
}
