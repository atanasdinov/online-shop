package project.model.DTOS;

public class CategoryDTO {

    private long id;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
