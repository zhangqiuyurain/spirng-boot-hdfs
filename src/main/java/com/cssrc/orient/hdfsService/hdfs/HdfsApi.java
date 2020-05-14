package com.cssrc.orient.hdfsService.hdfs;

import com.cssrc.orient.hdfsService.exception.GlobalException;
import com.cssrc.orient.hdfsService.util.tool.Base64;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.security.PrivilegedExceptionAction;
import java.text.DecimalFormat;

public class HdfsApi {

    private final Logger LOG = LoggerFactory.getLogger(HdfsApi.class);

    private static String uri;

    // Hadoop的用户和组信息
    private static UserGroupInformation ugi;

    //
    private static FileSystem fs;

    private static Configuration conf;


    /**
     * 根据conf和user构建api
     *
     * @param conf
     * @param user
     * @throws IOException
     * @throws InterruptedException
     */
    public HdfsApi(Configuration conf, String user) throws IOException, InterruptedException {
        if (null != conf) {
            this.conf = conf;
        } else {
            this.conf = new Configuration();
        }
        UserGroupInformation.setConfiguration(conf);
        if (StringUtils.isNotBlank(user)) {
            // 远程创建用户
            this.ugi = UserGroupInformation.createRemoteUser(user);
        } else {
            // 获取当前用户
            this.ugi = UserGroupInformation.getCurrentUser();
        }
        initFileSystem();
    }

    /**
     * 上传本地文件到HDFS
     *
     * @param srcPath   源文件路径
     * @param dstPath   目标文件路径
     * @param overwrite 若上传文件已存在，是否覆盖文件
     */
    public boolean uploadFile(final String srcPath, final String dstPath, final boolean overwrite) throws Exception {
        return execute(new PrivilegedExceptionAction<Boolean>() {
            public Boolean run() throws Exception {
                boolean flag = false;
                if (StringUtils.isBlank(dstPath)) {
                    throw new GlobalException(
                            "Dest Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS");
                }
                if (StringUtils.isBlank(srcPath)) {
                    throw new GlobalException(
                            "Src Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS");
                }
                FSDataOutputStream fsDataOutputStream = null;
                FileInputStream inputStream = null;
                try {
                    fsDataOutputStream = fs.create(new Path(dstPath), overwrite);
                    inputStream = new FileInputStream(srcPath);
                    IOUtils.copy(inputStream, fsDataOutputStream);
                    flag = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(null != fsDataOutputStream) {
                        fsDataOutputStream.close();
                        fsDataOutputStream = null;
                    }
                    if(null != inputStream) {
                        inputStream.close();
                        inputStream = null;
                    }
                }
                return flag;
            }
        });
    }

    /**
     * 按照字节流上传文件到HDFS中
     *
     * @param in        源文件字节流
     * @param dstPath   目标文件路径
     * @param overwrite 若上传文件已存在，是否覆盖文件
     * @return
     * @throws Exception
     */
    public boolean uploadFile(final InputStream in, final String dstPath, final boolean overwrite) throws IOException, InterruptedException {
        return execute(new PrivilegedExceptionAction<Boolean>() {
            public Boolean run() throws Exception {
                boolean flag = false;
                if (StringUtils.isBlank(dstPath)) {
                    throw new GlobalException(
                            "Dest Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS");
                }
                OutputStream os = null;
                try {
                    os = fs.create(new Path(dstPath), overwrite);
                    // IOUtils.copy(in, os);
                    org.apache.hadoop.io.IOUtils.copyBytes(in, os, 2048, true);
                    flag = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if( null != os) {
                        os.close();
                        os = null;
                    }
                }
                return flag;
            }
        });

    }

