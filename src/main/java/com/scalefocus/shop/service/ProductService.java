package com.scalefocus.shop.service;

import com.scalefocus.shop.camel.ProductFromCSV;
import com.scalefocus.shop.enumeration.ProductAction;
import com.scalefocus.shop.exception.EmptyCommentException;
import com.scalefocus.shop.exception.ImageNotUploadedException;
import com.scalefocus.shop.exception.InvalidProductDataException;
import com.scalefocus.shop.exception.ProductAlreadyExistsException;
import com.scalefocus.shop.model.entity.Category;
import com.scalefocus.shop.model.entity.Comment;
import com.scalefocus.shop.model.entity.Product;
import com.scalefocus.shop.model.entity.User;
import com.scalefocus.shop.repository.specification.CategoryRepository;
import com.scalefocus.shop.repository.specification.CommentRepository;
import com.scalefocus.shop.repository.specification.ProductRepository;
import com.scalefocus.shop.repository.specification.UserRepository;
import com.scalefocus.shop.service.sftp.SftpClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * <b>This service declares all manipulations that can be done on a {@link Product}.</b>
 */
@Service
@Transactional
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private SftpClient sftpClient;

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    private Category defaultCategory;

    private static final int PAGE_SIZE = 15;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, CommentRepository commentRepository,
                          UserRepository userRepository, SftpClient sftpClient) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.sftpClient = sftpClient;
    }

    /**
     * This method returns the default {@link Category}.
     *
     * @return the default category
     */
    @Bean
    @DependsOn(value = "tableCreation")
    public Category getDefaultCategory() {
        defaultCategory = categoryRepository.getCategory("misc");
        return defaultCategory;
    }

    /**
     * This method is used to validate the input data for products.
     *
     * @param productData   contains product data
     * @param bindingResult contains errors from form validation (if any)
     */
    public void addProduct(Product productData, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new InvalidProductDataException("Invalid product data!", productData, bindingResult, ProductAction.CREATE);

        if (StringUtils.isEmpty(productData.getFile().getOriginalFilename()))
            throw new ImageNotUploadedException("Image not selected!", productData);

        addProduct(productData);
    }

    /**
     * This method is used to add product to the database.
     *
     * @param productData contains product data
     */
    private void addProduct(Product productData) {
        Product product;

        if(productData.getCategory() == null || StringUtils.isEmpty(productData.getCategory().getName())) {
            product = new Product(defaultCategory, productData.getPrice(), productData.getName(), productData.getAvailableQuantity(), productData.getFile().getOriginalFilename());
        } else {
            try {
                categoryRepository.createCategory(new Category(productData.getCategory().getName()));
                logger.info("Category created.");
            } catch (DataIntegrityViolationException e) {
                logger.error("Category already exists!");
            }

            Category category = categoryRepository.getCategory(productData.getCategory().getName());
            product = new Product(category, productData.getPrice(), productData.getName(), productData.getAvailableQuantity(), productData.getFile().getOriginalFilename());
        }

        try {
            productRepository.createProduct(product);
            logger.info("Product created.");
        } catch (DataIntegrityViolationException e) {
            throw new ProductAlreadyExistsException("Product already exists!");
        }

        sftpClient.uploadFile(productData.getFile());
    }

    /**
     * This method is used to edit a product.
     *
     * @param productData   contains product data
     * @param bindingResult contains errors from form validation (if any)
     */
    public void editProduct(Product productData, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new InvalidProductDataException("Invalid product data!", productData, bindingResult, ProductAction.EDIT);

        Product product = productRepository.getProduct(productData.getId());

        if (!StringUtils.isEmpty(productData.getFile().getOriginalFilename())) {
            if (!productRepository.pictureIsBoundToMoreThanOneProduct(product.getPictureName()))
                sftpClient.deleteFile(product.getPictureName());

            sftpClient.uploadFile(productData.getFile());

            productData.setPictureName(productData.getFile().getOriginalFilename());
        }

        long categoryId = categoryRepository.getCategory(productData.getCategory().getName()).getId();

        productRepository.editProduct(productData, categoryId);
        logger.info("Product edited.");
    }

    /**
     * This method is used to remove a product from database.
     *
     * @param productId the id of the product
     */
    public void removeProduct(long productId) {
        Product product = productRepository.getProduct(productId);

        if (!productRepository.pictureIsBoundToMoreThanOneProduct(product.getPictureName())) {
            sftpClient.deleteFile(product.getPictureName());
            logger.info("Picture deleted from remote server.");
        }

        productRepository.deleteProduct(productId);
        logger.info("Product deleted.");
    }

    /**
     * This method is used to get product from the database.
     *
     * @param productId the criteria to be found
     * @return the product
     */
    public Product getProduct(long productId) {
        return productRepository.getProduct(productId);
    }

    /**
     * This method is used to search for products.
     *
     * @param keyword the criteria to search in the database
     * @return found products
     */
    public List<Product> getFoundProducts(String keyword) {
        return productRepository.productsMatching(keyword);
    }

    /**
     * This method is used to add comment to a product.
     *
     * @param username  username of the user
     * @param message   contains the product's comment
     * @param productId the id of the product
     */
    public void addComment(String username, String message, String productId) {
        if (message.equals(StringUtils.EMPTY))
            throw new EmptyCommentException("Empty comment!", productId);

        User user = userRepository.getUser(username);
        Comment comment = new Comment();

        comment.setUser(user);
        comment.setMessage(message);
        comment.setProduct(productRepository.getProduct(Long.parseLong(productId)));

        commentRepository.createComment(comment.getMessage(), user.getId());
        logger.info("Comment created.");

        long commentId = commentRepository.getLastComment().getId();
        productRepository.appendComment(Long.parseLong(productId), commentId);
        logger.info("Comment for product appended.");
    }

    /**
     * This method is used to delete comment.
     *
     * @param commentId the id of the comment
     * @param productId the id of the product
     */
    public void deleteComment(long commentId, long productId) {
        productRepository.removeComment(productId, commentId);
        logger.info("Comment removed.");
        commentRepository.deleteComment(commentId);
        logger.info("Comment deleted.");
    }

    /**
     * This method is used to paginate all products.
     *
     * @param pageNumber the current page number
     * @return 15 products per page
     */
    public List<Product> getPagedProducts(int pageNumber) {
        return productRepository.getProductsByPage(pageNumber, PAGE_SIZE);
    }

    /**
     * This method is used to create products with batch insert.
     *
     * @param products list of products
     */
    public void batchProductsAdd(final List<ProductFromCSV> products) {
        productRepository.batchProductsInsert(products, defaultCategory.getId());
    }
}