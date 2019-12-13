package cn.zuel.wlyw.networkalbumclient.config;

public class MainConfig {
    // 服务器IP地址
//    private final static String SERVER_IP = "10.174.244.60";
    private final static String SERVER_IP = "10.169.92.125";
    // 拼接形成请求地址
    public final static String REQUEST_URL = "http://" + SERVER_IP + ":8080/";

    // 各个Servlet的请求地址
    public final static String USER_LOGIN_URL = REQUEST_URL + "user/login";
    public final static String USER_REGISTER_URL = REQUEST_URL + "user/register";
    public final static String ALBUM_GET_URL = REQUEST_URL + "album/getAlbums";
    public final static String GET_IMAGES_URL = REQUEST_URL + "image/getImages";
    public final static String UPLOAD_IMAGE_URL = REQUEST_URL + "image/uploadImages";
}
