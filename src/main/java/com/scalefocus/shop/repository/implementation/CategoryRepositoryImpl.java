package com.scalefocus.shop.repository.implementation;

import com.scalefocus.shop.model.entity.Category;
import com.scalefocus.shop.repository.specification.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    @PersistenceContext
    private EntityManager em;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager em) {
        this.jdbcTemplate = jdbcTemplate;
        this.em = em;
    }

    @Override
    public void createCategory(Category category) {
        em.createNativeQuery("insert into categories (name) values (:name)")
                .setParameter("name", category.getName())
                .executeUpdate();
    }

    @Override
    public Category getCategory(String name) {
        return jdbcTemplate.queryForObject("select * from categories where name = ?", new Object[]{name}, (resultSet, i) -> {
            Category category = new Category();
            category.setName(name);
            category.setId(resultSet.getLong("id"));

            return category;
        });
    }

    @Override
    public Category getCategory(Long id) {
        return jdbcTemplate.queryForObject("select * from categories where id = ?", new Object[]{id}, (resultSet, i) -> {
            Category category = new Category();
            category.setName(resultSet.getString("name"));
            category.setId(id);

            return category;
        });
    }

    @Override
    public boolean categoryExists(String name) {
        return !(em.createNativeQuery("select * from categories where name=:name")
                .setParameter("name", name)
                .getResultList()
                .isEmpty());
    }

    @Override
    public List<Category> getAllCategories() {
        return em.createNativeQuery("select * from categories", Category.class)
                .getResultList();
    }

    @Override
    public void deleteCategory(Long categoryId) {
        em.createNativeQuery("delete from categories where id=:categoryId")
                .setParameter("categoryId", categoryId)
                .executeUpdate();
    }

    @Override
    public void editCategory(Category category) {
        em.createNativeQuery("update categories set name=:categoryName where id=:categoryId")
                .setParameter("categoryId", category.getId())
                .setParameter("categoryName", category.getName())
                .executeUpdate();
    }


}
