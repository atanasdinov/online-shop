package project.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.model.entities.Category;
import project.repository.specification.CategoryRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public CategoryRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void persist(Category category) {
        em.createNativeQuery("insert into categories (name) values (:name)")
                .setParameter("name", category.getName())
                .executeUpdate();
    }

    @Override
    @Transactional
    public Category getCategory(String name) {
        Category result = (Category) em
                .createNativeQuery("select * from categories where name=:name", Category.class)
                .setParameter("name", name).getSingleResult();

        return result;
    }

    @Override
    @Transactional
    public List<Category> getAllCategories() {
        return (List<Category>) em
                .createNativeQuery("select * from categories", Category.class)
                .getResultList();
    }

    @Override
    public boolean doesExist(String categoryName) {
        return !(em.createNativeQuery("select * from categories c where c.name=:name", Category.class)
                .setParameter("name", categoryName)
                .getResultList()
                .isEmpty());
    }
}