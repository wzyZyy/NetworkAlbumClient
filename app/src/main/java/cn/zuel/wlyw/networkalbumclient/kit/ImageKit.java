package cn.zuel.wlyw.networkalbumclient.kit;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

import androidx.loader.content.CursorLoader;

public class ImageKit {
    private static final String TAG = "ImageKit";

    /**
     * 根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    public static String getRealPathFromUri(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            Log.d(TAG, "getRealPathFromUri: isDocumentUri");
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                Log.d(TAG, "getRealPathFromUri: isMediaDocument");
                // 使用':'分割
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Log.d(TAG, "getRealPathFromUri: isDownloadsDocument");
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.d(TAG, "getRealPathFromUri: content uri");
            // 如果是 content 类型的 Uri
//            filePath = getDataColumn(context, uri, null, null);
            filePath = getRealFilePathThroughCamera(context, uri);
            Log.d(TAG, "getRealPathFromUri: path: " + filePath);
        } else if ("file".equals(uri.getScheme())) {
            Log.d(TAG, "getRealPathFromUri: file uri");
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     *  相机拍照上传，生硬地获得图片地真实地址
     */
    public static String getRealFilePathThroughCamera(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        Log.d(TAG, "getRealFilePath: scheme ----->" + scheme);
        String realPath = null;
        if (scheme == null)
            realPath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            realPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        realPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        Log.d(TAG, "getRealFilePath: realPath------->" + realPath);
        if (TextUtils.isEmpty(realPath)) {
            if (uri != null) {
                String uriString = uri.toString();
                Log.d(TAG, "getRealFilePath: uriString------->" + uriString);
                int index = uriString.lastIndexOf("/");
                String imageName = uriString.substring(index);
                Log.d(TAG, "getRealFilePath: imageName--------->" + imageName);
                File storageDir;

                storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File file = new File(storageDir, imageName);
                if (file.exists()) {
                    realPath = file.getAbsolutePath();
                    realPath = realPath.replaceFirst("files/Pictures", "cache");
                    Log.d(TAG, "getRealFilePath: file exist----------->" + realPath);
                } else {
                    storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File file1 = new File(storageDir, imageName);
                    realPath = file1.getAbsolutePath();
                    realPath = realPath.replaceFirst("files/Pictures", "cache");
                    Log.d(TAG, "getRealFilePath: file not exist----------->" + realPath);
                }
            }
        }
        Log.d(TAG, "getRealFilePath: 结束--------->realPath: " + realPath);
        return realPath;
    }
}
