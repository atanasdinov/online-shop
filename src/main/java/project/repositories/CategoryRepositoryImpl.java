package project.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.model.entities.Category;

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
        em.persist(category);
    }

    @Override
    @Transactional
    public Category get(String name) {
        Category result = (Category) em
                .createNativeQuery("select * from categories where name=:name", Category.class)
                .setParameter("name", name).getSingleResult();

        return result;
    }

    @Override
    @Transactional
    public List<Category> all() {
        return (List<Category>) em
                .createNativeQuery("select * from categories", Category.class)
                .getResultList();
    }

    @Override
    public boolean doExist(String categoryName) {
        List resultList = em.createNativeQuery("select * from categories as c where c.name=:name", Category.class)
                .setParameter("name", categoryName)
                .getResultList();

        return resultList.size() != 0;
    }

}
