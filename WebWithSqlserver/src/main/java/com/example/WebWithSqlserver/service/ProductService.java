package com.example.WebWithSqlserver.service;

import com.example.WebWithSqlserver.dto.request.ProductRequest;
import com.example.WebWithSqlserver.entity.Product;
import com.example.WebWithSqlserver.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProduct() {
        try {
            String sql = "SELECT * FROM products";
            List<Product> products = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Product.class));
            return products;
        } catch (Exception e) {
            System.out.print(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve products");
        }
    }

    public List<Product> getProductBySpecialCondition(String name, BigDecimal LowPrice, BigDecimal HighPrice) {
        String sql = "select * from products where name like ? and price >= ? and price <= ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Product.class), "%" + name + "%", LowPrice, HighPrice);
    }

    public Optional<Product> getProductById(Long id) {
        String sql = "select * from products where id = ?";
        Product product;
        try {
            product = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Product.class), id);
        } catch (Exception e) {
            if (e.getMessage().contains("Incorrect result size: expected 1, actual 0")) {
                return Optional.empty();
            }
            throw e;
        }
        return Optional.ofNullable(product);
    }


    public void create(Product product) {
        String sql = "insert into products (name, price, discountPrice, description, category) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getDiscountPrice(), product.getDescription(), product.getCategory());
    }

    public void update(Product product, Long id) {
        String sql = "UPDATE products SET name = ?, price = ?, discountPrice = ?, description = ?, category = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getDiscountPrice(), product.getDescription(), product.getCategory(), id);
    }

    public void delete(Integer id) {
        String sql = "delete from products where id = ?";
        jdbcTemplate.update(sql, id);
    }


}



