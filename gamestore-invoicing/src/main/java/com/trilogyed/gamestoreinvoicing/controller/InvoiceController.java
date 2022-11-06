package com.trilogyed.gamestoreinvoicing.controller;

import com.trilogyed.gamestoreinvoicing.model.Invoice;
import com.trilogyed.gamestoreinvoicing.service.GameStoreInvoicingServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/invoice")
//@CrossOrigin(origins = {"http://localhost:7474"})
public class InvoiceController {

    @Autowired
    GameStoreInvoicingServiceLayer service;

    // Assumption: All orders are final and data privacy is not top priority. Therefore, the Update & Delete EndPoints
    // are left out by design due to its potential danger. The getAllInvoices is a questionable one since it could
    // overwhelm the system and infringes on data privacy; however, it does not damage data as with the Update and Delete

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Invoice purchaseItem(@RequestBody @Valid Invoice invoice) {
        invoice = service.createNewInvoice(invoice);
        return invoice;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Invoice findInvoice(@PathVariable("id") long invoiceId) {
        Invoice  invoice  = service.getInvoiceById(invoiceId);
        if (invoice  == null) {
            throw new IllegalArgumentException("Invoice could not be retrieved for id " + invoiceId);
        } else {
            return invoice ;
        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> findAllInvoices() {
        List<Invoice> invoiceViewModelList = service.getAllInvoices();

        if (invoiceViewModelList == null || invoiceViewModelList.isEmpty()) {
            throw new IllegalArgumentException("No invoices were found.");
        } else {
            return invoiceViewModelList;
        }
    }

    @GetMapping("/cname/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice > findInvoicesByCustomerName(@PathVariable String name) {
        List<Invoice > invoiceViewModelList = service.getInvoicesByCustomerName(name);

        if (invoiceViewModelList == null || invoiceViewModelList.isEmpty()) {
            throw new IllegalArgumentException("No invoices were found for: "+name);
        } else {
            return invoiceViewModelList;
        }
    }
}
