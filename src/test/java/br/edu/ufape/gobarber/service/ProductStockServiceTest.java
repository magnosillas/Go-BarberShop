package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.Product;
import br.edu.ufape.gobarber.model.ProductStock;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade ProductStock e suas operações básicas.
 * Estes testes validam a criação, modificação e integridade dos dados de estoque de produtos.
 */
@SpringBootTest
public class ProductStockServiceTest {

    /**
     * Testa a criação de um estoque de produto com todos os campos preenchidos.
     */
    @Test
    public void testProductStockCreation() {
        Product product = new Product(1, "Pomada", "Shine", "Pomada modeladora", 25.0, "100ml");

        ProductStock stock = new ProductStock(
                1,
                "LOTE001",
                50,
                LocalDate.of(2025, 12, 31),
                LocalDate.of(2024, 1, 15),
                product
        );

        assertEquals(1, stock.getIdStock());
        assertEquals("LOTE001", stock.getBatchNumber());
        assertEquals(50, stock.getQuantity());
        assertEquals(LocalDate.of(2025, 12, 31), stock.getExpirationDate());
        assertEquals(LocalDate.of(2024, 1, 15), stock.getAcquisitionDate());
        assertNotNull(stock.getProduct());
    }

    /**
     * Testa a modificação de dados do estoque.
     */
    @Test
    public void testUpdateProductStockData() {
        ProductStock stock = new ProductStock();
        stock.setBatchNumber("LOTE002");
        stock.setQuantity(100);

        assertEquals("LOTE002", stock.getBatchNumber());
        assertEquals(100, stock.getQuantity());
    }

    /**
     * Testa o construtor padrão do ProductStock.
     */
    @Test
    public void testDefaultConstructor() {
        ProductStock stock = new ProductStock();

        assertNull(stock.getIdStock());
        assertNull(stock.getBatchNumber());
        assertNull(stock.getQuantity());
        assertNull(stock.getExpirationDate());
        assertNull(stock.getAcquisitionDate());
        assertNull(stock.getProduct());
    }

    /**
     * Testa a associação de produto ao estoque.
     */
    @Test
    public void testProductStockWithProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("Gel");
        product.setBrand("HairStyle");

        ProductStock stock = new ProductStock();
        stock.setProduct(product);

        assertNotNull(stock.getProduct());
        assertEquals("Gel", stock.getProduct().getName());
        assertEquals("HairStyle", stock.getProduct().getBrand());
    }

    /**
     * Testa a modificação da quantidade em estoque.
     */
    @Test
    public void testQuantityModification() {
        ProductStock stock = new ProductStock();
        stock.setQuantity(100);

        assertEquals(100, stock.getQuantity());

        stock.setQuantity(50);
        assertEquals(50, stock.getQuantity());
    }

    /**
     * Testa estoque com quantidade zero.
     */
    @Test
    public void testStockWithZeroQuantity() {
        ProductStock stock = new ProductStock();
        stock.setQuantity(0);

        assertEquals(0, stock.getQuantity());
    }

    /**
     * Testa a data de validade do produto.
     */
    @Test
    public void testExpirationDateValidation() {
        LocalDate futureDate = LocalDate.now().plusYears(1);
        LocalDate pastDate = LocalDate.now().minusDays(1);

        ProductStock validStock = new ProductStock();
        validStock.setExpirationDate(futureDate);

        ProductStock expiredStock = new ProductStock();
        expiredStock.setExpirationDate(pastDate);

        assertTrue(validStock.getExpirationDate().isAfter(LocalDate.now()));
        assertTrue(expiredStock.getExpirationDate().isBefore(LocalDate.now()));
    }

    /**
     * Testa a data de aquisição.
     */
    @Test
    public void testAcquisitionDateAssignment() {
        LocalDate acquisitionDate = LocalDate.of(2024, 6, 15);
        ProductStock stock = new ProductStock();
        stock.setAcquisitionDate(acquisitionDate);

        assertEquals(acquisitionDate, stock.getAcquisitionDate());
    }

    /**
     * Testa números de lote diferentes.
     */
    @Test
    public void testDifferentBatchNumbers() {
        ProductStock stock1 = new ProductStock();
        stock1.setBatchNumber("LOTE2024001");

        ProductStock stock2 = new ProductStock();
        stock2.setBatchNumber("LOTE2024002");

        assertNotEquals(stock1.getBatchNumber(), stock2.getBatchNumber());
    }

    /**
     * Testa a cronologia das datas.
     */
    @Test
    public void testDateChronology() {
        ProductStock stock = new ProductStock();
        LocalDate acquisitionDate = LocalDate.of(2024, 1, 1);
        LocalDate expirationDate = LocalDate.of(2025, 12, 31);

        stock.setAcquisitionDate(acquisitionDate);
        stock.setExpirationDate(expirationDate);

        assertTrue(stock.getExpirationDate().isAfter(stock.getAcquisitionDate()));
    }

    /**
     * Testa múltiplos estoques para o mesmo produto.
     */
    @Test
    public void testMultipleStocksForSameProduct() {
        Product product = new Product(1, "Shampoo", "Clean", "Shampoo anticaspa", 15.0, "200ml");

        ProductStock stock1 = new ProductStock();
        stock1.setIdStock(1);
        stock1.setBatchNumber("LOTE001");
        stock1.setQuantity(30);
        stock1.setProduct(product);

        ProductStock stock2 = new ProductStock();
        stock2.setIdStock(2);
        stock2.setBatchNumber("LOTE002");
        stock2.setQuantity(20);
        stock2.setProduct(product);

        assertEquals(stock1.getProduct().getId(), stock2.getProduct().getId());
        assertNotEquals(stock1.getIdStock(), stock2.getIdStock());
        assertEquals(50, stock1.getQuantity() + stock2.getQuantity());
    }
}
