package com.trilogyed.gamestoreinvoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private long id;
    @NotEmpty(message = "Name is required, please.")
    private String name;
    @NotEmpty(message="Street name is required, please.")
    private String street;
    @NotEmpty(message = "City name is required, please.")
    private String city;
    @NotEmpty(message = "State name is required, please.")
    @Size(min = 2, max = 2, message = "2-Letter State Code is invalid.")
    private String state;
    @NotEmpty(message = "Zipcode is required, please.")
    private String zipcode;
    @NotEmpty(message = "Item type is required, please.")
    private String itemType;
    @NotNull(message = "An item id is required, please.")
    private long itemId;
    private BigDecimal unitPrice;
    @NotNull(message = "QUantity is required, please.")
    @Min(value = 1, message = "Minimum quantity is 1.")
    @Max(value = 50000, message = "Maximum quantity is 50,00.")
    private long quantity;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal processingFee;
    private BigDecimal total;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(BigDecimal processingFee) {
        this.processingFee = processingFee;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return getId() == invoice.getId() &&
                getItemId() == invoice.getItemId() &&
                getQuantity() == invoice.getQuantity() &&
                Objects.equals(getName(), invoice.getName()) &&
                Objects.equals(getStreet(), invoice.getStreet()) &&
                Objects.equals(getCity(), invoice.getCity()) &&
                Objects.equals(getState(), invoice.getState()) &&
                Objects.equals(getZipcode(), invoice.getZipcode()) &&
                Objects.equals(getItemType(), invoice.getItemType()) &&
                Objects.equals(getUnitPrice(), invoice.getUnitPrice()) &&
                Objects.equals(getSubtotal(), invoice.getSubtotal()) &&
                Objects.equals(getTax(), invoice.getTax()) &&
                Objects.equals(getProcessingFee(), invoice.getProcessingFee()) &&
                Objects.equals(getTotal(), invoice.getTotal());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getStreet(), getCity(), getState(), getZipcode(),
                getItemType(), getItemId(), getUnitPrice(), getQuantity(), getSubtotal(), getTax(),
                getProcessingFee(), getTotal());
    }
}
