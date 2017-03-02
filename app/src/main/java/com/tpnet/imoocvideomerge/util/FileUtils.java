
package com.tpnet.imoocvideomerge.util;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.tpnet.imoocvideomerge.model.IOnProgressListener;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

public final class FileUtils {


    public static boolean checkSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    public static String getRootPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    public static void debugFile(Context mContext, String str) throws Exception {
        File file = FileUtils.getSaveFile(mContext.getPackageName()
                        + File.separator + "debug",
                SystemTool.getDataTime("yyyy-MM-dd-HH-mm-ss") + ".log");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(str);
        bw.flush();
        bw.close();


    }


    public static void saveFileCache(byte[] fileData, String folderPath,
                                     String fileName) {
        File folder = new File(folderPath);
        folder.mkdirs();
        File file = new File(folderPath, fileName);
        ByteArrayInputStream is = new ByteArrayInputStream(fileData);
        OutputStream os = null;
        if (file.exists())
            return;
        try {
            file.createNewFile();
            os = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = is.read(buffer))) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeIO(new Closeable[]{is, os});
        }
    }

    public static File getSaveFile(String folderPath, String fileNmae) {
        File file = new File(getSavePath(folderPath) + File.separator
                + fileNmae);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }

    public static File getSaveFolder(String folderName) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsoluteFile()
                + File.separator
                + folderName
                + File.separator);
        file.mkdirs();
        return file;
    }

    public static final byte[] input2byte(InputStream inStream) {
        if (inStream == null)
            return null;
        byte[] in2b = null;
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in2b = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(new Closeable[]{swapStream});
        }
        return in2b;
    }

    public static File uri2File(Activity aty, Uri uri) {
        if (SystemTool.getSDKVersion() < 11) {
            String[] proj = {"_data"};

            Cursor actualimagecursor = aty.managedQuery(uri, proj, null, null,
                    null);
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow("_data");
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor
                    .getString(actual_image_column_index);
            return new File(img_path);
        }

        String[] projection = {"_data"};
        CursorLoader loader = new CursorLoader(aty, uri, projection, null,
                null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return new File(cursor.getString(column_index));
    }

    public static boolean copyFile(File from, File to) {
        if ((from == null) || (!from.exists()))
            return false;
        if (to == null)
            return false;
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(from);
            if (!to.exists()) {
                to.createNewFile();
            }
            os = new FileOutputStream(to);
            return copyFileFast(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeIO(new Closeable[]{is, os});
        }
        return false;
    }

    public static Boolean copyFileFast(FileInputStream is, FileOutputStream os)
            throws IOException {
        FileChannel in = is.getChannel();
        FileChannel out = os.getChannel();
        in.transferTo(0L, in.size(), out);
        return true;
    }

    public static void closeIO(Closeable[] closeables) {
        if ((closeables == null) || (closeables.length <= 0))
            return;
        for (Closeable cb : closeables)
            try {
                if (cb != null) {
                    cb.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static boolean bitmapToFile(Bitmap bitmap, String filePath) {
        boolean isSuccess = false;
        if (bitmap == null)
            return isSuccess;
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(filePath), 8192);
            isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 70, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeIO(new Closeable[]{out});
        }
        return isSuccess;
    }

    public static String readFile(String filePath) {
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream2String(is);
    }

    public static String readFile(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream2String(is);
    }

    public static String readFileFromAssets(Context context, String name) {
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream2String(is);
    }

    public static String inputStream2String(InputStream is) {
        if (is == null)
            return null;
        StringBuilder resultSb = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            resultSb = new StringBuilder();
            String len;
            while ((len = br.readLine()) != null) {
                resultSb.append(len);
            }
        } catch (Exception localException) {
        } finally {
            closeIO(new Closeable[]{is});
        }
        return ((resultSb == null) ? null : resultSb.toString());
    }

    public static String getMimeType(String fileUrl) throws IOException,
            MalformedURLException {
        String type = null;
        URL u = new URL(fileUrl);
        URLConnection uc = null;
        uc = u.openConnection();
        type = uc.getContentType();
        return type;
    }


    public static Boolean isFileExist(String path) {
        File file = new File(path);
        LogUtil.e(file.getAbsolutePath());

        return file.exists();
    }


    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(File file) {

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {

                if (aFileList.isDirectory()) {
                    //如果是文件夹就递归继续获取
                    size = size + getFolderSize(aFileList);
                } else {
                    //获取文件的长度
                    size = size + aFileList.length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }


    /**
     * 删除指定目录下文件及目录
     *
     * @param filePath 文件路径
     * @return
     */
    public static void deleteFolderFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath());
                    }
                }

                if (file.isDirectory()) {// 目录

                    if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                        file.delete();
                    }

                } else {// 如果是文件，删除
                    file.delete();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }


    /**
     * 获取剩余空间的大小
     *
     * @return
     */
    public static int getSDAvailablePercent() {
        StatFs statfs = new StatFs(getRootPath());


        long allSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //获取block的SIZE
            long blocSize = statfs.getBlockSizeLong();
            //获取BLOCK数量
            long totalBlocks = statfs.getBlockCountLong();

            allSize = blocSize * totalBlocks;
        } else {
            //获取block的SIZE
            long blocSize = statfs.getBlockSize();
            //获取BLOCK数量
            long totalBlocks = statfs.getBlockCount();

            allSize = blocSize * totalBlocks;
        }

        return 100 - (int) (getSDAvailableSize() / (double) allSize * 100);

    }


    /**
     * 获取剩余空间 的百分比
     *
     * @return
     */
    public static long getSDAvailableSize() {
        StatFs statfs = new StatFs(getRootPath());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //获取block的SIZE
            long blocSize = statfs.getBlockSizeLong();
            //获取BLOCK数量
            long availableBlocks = statfs.getAvailableBlocksLong();

            return blocSize * availableBlocks;
        } else {
            //获取block的SIZE
            long blocSize = statfs.getBlockSize();
            //获取空闲的BLOCK数量
            long availableBlocks = statfs.getAvailableBlocks();

            return blocSize * availableBlocks;
        }
    }


    public static boolean copyFolder(File oldFile, File newFile) {
        try {
            newFile.mkdirs(); //如果文件夹不存在 则建立新文件夹
            String[] file = oldFile.list();
            File temp = null;
            for (String aFile : file) {

                temp = new File(oldFile.getAbsolutePath() + File.separator + aFile);

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newFile.getAbsolutePath() + File.separator + temp.getName());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldFile.getAbsolutePath() + File.separator + aFile, newFile.getAbsolutePath() + File.separator + aFile);
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();
            return false;

        }

    }


    public static boolean copyFolder(String oldPath, String newPath) {
        return copyFolder(new File(oldPath), new File(newPath));
    }

    public static boolean copyFolder(String oldPath, File newFile) {
        return copyFolder(new File(oldPath), newFile);
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static Boolean copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;   //文件总大小
            int byteread = 0;
            File oldfile = new File(oldPath);

            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(createNewFile(newPath,true));
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }

                fs.flush();
                fs.close();
                inStream.close();
                return true;
            } else {
                throw new Exception("源文件不存在");
            }
        } catch (Exception e) {
            LogUtil.e("复制单个文件操作出错" + e.getMessage());
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 复制单个文件带进度
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static Boolean copyFile(String oldPath, String newPath, IOnProgressListener<String> listener) {
        try {
            long bytesum = 0;   //文件总大小
            int byteread = 0;
            File oldfile = new File(oldPath);

            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件

                FileOutputStream fs = new FileOutputStream(createNewFile(newPath,true));

                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                    listener.progress(oldPath,0,0,(int)((float)bytesum/oldfile.length() * 100));
                }
                fs.flush();
                fs.close();
                inStream.close();
                listener.success(null,null,null);
                return true;
            } else {
                throw new Exception("源文件不存在");
            }
        } catch (Exception e) {
            LogUtil.e("复制单个进度文件操作出错" + e.getMessage());
            e.printStackTrace();
            listener.success(null,null,new Exception("复制单个进度文件操作出错" + e.getMessage()));
            return false;
        }

    }


    /**
     * 创建文件夹
     */
    public static File createFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
        return file;
    }


    /**
     * 分级建立文件夹
     *
     * @param path      路径
     * @param isFileEnd 最后是否文件，还是文件夹
     */
    public static void getDirectory(String path, Boolean isFileEnd) {
        //对SDpath进行处理，分层级建立文件夹
        String[] s = path.split(File.separator);

        for (int i = 1; i < (isFileEnd ? s.length - 1 : s.length); i++) {

            File file = new File(getRootPath() + s[i]);
            if (!file.exists()) {
                file.mkdir();
            }
        }

    }

    public static File createNewFile(String path,Boolean isFilend) {
        File newFile = new File(path);
        if (newFile.exists()) {
            return newFile;
        } else {
            getDirectory(path,isFilend);
            try {
                newFile.createNewFile();
                return newFile;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


}