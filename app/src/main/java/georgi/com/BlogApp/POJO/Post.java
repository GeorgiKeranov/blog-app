package georgi.com.BlogApp.POJO;

import java.util.List;


public class Post {

    private Long id;

    private String title;

    private String icon;

    private String description;

    private String date;

    public Post(){

    }

    public Post(String title, String description) {
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


    public String getSummaryTitle(){

        if(title.length() > 40) {
            String summary = title.substring(0, 40);
            return summary + "...";
        }
        return title;
    }

    public String getSummaryDesc(){

        if(description.length() > 50){
            String summary = description.substring(0, 50);
            return summary + "...";
        }
        return description;
    }

}
