package georgi.com.BlogApp.POJO;

import java.util.List;


public class Post {

    private Long id;

    private String title;

    private String icon;

    private String description;

    private String date;

    private List<Comment> comments;

    public Post(){

    }

    public Post(String title, String description, User author) {
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getDate() {
        return date;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    //TODO fix these.
    public String getSummaryTitle(){
        if(title.length() > 15) {
            String summary = title.substring(0, 15);
            return summary + "...";
        }
        return title;
    }

    public String getSummaryDesc(){
        if(description.length() > 325){
            String summary = description.substring(0, 325);
            return summary + "...";
        }
        return description;
    }

}
