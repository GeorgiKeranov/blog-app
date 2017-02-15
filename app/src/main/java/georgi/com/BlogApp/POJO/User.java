package georgi.com.BlogApp.POJO;


public class User {

    private String userUrl;

    private String firstName;

    private String lastName;

    private String email;

    private String profile_picture;

    public User() {}

    public User(String userUrl, String firstName, String lastName, String email) {
        this.userUrl = userUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
