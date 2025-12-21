package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.page.PageProductDTO;
import br.edu.ufape.gobarber.dto.product.ProductCreateDTO;
import br.edu.ufape.gobarber.dto.product.ProductDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Product;
import br.edu.ufape.gobarber.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductDTO createProduct(@Valid ProductCreateDTO productCreateDTO) throws DataBaseException{
        try {
            Product product = convertDTOtoEntity(productCreateDTO);

            product = productRepository.save(product);

            return convertEntityToDTO(product);
        }catch (Exception e) {
            throw new DataBaseException("Erro ao criar produto: " + e.getMessage());
        }
    }

    @Transactional
    public ProductDTO updateProduct(Integer id, ProductCreateDTO updatedProductDTO) throws DataBaseException{
        Product product = productRepository.findById(id).orElseThrow(() -> new DataBaseException("Produto não encontrado no banco de dados."));

        product.setName(updatedProductDTO.getName());
        product.setBrand(updatedProductDTO.getBrand());
        product.setDescription(updatedProductDTO.getDescription());
        product.setPrice(updatedProductDTO.getPrice());
        product.setSize(updatedProductDTO.getSize());

        product = productRepository.save(product);

        return convertEntityToDTO(product);
    }

    @Transactional
    public void deleteProduct(Integer id) throws DataBaseException{
        Product product = productRepository.findById(id).orElseThrow(() -> new DataBaseException("Produto não encontrado no banco de dados."));

        productRepository.delete(product);
    }

    public ProductDTO getProduct(Integer id) throws DataBaseException{
        return convertEntityToDTO(productRepository.findById(id).orElseThrow(() -> new DataBaseException("Produto não encontrado no banco de dados.")));
    }

    public PageProductDTO getAllProducts(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        Page<ProductDTO> productDTOPage = productPage.map(this::convertEntityToDTO);

        return new PageProductDTO(
                productDTOPage.getTotalElements(),
                productDTOPage.getTotalPages(),
                productDTOPage.getPageable().getPageNumber(),
                productDTOPage.getSize(),
                productDTOPage.getContent()
        );
    }

    protected Product getProductById(Integer id) throws DataBaseException {
        return productRepository.findById(id).orElseThrow(() -> new DataBaseException("Não existe Produto com o id informado"));
    }

    protected Product convertDTOtoEntity(ProductCreateDTO productCreateDTO) {
        Product product = new Product();
        product.setName(productCreateDTO.getName());
        product.setBrand(productCreateDTO.getBrand());
        product.setDescription(productCreateDTO.getDescription());
        product.setPrice(productCreateDTO.getPrice());
        product.setSize(productCreateDTO.getSize());

        return product;
    }

    private ProductDTO convertEntityToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setBrand(product.getBrand());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setSize(product.getSize());

        return productDTO;
    }
}
