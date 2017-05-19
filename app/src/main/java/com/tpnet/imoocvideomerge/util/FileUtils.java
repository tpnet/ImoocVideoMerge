
package com.tpnet.imoocvideomerge.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.tpnet.imoocvideomerge.model.face.IOnProgressListener;
import com.tpnet.imoocvideomerge.ui.MainActivity;
import com.tpnet.imoocvideomerge.ui.SettingFragment;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {


    public static boolean checkSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * 获取内置内存卡路径
     *
     * @return
     */
    public static String getInnerRootPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取当前选择保存的内存卡路径
     *
     * @return
     */
    public static String getSaveRootPath(Context context) {
        return DataHelper.getStringSF(context, SettingFragment.STORAGE_PATH_KEY) + File.separator;
    }

    /**
     * 获取当前选择保存的内存卡路径，默认内置
     *
     * @return
     */
    public static String getSaveRootPathDefault(Context context) {
        String path = DataHelper.getStringSF(context, SettingFragment.STORAGE_PATH_KEY);
        if (TextUtils.isEmpty(path)) {
            path = getInnerRootPath() + File.separator;
            DataHelper.SetStringSF(context, SettingFragment.STORAGE_PATH_KEY, path);
        }
        return path;
    }

    /**
     * 获取当前选择显示的内存卡路径
     *
     * @return
     */
    public static String getShowRootPath(Context context) {
        return DataHelper.getStringSF(context, MainActivity.SP_SELECT_STROAGE) + File.separator;
    }

    /**
     * 获取当前选择显示的内存卡路径,默认内存
     *
     * @return
     */
    public static String getShowRootPathDefault(Context context) {
        String path = DataHelper.getStringSF(context, MainActivity.SP_SELECT_STROAGE);
        if (TextUtils.isEmpty(path)) {
            path = getInnerRootPath() + File.separator;
            DataHelper.SetStringSF(context, MainActivity.SP_SELECT_STROAGE, path);
        }
        return path;
    }

    /**
     * 获取外置内存卡路径
     *
     * @param context
     * @return
     */
    public static String getOutRootPath(Context context) {
        List<String> list = getExtSDCardPath();
        if (list.size() > 0) {
            return list.get(0) + File.separator;
        } else {
            return "";
        }
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


    public static String readFile(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
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
    public static int getSDAvailablePercent(Context context) {
        StatFs statfs = new StatFs(getShowRootPathDefault(context));


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

        return 100 - (int) (getSDAvailableSize(context) / (double) allSize * 100);

    }


    /**
     * 获取sd卡剩余空间
     *
     * @return 字节byte
     */
    public static long getSDAvailableSize(Context context) {
        StatFs statfs = new StatFs(getShowRootPathDefault(context));

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


/*    public static boolean copyFolder(File oldFile, File newFile) {
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
    }*/


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

        String cachePath = File.separator;

        for (int i = 1; i < (isFileEnd ? s.length - 1 : s.length); i++) {

            cachePath += File.separator + s[i];

            File file = new File(cachePath);
            if (!file.exists()) {
                file.mkdir();
            }
        }

    }

    /**
     * 创建文件，目录不存在会自动创建目录
     *
     * @param path
     * @param isFilend
     * @return
     */
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


    /**
     * 获取外置SD卡路径
     *
     * @return 应该就一条记录或空
     */
    public static List<String> getExtSDCardPath() {
        List<String> lResult = new ArrayList<>();

        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lResult;
    }


    /**
     * 打开指定文件夹
     *
     * @param context
     * @param
     */
    public static void openFolder(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.parse(path), "file/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        context.startActivity(intent);
    }
}