package com.rsr.product_microservice.unitTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsr.product_microservice.ProductFactory;
import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.impl.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService mockProductService;


    @Nested
    @DisplayName("Test cases for getting a product or multiple products via RestController - White Box Test")
    class GetProductTests {

        @Test
        public void getAllProductsTest() throws Exception {
            Product product_rock = ProductFactory.getExampleValidProduct();
            Product product_stone = ProductFactory.getExampleValidProduct();
            product_rock.setName("Rock");
            product_stone.setName("Stone");

            List<Product> products = Arrays.asList(
                    product_rock,
                    product_stone
            );

            Mockito.when(mockProductService.getAllProducts()).thenReturn(products);

            mockMvc.perform(MockMvcRequestBuilders.get("/products")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(products.size()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Rock"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Stone"));
        }

        @Test
        void getProductByIdTest() throws Exception {
            Product productRock = ProductFactory.getExampleValidProduct();
            UUID rockId = UUID.randomUUID();
            productRock.setName("Rock");
            productRock.setId(rockId);

            Product productStone = ProductFactory.getExampleValidProduct();
            UUID stoneId = UUID.randomUUID();
            productStone.setName("Stone");
            productStone.setId(stoneId);

            List<Product> products = Arrays.asList(
                    productRock,
                    productStone
            );

            Mockito.when(mockProductService.getAllProducts()).thenReturn(products);
            Mockito.when(mockProductService.getProductById(rockId)).thenReturn(productRock);
            Mockito.when(mockProductService.getProductById(stoneId)).thenReturn(productStone);

            mockMvc.perform(MockMvcRequestBuilders.get("/product/" + rockId.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath(".name").value("Rock"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(rockId.toString()));

        }
    }

}