    /**
     * 从 HDFS文件系统上 下载文件到指定destPath路径下
     * TODO 待校验
     *
     * @param srcFile  hdfs上文件路径
     * @param destPath
     * @throws InterruptedException
     * @throws IOException
     */
    public boolean downLoadFile(final String srcFile, final String destPath) throws IOException, InterruptedException {

        return execute(new PrivilegedExceptionAction<Boolean>() {
            public Boolean run() throws Exception {
                boolean flag = false;
                // 源路径
                Path sPath;
                if (StringUtils.isNotBlank(uri)) {
                    sPath = new Path(uri + "/" + srcFile);
                } else {
                    sPath = new Path(srcFile);
                }

                /**
                 * 本地路径或者Linux下路径
                 */
                Path dstPath = new Path(destPath);
                try {
                    // fs.copyToLocalFile(sPath, dstPath); // 这个方法会报空指针错误
                    fs.copyToLocalFile(false, sPath, dstPath, true);
                    System.out.println("<" + srcFile + ">文件下载至：" + destPath);
                    flag = true;
                } catch (IOException e) {
                    System.err.println(e);
                    return false;
                }
                return flag;
            }
        });
    }

    /**
     * 从HDFS中读取文件流写入到本地文件
     * TODO 待校验
     *
     * @param srcFile
     * @param response
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean downLoadFile(final String srcFile, final HttpServletResponse response) throws IOException, InterruptedException {
        return execute(new PrivilegedExceptionAction<Boolean>() {
            public Boolean run() throws Exception {
                boolean flag = false;
                // 源路径
                Path sPath;
                if (StringUtils.isNotBlank(uri)) {
                    sPath = new Path(uri + "/" + srcFile);
                } else {
                    sPath = new Path(srcFile);
                }

                String fileName = srcFile.substring(srcFile.lastIndexOf("/") + 1);
                String backFileName = fileName.substring(fileName.indexOf("_") + 1);
                response.setContentType(new MimetypesFileTypeMap().getContentType(new File(fileName)));
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(backFileName, "UTF-8"));
                try {
                    InputStream in = fs.open(sPath);
                    byte[] data = new byte[1024];
                    OutputStream out = response.getOutputStream();
                    while (in.read(data) != -1) {
                        out.write(data);
                    }
                    out.flush();
                    in.close();
                    out.close();
                    flag = true;
                } catch (Exception e) {
                    System.err.println(e);
                    return flag;
                }
                return flag;
            }
        });
    }

    /**
     * 下载文件
     *
     * @param request
     * @param response
     * @param srcFile
     * @param fileName
     * @throws IOException
     * @throws InterruptedException
     */
    public void downLoadFile(final HttpServletRequest request, final HttpServletResponse response, final String srcFile, final String fileName) throws IOException, InterruptedException {
        execute(new PrivilegedExceptionAction<Void>() {
            public Void run() throws IOException, InterruptedException {
                OutputStream outp = null;
                // 判断文件是否存在
                boolean isExistFile = existFile(srcFile);
                if (isExistFile) {
                    response.setContentType("APPLICATION/OCTET-STREAM");
                    String filedisplay = fileName;
                    if (fileName.indexOf("/") >= 0) {
                        filedisplay = fileName.substring(fileName.lastIndexOf("/") + 1);
                    } else if (fileName.indexOf("\\") >= 0) {
                        filedisplay = fileName.substring(fileName.lastIndexOf("\\") + 1);
                    }
                    String agent = (String) request.getHeader("USER-AGENT");
                    if (agent != null && agent.indexOf("MSIE") == -1) {
                        String enableFileName = "=?UTF-8?B?"
                                + (new String(Base64.getBase64(filedisplay))) + "?=";
                        response.setHeader("Content-Disposition",
                                "attachment; filename=" + enableFileName);
                    } else {
                        filedisplay = URLEncoder.encode(filedisplay, "utf-8");
                        response.addHeader("Content-Disposition",
                                "attachment;filename=" + filedisplay);
                    }
                    // 源路径
                    Path sPath;
                    if (StringUtils.isNotBlank(uri)) {
                        sPath = new Path(uri + "/" + srcFile);
                    } else {
                        sPath = new Path(srcFile);
                    }
                    InputStream in = null;
                    try {
                        in = fs.open(sPath);
                        outp = response.getOutputStream();
                        byte[] data = new byte[1024];
                        while (in.read(data) != -1) {
                            outp.write(data);
                        }
                        outp.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (in != null) {
                            in.close();
                            in = null;
                        }
                        if (outp != null) {
                            outp.close();
                            outp = null;
                            response.flushBuffer();
                        }
                    }
                } else {
                    outp.write("文件不存在!".getBytes("GBK"));
                }
                return null;
            }
        });
    }


