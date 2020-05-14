package com.cssrc.orient.hdfsService.util.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FtpFileUtil {

    public static final String UPLOAD_ROOT = "上传文件";
    public static final String IMPORT_ROOT = "导入文件";
    public static final String EXPORT_ROOT = "导出文件";
    public static final SimpleDateFormat pathFormat = new SimpleDateFormat("-yyyy-MM-dd-HHmmss-");
    public static final SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyyMMddHHmmss_");

    public static String getRelativeUploadPath(String root) {
        String subFolder = pathFormat.format(new Date()).replaceAll("-", "/");

        // TODO HDFS中的存储路径格式为：/folder1/floder2/test.txt
        return "/" + root + subFolder;
        // return File.separator + root + subFolder;

    }

    public static String getRelativeUploadPath(String root, Date date) {
        String subFolder = pathFormat.format(date).replaceAll("-", "/");

        // TODO HDFS中的存储路径格式为：/folder1/floder2/test.txt
        return "/" + root + subFolder;
        // return File.separator + root + subFolder;

    }
}
