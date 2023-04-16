package com.kodilla.ecommercee.repository;


import com.kodilla.ecommercee.domain.Product;
import com.kodilla.ecommercee.domain.ProductGroup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductGroupRepository groupRepository;

    @Test
    public void testProductSave() {
        //Given
        ProductGroup group = new ProductGroup("NAME");
        Product product1 = new Product();
        product1.setName("name");
        product1.setDescription("desc");
        product1.setPrice(66.6);
        product1.setProductGroup(group);
        group.getProducts().add(product1);
        groupRepository.save(group);

        //When
        long id = product1.getId();
        long groupId = group.getId();

        Optional<Product> readProduct = productRepository.findById(id);

        //Then
        assertTrue(readProduct.isPresent() && readProduct.get().getId() == id);

        //CleanUp
        groupRepository.deleteById(groupId);
    }

    @Test
    public void testFindAllProducts(){

        //Given
        ProductGroup group = new ProductGroup("NAME");
        Product product1 = new Product(1L, "Name", "Descr", 10.5, group,new ArrayList<>());
        Product product2 = new Product(2L, "Name2", "Descr2", 11.5, group, new ArrayList<>());
        Product product3 = new Product(3L, "Name3", "Descr3", 12.5, group, new ArrayList<>());

        //When
        groupRepository.save(group);
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        List<Product> products = (List<Product>) productRepository.findAll();
        int productAmount = products.size();

        //Then
        long id = group.getId();
        assertEquals(3, productAmount);

        //Cleanup
        groupRepository.deleteById(id);

    }

    @Test
    public void testDeleteProduct_ShouldSaveGroup(){

        //Given
        ProductGroup group = new ProductGroup("NAME");
        Product product1 = new Product(1L,"Name", "Descr", 5.50, group, new ArrayList<>());
        Product product2 = new Product(2L, "Name2", "Descr2", 7.50, group, new ArrayList<>());

        //When
        groupRepository.save(group);
        productRepository.save(product1);
        productRepository.save(product2);
        long product1Id = productRepository.save(product1).getId();

        //Then
        long id = group.getId();
        productRepository.deleteById(product1Id);
        List<Product> products = (List<Product>) productRepository.findAll();
        int productAmount = products.size();

        assertEquals(1L, productAmount);
        assertTrue(!groupRepository.findAll().isEmpty());

        //CleanUp
        groupRepository.deleteById(id);
    }

}