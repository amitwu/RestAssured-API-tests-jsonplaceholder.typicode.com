package config;

public interface EndPoint {

    String USERS_PATH = "/users";
    String USERS_ID_PATH = "/users/{userid}";
    String POST_PATH = "/posts";
    String POST_USER_ID_PATH = "/posts/{userid}";
    String DELETE_USER_ID_PATH = "/posts/{id}";
    String COMMENTS_PATH = "/comments";


}
