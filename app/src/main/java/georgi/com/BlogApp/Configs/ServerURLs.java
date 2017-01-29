package georgi.com.BlogApp.Configs;

public class ServerURLs {

    private static final String IP_ADDRESS = "http://192.168.0.102:8080";
    private static final String SERVER_URL = IP_ADDRESS + "/rest";

    public static final String LOGIN_URL = SERVER_URL + "/login";
    public static final String REGISTER_URL = SERVER_URL + "/register";
    public static final String LOGOUT_URL = SERVER_URL + "/logout";
    public static final String AUTHENTICATION_URL = SERVER_URL + "/authentication";

    public static final String ACCOUNT_URL = SERVER_URL + "/account";
    public static final String EDIT_ACCOUNT_URL = SERVER_URL + "/account/edit";
    public static final String AUTH_USER_POSTS_URL = SERVER_URL + "/account/posts";

    public static final String POST_URL = SERVER_URL + "/posts/";
    public static final String LATEST10POSTS_URL = SERVER_URL + "/posts/latest10";

    public static final String USER_IMAGES_URL = "http://192.168.0.102:8080/res/images/";
    public static final String POSTS_IMAGES_URL = "http://192.168.0.102:8080/res/image-post/";

    public static final String DEFAULT_USER_IMG = IP_ADDRESS + "/res/images/default-image.png";
    public static final String DEFAULT_POST_IMG = IP_ADDRESS + "/res/image-post/default-image.png";

}