    /**
     * 删除文件或者目录
     * TODO 此方法暂时不考虑回收站问题
     *
     * @param path
     * @param recursive 是否递归删除：如果path是目录，则该参数设置为true，否则会抛出异常
     *                  // @param skiptrash 是否删除的时候，将文件或者目录放入回收站
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean deleteFile(final String path, final boolean recursive) throws IOException, InterruptedException {
        return execute(new PrivilegedExceptionAction<Boolean>() {
            public Boolean run() throws Exception {
                boolean isExistFile = existFile(path);
                if (isExistFile) {
                    try {
                        Path dPath;
                        String destPath = "";
                        if (StringUtils.isNotBlank(uri)) {
                            destPath = uri + "/" + path;
                            dPath = new Path(destPath);
                        } else {
                            destPath = path;
                            dPath = new Path(path);
                        }
                        return fs.delete(dPath, recursive);
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getClass() + "," + e.getMessage());
                    } catch (IOException e) {
                        System.err.println(e.getClass() + "," + e.getMessage());
                    }
                } else {
                    throw new GlobalException(
                            "Src Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS");
                }
                return false;
            }
        });
    }

    /**
     * 复制文件
     *     从srcPath目录复制到dest目录
     *
     * @param srcPath         源路径
     * @param destPath        目标路径
     * @param isDeleteSrcFile 是否删除源文件
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws GlobalException
     */
    public boolean copyFile(final String srcPath, final String destPath, final boolean isDeleteSrcFile) throws IOException, InterruptedException, GlobalException {
        boolean flag = execute(new PrivilegedExceptionAction<Boolean>() {
            public Boolean run() throws Exception {
                return FileUtil.copy(fs, new Path(uri + "/" + srcPath), fs, new Path(uri + "/" + destPath), isDeleteSrcFile, conf);
            }
        });
        if (!flag) {
            throw new GlobalException("HDFS Can't copy source file from" + srcPath + " to " + destPath);
        }
        return flag;
    }


    /**
     * 文件或者目录重命名
     *
     * @param srcPath 源文件路径
     * @param dstPath 目标文件路径
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean rename(final String srcPath, final String dstPath) throws IOException, InterruptedException {
        return execute(new PrivilegedExceptionAction<Boolean>() {
            public Boolean run() throws Exception {
                boolean flag = false;
                try {

                    Path sPath;
                    Path dPath;

                    if (StringUtils.isNotBlank(uri)) {
                        sPath = new Path(uri + "/" + srcPath);
                        dPath = new Path(uri + "/" + dstPath);
                    } else {
                        sPath = new Path(srcPath);
                        dPath = new Path(dstPath);
                    }
                    if (!fs.exists(sPath)) {
                        System.err.println("The File <" + srcPath + "> does not exist!");
                        return false;
                        // throw new GlobalException(sPath + "(The file does not exist!)");
                    }

                    if (sPath.getName().equals(dPath.getName())) {
                        flag = true;
                    } else {
                        flag = fs.rename(sPath, dPath);
                    }

                    System.out.println(srcPath + " rename to " + dstPath + ",成功");
                } catch (IOException e) {
                    System.err.println(srcPath + " rename to " + dstPath + " error: " + e.getMessage());
                }

                return flag;
            }
        });
    }

    /**
     * 打开文件 FSDataInputStream继承DataInputStream，并实现了Seekable等接口
     *
     * @param path
     *            path
     * @return input stream
     * @throws IOException
     * @throws InterruptedException
     */
    public FSDataInputStream open(final String path) throws IOException, InterruptedException {
        return execute(new PrivilegedExceptionAction<FSDataInputStream>() {
            public FSDataInputStream run() throws Exception {
                return fs.open(new Path(uri + "/" + path));
            }
        });
    }


