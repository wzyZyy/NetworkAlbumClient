package cn.zuel.wlyw.networkalbumclient.config;

public class ServerConstantConfig {
    // 服务器IP地址
    private final static String SERVER_IP = "10.169.100.102";
//    private final static String SERVER_IP = "10.169.71.27";
    // 拼接形成请求地址
    public final static String REQUEST_URL = "http://" + SERVER_IP + ":8080/";

    // 各个Servlet的请求地址
    public final static String USER_LOGIN_URL = REQUEST_URL + "user/login";
    public final static String USER_REGISTER_URL = REQUEST_URL + "user/register";
    public final static String ALBUM_GET_URL = REQUEST_URL + "album/getAlbums";
    public final static String ALBUM_CREATE_URL = REQUEST_URL + "album/createAlbum";
    public final static String ALBUM_GET_SHARE_URL = REQUEST_URL + "album/getShareAlbums";
    public final static String ALBUM_DELETE_URL = REQUEST_URL + "album/deleteAlbum";
    public final static String GET_IMAGES_URL = REQUEST_URL + "image/getImages";
    public final static String DELETE_IMAGES_URL = REQUEST_URL + "image/deleteImage";
    public final static String UPLOAD_IMAGE_URL = REQUEST_URL + "image/uploadImage";
    public final static String GET_USER_URL = REQUEST_URL + "user/getInfo";
    public final static String MODIFY_USER_INFO_URL = REQUEST_URL + "user/modifyInfo";
}
