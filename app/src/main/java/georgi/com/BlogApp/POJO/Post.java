package georgi.com.BlogApp.POJO;

import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_POST_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.POSTS_IMAGES_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.POST_URL;

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


    // This method is used when we have limited space on the screen.
    // If the title is more than 40 characters it is returned with only
    // 40 characters and "..." .
    public String getSummaryTitle(){

        if(title.length() > 40) {
            String summary = title.substring(0, 40);
            return summary + "...";
        }
        return title;
    }

    // This method is used when we have limited space on the screen.
    // And we substring the original description to smaller one.
    public String getSummaryDesc(){

        String summaryDesc = description;

        // If summary desc contains "\n" it is split in array
        // and summaryDesc is the first line of the description.
        if(description.contains("\n")){
            String[] descLines = description.split("\n");
            summaryDesc = descLines[0];
        }

        // If summaryDecs is more than 50 characters
        // We substring it to 50 characters.
        if(summaryDesc.length() > 50){
            summaryDesc = description.substring(0, 50);
            return summaryDesc + "...";
        }

        return summaryDesc;
    }


    // This method formats the icon to url for that picture(icon).
    public String getPictureUrl() {

        // "no" means that there is not picture to that Post
        // so we are returning the default image for post URL.
        if(icon.equals("no")) return DEFAULT_POST_IMG;

        // If there is a picture we are adding the server url for pictures
        // then the picture name and returning created URL.
        else return POSTS_IMAGES_URL + icon;
    }

}
