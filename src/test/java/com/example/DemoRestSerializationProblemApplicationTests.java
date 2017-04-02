package com.example;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoRestSerializationProblemApplicationTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    protected final MediaType MEDIA_TYPE = MediaType.parseMediaType("application/hal+json;charset=UTF-8");

    private MockMvc mockMvc;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Resource
    private WebApplicationContext wac;

    @Before
    public void setUp() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setApprovalStatus(ProductApprovalStatus.APPROVED);

        productRepository.save(productEntity);

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);
        categoryRepository.save(categoryEntity);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).alwaysDo(print()).build();
    }

    @Test
    public void testCreate() throws Exception {

        new TransactionTemplate(transactionManager).execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {

                    mockMvc.perform(patch("http://localhost:8080/categoryEntities/1").content(
                                    "{ \"products\" : [\"http://localhost:8080/productEntities/1\"]}").accept(
                                    MediaType.APPLICATION_JSON_UTF8_VALUE).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andDo(print()).andExpect(
                                    status().isOk());
                } catch (Exception e) {
                    fail(e.getMessage());
                }
            }
        });
    }

    @Test
    @Ignore
    public void contextLoads() throws Exception {

        mockMvc.perform(get("http://localhost:8080/productEntities/1").accept(MEDIA_TYPE).contentType(MEDIA_TYPE)).andExpect(status().isOk()).andExpect(
                        content().contentType(MEDIA_TYPE));
    }

}