    /**
     * 文件系统FileSystem对象实例化
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void initFileSystem() throws IOException, InterruptedException {
        this.fs = execute(new PrivilegedExceptionAction<FileSystem>() {
            public FileSystem run() throws Exception {
                return FileSystem.get(conf);
            }
        });
        if (StringUtils.isBlank(uri)) {
            this.uri = conf.get("fs.default.name");
            if (uri.equals("file:///")) {
                this.uri = "C:";
            }
        }
    }

    /**
     * 释放fs
     *
     * @throws IOException
     */
    public void fsClose() throws IOException {
        fs.close();
    }


    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean existFile(final String filePath) throws IOException, InterruptedException {
        return execute(new PrivilegedExceptionAction<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                boolean flag = false;
                if (StringUtils.isEmpty(filePath)) {
                    return flag;
                }
                try {
                    Path path;
                    if (StringUtils.isNotBlank(uri)) {
                        path = new Path(uri + "/" + filePath);
                    } else {
                        path = new Path(filePath);
                    }
                    // 如果文件存在，返回true
                    if (fs.exists(path)) {
                        flag = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return flag;
            }
        });
    }


    /**
     * 获取HDFS的home目录
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public Path getHomeDir() throws IOException, InterruptedException {
        return execute(new PrivilegedExceptionAction<Path>() {
            public Path run() throws Exception {
                return fs.getHomeDirectory();
            }
        });
    }


    /**
     * 获取HDFS文件系统的状态（存储情况）
     *
     * @return fs的状态
     * @throws Exception
     */
    public synchronized FsStatus getStatus() throws Exception {
        return execute(new PrivilegedExceptionAction<FsStatus>() {
            public FsStatus run() throws IOException {
                FsStatus status = fs.getStatus();
                System.out.println("容量：" + getByteToSize(status.getCapacity()));
                System.out.println("已用：" + getByteToSize(status.getUsed()));
                System.out.println("剩余：" + getByteToSize(status.getRemaining()));
                return status;
            }
        });
    }

    /**
     * 字节大小转文件大小GB、MB、KB
     *
     * @param size
     * @return
     */
    public String getByteToSize(long size) {

        StringBuffer bytes = new StringBuffer();
        // 保留两位有效数字
        DecimalFormat format = new DecimalFormat("###.00");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    /**
     * 在HDFS上，使用ugi的doAs执行action，记录异常
     *
     * @param action 策略对象
     * @param <T>    run方法里面返回值的类型
     * @return 出现异常情况。每个实现 PrivilegedExceptionAction 的类都应该记录其 run 方法能够抛出的异常。
     * @throws IOException
     * @throws InterruptedException
     */
    public <T> T execute(PrivilegedExceptionAction<T> action) throws IOException, InterruptedException {
        return execute(action, false);
    }

    /**
     * 在HDFS上，使用ugi的doAs执行action，记录异常 方法重载，第二个参数alwaysRetry不设置，默认重试三次异常后，跳出循环
     *
     * @param action 策略对象
     * @param <T>    result type
     * @return result of operation
     * @throws IOException
     * @throws InterruptedException
     */
    public <T> T execute(PrivilegedExceptionAction<T> action, boolean alwaysRetry)
            throws IOException, InterruptedException {

        T result = null;

        /**
         * 由于HDFS-1058，这里采用了重试策略。HDFS可以随机抛出异常 IOException关于从DN中检索块(如果并发读写)
         * 在特定文件上执行(参见HDFS-1058的详细信息)。
         */
        int tryNumber = 0;
        boolean succeeded = false;
        do {
            tryNumber += 1;
            try {
                // doAs中执行的操作都是以proxyUser用户的身份执行
                result = ugi.doAs(action);
                succeeded = true;
            } catch (IOException ex) {
                if (!Strings.isNullOrEmpty(ex.getMessage()) && !ex.getMessage().contains("无法获取块的长度：")) {
                    throw ex;
                }

                // 尝试超过>=3次，抛出异常，do while 退出
                if (tryNumber >= 3) {
                    throw ex;
                }
                LOG.info("HDFS抛出'IOException:无法获得块长度'的异常. " + "再次尝试... 尝试 #" + (tryNumber + 1));
                LOG.error("再次尝试: " + ex.getMessage(), ex);
                Thread.sleep(1000); // 1s后再试
            }
        } while (!succeeded);
        return result;
    }
}
