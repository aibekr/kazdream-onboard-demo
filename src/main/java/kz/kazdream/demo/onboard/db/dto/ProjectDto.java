package kz.kazdream.demo.onboard.db.dto;

import java.util.Date;

public class ProjectDto extends AbstractDto {
    private int id;
    private String name;
    private int[] taskIds;
    private Date createdDate;

    public ProjectDto() {
    }

    public int getId() {
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int[] getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(int[] taskIds) {
        this.taskIds = taskIds;
    }
}
