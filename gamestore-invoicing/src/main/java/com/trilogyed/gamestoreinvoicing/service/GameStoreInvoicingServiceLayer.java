package com.trilogyed.gamestoreinvoicing.service;


import com.trilogyed.gamestoreinvoicing.feign.GameStoreCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import com.trilogyed.gamestoreinvoicing.repository.*;
import com.trilogyed.gamestoreinvoicing.model.*;

@Component
public class GameStoreInvoicingServiceLayer {

    @Autowired
    private GameStoreCatalog client;

    void InvoiceController(GameStoreCatalog client) {
        this.client = client;
    }

    private final BigDecimal PROCESSING_FEE = new BigDecimal("15.49");
    private final BigDecimal MAX_INVOICE_TOTAL = new BigDecimal("999.99");

    private final String GAME_ITEM_TYPE = "Game";
    private final String CONSOLE_ITEM_TYPE = "Console";
    private final String TSHIRT_ITEM_TYPE = "T-Shirt";

    InvoiceRepository invoiceRepo;
    TaxRepository taxRepo;
    ProcessingFeeRepository processingFeeRepo;


    @Autowired
    public GameStoreInvoicingServiceLayer(GameStoreCatalog client, InvoiceRepository invoiceRepo,
                                          TaxRepository taxRepo, ProcessingFeeRepository processingFeeRepo) {
        this.client = client;
        this.invoiceRepo = invoiceRepo;
        this.taxRepo = taxRepo;
        this.processingFeeRepo = processingFeeRepo;
    }

    public Invoice createNewInvoice(Invoice invoice) {

        //validation...
        if (invoice == null)
            throw new NullPointerException("Create invoice failed. no invoice data.");

        if (invoice.getItemType() == null)
            throw new IllegalArgumentException("Unrecognized Item type. Valid ones: Console or Game");

        //Check Quantity is > 0...
        if (invoice.getQuantity() <= 0) {
            throw new IllegalArgumentException(invoice.getQuantity() +
                    ": Unrecognized Quantity. Must be > 0.");
        }

        //Checks the item type and get the correct unit price
        //Check if we have enough quantity
        if (invoice.getItemType().equals(CONSOLE_ITEM_TYPE)) {
            Console tempCon = null;
            Optional<Console> returnVal = Optional.ofNullable(client.getConsoleById(invoice.getItemId()));

            if (returnVal.isPresent()) {
                tempCon = returnVal.get();
            } else {
                throw new IllegalArgumentException("Requested item is unavailable.");
            }

            if (invoice.getQuantity() > tempCon.getQuantity()) {
                throw new IllegalArgumentException("Requested quantity is unavailable.");
            }

            invoice.setUnitPrice(tempCon.getPrice());

        } else if (invoice.getItemType().equals(GAME_ITEM_TYPE)) {
            Game tempGame = null;
            Optional<Game> returnVal = Optional.ofNullable(client.getGameById(invoice.getItemId()));

            if (returnVal.isPresent()) {
                tempGame = returnVal.get();
            } else {
                throw new IllegalArgumentException("Requested item is unavailable.");
            }

            if (invoice.getQuantity() > tempGame.getQuantity()) {
                throw new IllegalArgumentException("Requested quantity is unavailable.");
            }
            invoice.setUnitPrice(tempGame.getPrice());

        } else if (invoice.getItemType().equals(TSHIRT_ITEM_TYPE)) {
            TShirt tempTShirt = null;
            Optional<TShirt> returnVal = Optional.ofNullable(client.getTshirtById(invoice.getItemId()));

            if (returnVal.isPresent()) {
                tempTShirt = returnVal.get();
            } else {
                throw new IllegalArgumentException("Requested item is unavailable.");
            }

            if (invoice.getQuantity() > tempTShirt.getQuantity()) {
                throw new IllegalArgumentException("Requested quantity is unavailable.");
            }
            invoice.setUnitPrice(tempTShirt.getPrice());

        } else {
            throw new IllegalArgumentException(invoice.getItemType() +
                    ": Unrecognized Item type. Valid ones: T-Shirt, Console, or Game");
        }
        invoice.setSubtotal(
                invoice.getUnitPrice().multiply(
                        new BigDecimal(invoice.getQuantity())).setScale(2, RoundingMode.HALF_UP));

        /**   check this one*/
        //Throw Exception if subtotal is greater than 999.99
        if ((invoice.getSubtotal().compareTo(new BigDecimal(999.99)) > 0)) {
            throw new IllegalArgumentException("Subtotal exceeds maximum purchase price of $999.99");
        }

        //Validate State and Calc tax...
        BigDecimal tempTaxRate;
        Optional<Tax> returnVal = taxRepo.findById(invoice.getState());

        if (returnVal.isPresent()) {
            tempTaxRate = returnVal.get().getRate();
        } else {
            throw new IllegalArgumentException(invoice.getState() + ": Invalid State code.");
        }

        if (!tempTaxRate.equals(BigDecimal.ZERO))
            invoice.setTax(tempTaxRate.multiply(invoice.getSubtotal()));
        else
            throw new IllegalArgumentException(invoice.getState() + ": Invalid State code.");

        BigDecimal processingFee;
        Optional<ProcessingFee> returnVal2 = processingFeeRepo.findById(invoice.getItemType());

        if (returnVal2.isPresent()) {
            processingFee = returnVal2.get().getFee();
        } else {
            throw new IllegalArgumentException("Requested item is unavailable.");
        }

        invoice.setProcessingFee(processingFee);

        //Checks if quantity of items if greater than 10 and adds additional processing fee
        if (invoice.getQuantity() > 10) {
            invoice.setProcessingFee(invoice.getProcessingFee().add(PROCESSING_FEE));
        }

        invoice.setTotal(invoice.getSubtotal().add(invoice.getProcessingFee()).add(invoice.getTax()));

        //checks total for validation
        if ((invoice.getTotal().compareTo(MAX_INVOICE_TOTAL) > 0)) {
            throw new IllegalArgumentException("Subtotal exceeds maximum purchase price of $999.99");
        }
        invoice = invoiceRepo.save(invoice);
            return invoice;
    }

    public Invoice getInvoiceById(long id) {
        Optional<Invoice> invoice = invoiceRepo.findById(id);
        if (invoice == null) {
            return null;
        } else {
            return invoice.get();
        }
    }
    public List<Invoice> getInvoicesByCustomerName(String name){
        List<Invoice> invoiceList = invoiceRepo.findByName(name);
        if(invoiceList == null){
            return null;
        }else{
            return invoiceList;
        }
    }
    public List<Invoice> getAllInvoices(){
        List<Invoice> invoiceList = invoiceRepo.findAll();
        if(invoiceList == null){
            return null;
        }else{
            return invoiceList;
        }
    }
    public void deleteInvoice(long id){
        invoiceRepo.deleteById(id);
    }
}