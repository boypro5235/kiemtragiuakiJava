package com.example.WebWithSqlserver.controller;

import com.example.WebWithSqlserver.entity.Product;
import com.example.WebWithSqlserver.service.ProductService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductService productService;


    @GetMapping("/getAll")
    public List<Product> getProduct() {
        return productService.getProduct();
    }

    @GetMapping("/getProduct")
    public List<Product> getProduct(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal lowerPrice,
            @RequestParam(required = false) BigDecimal upperPrice) {

        if (name != null && name.contains(" ")) {
            name = null;
        }
        if (name == null || lowerPrice == null || upperPrice == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing or invalid parameters");
        }
        return productService.getProductBySpecialCondition(name, lowerPrice, upperPrice);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody Product product) {
        productService.create(product);
        return "Created";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String delete(@PathVariable Integer id) {
        productService.delete(id);
        return "Deleted";
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String update(@RequestBody Product product, @PathVariable Long id) {
        productService.update(product, id);
        return "Updated";
    }



}

