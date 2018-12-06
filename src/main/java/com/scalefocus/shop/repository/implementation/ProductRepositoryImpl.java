package com.scalefocus.shop.repository.implementation;

import com.scalefocus.shop.camel.ProductFromCSV;
import com.scalefocus.shop.model.entity.Product;
import com.scalefocus.shop.repository.specification.CategoryRepository;
import com.scalefocus.shop.repository.specification.CommentRepository;
import com.scalefocus.shop.repository.specification.ProductRepository;
import com.scalefocus.shop.service.sftp.SftpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @PersistenceContext
    private EntityManager em;

    private JdbcTemplate jdbcTemplate;

    private SftpClient sftpClient;

    private CategoryRepository categoryRepository;
    private CommentRepository commentRepository;

    @Autowired
    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager em, SftpClient sftpClient,
                                 CategoryRepository categoryRepository, CommentRepository commentRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.em = em;
        this.sftpClient = sftpClient;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void createProduct(Product product) {
        em.createNativeQuery("insert into products (name, price, available_quantity, category_id, picture_name) " +
                "values (:name, :price, :quantity, :categoryId, :picture)")
                .setParameter("name", product.getName())
                .setParameter("price", product.getPrice())
                .setParameter("quantity", product.getAvailableQuantity())
                .setParameter("categoryId", product.getCategory().getId())
                .setParameter("picture", product.getPictureName())
                .executeUpdate();
    }

    @Override
    public void batchProductsInsert(List<ProductFromCSV> products, long defaultCategoryId) {
        String sql = "insert into products (name, price, available_quantity, category_id) values (?, ?, ?, ?);";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ProductFromCSV productFromCSV = products.get(i);
                ps.setString(1, productFromCSV.getName());
                ps.setDouble(2, productFromCSV.getPrice());
                ps.setInt(3, productFromCSV.getQuantity());
                ps.setLong(4, defaultCategoryId);
            }

            @Override
            public int getBatchSize() {
                return products.size();
            }

        });
    }

    @Override
    public void editProduct(Product productData, long categoryId) {
        Product product = getProduct(productData.getId());

        if (!product.getPrice().equals(productData.getPrice())) { // Workaround due to 'price' being criteria for partition
            em.createNativeQuery("delete from products where id=:id", Product.class)
                    .setParameter("id", product.getId())
                    .executeUpdate();

            em.createNativeQuery("insert into products (name, price, available_quantity, category_id, picture_name) " +
                    "values (:name, :price, :quantity, :categoryId, :pictureName)")
                    .setParameter("name", productData.getName())
                    .setParameter("price", productData.getPrice())
                    .setParameter("quantity", productData.getAvailableQuantity())
                    .setParameter("categoryId", categoryId)
                    .setParameter("pictureName", product.getPictureName())
                    .executeUpdate();
        } else
            em.createNativeQuery("update products set name=:name, price=:price, available_quantity=:quantity, " +
                    "category_id=:categoryId where id=:id", Product.class)
                    .setParameter("id", productData.getId())
                    .setParameter("categoryId", categoryId)
                    .setParameter("name", productData.getName())
                    .setParameter("price", productData.getPrice())
                    .setParameter("quantity", productData.getAvailableQuantity())
                    .executeUpdate();

        Optional<String> pictureName = Optional.ofNullable(productData.getPictureName());

        if (pictureName.isPresent())
            em.createNativeQuery("update products set picture_name=:pictureName where id=:id", Product.class)
                    .setParameter("id", productData.getId())
                    .setParameter("pictureName", pictureName.get())
                    .executeUpdate();
    }

    @Override
    public void deleteProduct(long id) {
        em.createNativeQuery("delete from products where id=:id", Product.class)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public Product getProduct(long id) {
        String sql = "select * from products where id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> {
            Product product = new Product();
            product.setId(id);
            product.setName(resultSet.getString("name"));
            product.setPrice(resultSet.getDouble("price"));
            product.setAvailableQuantity(resultSet.getInt("available_quantity"));
            product.setCategory(categoryRepository.getCategory(resultSet.getLong("category_id")));
            product.setComments(commentRepository.getAllComments(id));
            product.setPictureName(resultSet.getString("picture_name"));
            product.setImage(sftpClient.downloadFile(product.getPictureName()));

            return product;
        });
    }

    @Override
    public List<Product> getAllProductsByCategory(Long categoryId) {
        String sql = "select * from products where category_id = ? order by id";
        List<Product> productList = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[]{categoryId});
        for (Map<String, Object> row : rows) {
            Product product = new Product();
            product.setId((Long) row.get("id"));
            product.setName((String) row.get("name"));
            product.setAvailableQuantity((Integer) row.get("available_quantity"));
            product.setPrice((Double) row.get("price"));
            product.setCategory(categoryRepository.getCategory(categoryId));
            product.setPictureName((String) row.get("picture_name"));
            product.setImage(sftpClient.downloadFile(product.getPictureName()));
            productList.add(product);
        }
        return productList;
    }

    @Override
    public void decreaseQuantity(long id, int quantity) {
        em.createNativeQuery("update products set available_quantity=available_quantity-:quantity where id=:id", Product.class)
                .setParameter("id", id)
                .setParameter("quantity", quantity)
                .executeUpdate();
    }

    @Override
    public List<Product> productsMatching(String keyword) {
        String sql = "select * from products where name like ?";
        List<Product> productList = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[]{"%" + keyword + "%"});
        for (Map<String, Object> row : rows) {
            Product product = new Product();
            product.setId((Long) row.get("id"));
            product.setName((String) row.get("name"));
            product.setAvailableQuantity((Integer) row.get("available_quantity"));
            product.setPrice((Double) row.get("price"));
            product.setCategory(categoryRepository.getCategory((Long) row.get("category_id")));
            product.setPictureName((String) row.get("picture_name"));
            product.setImage(sftpClient.downloadFile(product.getPictureName()));
            productList.add(product);
        }

        return productList;
    }

    @Override
    public void appendComment(long productId, long commentId) {
        em.createNativeQuery("insert into product_comments(comment_id, product_id) values (:commentId, :productId)")
                .setParameter("commentId", commentId)
                .setParameter("productId", productId)
                .executeUpdate();
    }

    @Override
    public void removeComment(long productId, long commentId) {
        em.createNativeQuery("delete from product_comments where comment_id=:commentId and product_id=:productId")
                .setParameter("commentId", commentId)
                .setParameter("productId", productId)
                .executeUpdate();
    }

    @Override
    public List<Product> getProductsByPage(int pageNumber, int pageSize) {
        String sql = "select * from products order by id limit ? offset ?";
        List<Product> productList = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[]{pageSize, (pageNumber - 1) * pageSize});
        for (Map<String, Object> row : rows) {
            Product product = new Product();
            product.setId((Long) row.get("id"));
            product.setName((String) row.get("name"));
            product.setAvailableQuantity((Integer) row.get("available_quantity"));
            product.setPrice((Double) row.get("price"));
            product.setCategory(categoryRepository.getCategory((Long) row.get("category_id")));
            product.setPictureName((String) row.get("picture_name"));
            product.setImage(sftpClient.downloadFile(product.getPictureName()));
            productList.add(product);
        }
        return productList;
    }

    @Override
    public boolean pictureIsBoundToMoreThanOneProduct(String pictureName) {
        List<String> result = em.createNativeQuery("select * from products where picture_name = :pictureName")
                .setParameter("pictureName", pictureName)
                .getResultList();

        if(result.size() > 1)
            return true;

        return false;
    }

}