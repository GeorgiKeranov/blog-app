package georgi.com.BlogApp.Configs;


// All of the server urls.
public class ServerURLs {

    private static final String IP_ADDRESS = "http://192.168.0.101:8080";
    private static final String SERVER_URL = IP_ADDRESS + "/rest";

    public static final String LOGIN_URL = SERVER_URL + "/login";
    public static final String REGISTER_URL = SERVER_URL + "/register";
    public static final String LOGOUT_URL = SERVER_URL + "/logout";
    public static final String AUTHENTICATION_URL = SERVER_URL + "/authentication";

    public static final String ACCOUNT_URL = SERVER_URL + "/account";
    public static final String EDIT_ACCOUNT_URL = SERVER_URL + "/account/edit";

    public static final String CREATE_POST_URL = SERVER_URL + "/create-post";
    public static final String POST_URL = SERVER_URL + "/posts/";

    public static final String LATEST_USER_POSTS_URL = SERVER_URL + "/your-posts/latest";
    public static final String UPDATE_USER_5POSTS_URL = SERVER_URL + "/your-posts";
    public static final String LATEST_POSTS_URL = SERVER_URL + "/posts/latest";
    public static final String UPDATE_5POSTS_URL = SERVER_URL + "/posts";

    public static final String USER_IMAGES_URL = IP_ADDRESS + "/res/images/";
    public static final String POSTS_IMAGES_URL = IP_ADDRESS + "/res/image-post/";

    public static final String DEFAULT_USER_IMG = IP_ADDRESS + "/res/images/default-image.png";
    public static final String DEFAULT_POST_IMG = IP_ADDRESS + "/res/image-post/default-image.png";

}
