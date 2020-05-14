package com.cssrc.orient.hdfsService.business;


import com.cssrc.orient.hdfsService.hdfs.HdfsApi;
import com.cssrc.orient.hdfsService.model.FileOperationVO;
import com.cssrc.orient.hdfsService.model.FileVO;
import com.cssrc.orient.hdfsService.model.entity.CwmFilePO;
import com.cssrc.orient.hdfsService.model.repository.CwmFileRepository;
import com.cssrc.orient.hdfsService.util.image.ImageUtils;
import com.cssrc.orient.hdfsService.util.tool.FtpFileUtil;
import com.cssrc.orient.hdfsService.util.tool.UUIDGenerator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FileStorageBusiness {

    @Autowired
    private Configuration conf;

    @Value("root")
    private String user;

    @Autowired
    CwmFileRepository cwmFileRepository;

    public FileVO saveFile(String fileName, Long fileSize, InputStream in, String modelId, String dataId, String userId) {
        FileVO fileVO = new FileVO();
        boolean flag = false;
        // String fileName = multipartFile.getOriginalFilename();
        // long fileSize = multipartFile.getSize();
        Date date = new Date();
        // 格式：/上传文件/2020/04/25/095412/
        String folder = FtpFileUtil.getRelativeUploadPath(FtpFileUtil.UPLOAD_ROOT, date);
        // 文件后缀
        String suffix = getSuffix(fileName);
        String finalName = UUIDGenerator.getUUID() + "." + suffix;
        // 文件存储相对路径
        String fileLocation = folder + finalName;
        // 上传到HDFS
        flag = uploadFileToHDFS(in, fileLocation);
        if (flag) {
            CwmFilePO cwmFilePO = new CwmFilePO();
            cwmFilePO.setTableid(modelId);
            cwmFilePO.setDataid(dataId);
            cwmFilePO.setUploadDate(date);
            cwmFilePO.setUploadUserId(userId);
            cwmFilePO.setFinalname(finalName);
            cwmFilePO.setFiletype(suffix);
            cwmFilePO.setFilename(fileName);
            cwmFilePO.setFilelocation(fileLocation);
            cwmFilePO.setFilecatalog("COMMON");
            cwmFilePO.setFilesize(fileSize);
            cwmFileRepository.save(cwmFilePO);
            // 返回值
            // TODO fileId改为ong类型
            long fileId = cwmFilePO.getFileid();
            fileVO.setId(fileId);
            fileVO.setName(fileName);
            fileVO.setSuffix(suffix);
        }
        return fileVO;
    }

    public FileOperationVO getFile(Long fileId, String operateType) {
        CwmFilePO filePO = getCwmFile(fileId);
        if( null != filePO) {
            String fileName = filePO.getFilename();
            String finalName = filePO.getFinalname();
            String fileLocation = filePO.getFilelocation();
            FileOperationVO fileOperationVO = new FileOperationVO();
            if("preview".equals(operateType)) {
                // TODO 预览功能处理
                String fileType = filePO.getFiletype();
                if(imageTypes.contains(fileType)) {
                    // 图片缩略图的相对路径
                    String newFilePath = fileLocation.substring(0, fileLocation.indexOf(fileType) - 1 ) + "_s" + "." + fileType;
                    boolean flag = isExitsFileInHDFS(newFilePath);
                    if(!flag) {
                        try {
                            HdfsApi hdfsApi = new HdfsApi(conf, user);
                            FSDataInputStream fsDataInputStream = hdfsApi.open(fileLocation);
                            // 暂存在本地的图片文件
                            String localPath = System.getProperty("user.dir") + File.separator +
                                    finalName.substring(0, finalName.indexOf(fileType) - 1 ) + "_s" + "." + fileType;
                            // 图片文件缩放处理
                            ImageUtils.zoomImageScale(fsDataInputStream,localPath,200,fileType);
                            // 将生成的图片缩放图上传到与原图片相同路径下
                            hdfsApi.uploadFile(localPath, newFilePath, false);
                            hdfsApi.fsClose();
                            // 删除在本地生成的图片缩放图
                            File file = new File(localPath);
                            if(!file.isDirectory()) {
                                file.delete();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    fileOperationVO.setFileLocation(newFilePath);
                    fileOperationVO.setFileName(fileName);

                }else if(videoTypes.contains(fileType)) {

                }else if(voiceTypes.contains(fileType)) {

                }else {
                    //TODO 预览功能若是非图片音频视频文件,则直接返回null
                    return null;
                }
            }else {
                fileOperationVO.setFileName(fileName);
                fileOperationVO.setFileLocation(fileLocation);
            }
            return fileOperationVO;
        }
        return null;
    }

    public FileVO saveFileInfoToDB(String fileName, String finalFileName, String filePath, long fileSize, String uploadUser, Date uploadDate) {
        FileVO fileVO = new FileVO();
        String suffix = getSuffix(fileName);
        CwmFilePO cwmFilePO = new CwmFilePO();
        cwmFilePO.setFilename(fileName);
        cwmFilePO.setFinalname(finalFileName);
        cwmFilePO.setFilelocation(filePath);
        cwmFilePO.setFiletype(suffix);
        cwmFilePO.setUploadDate(uploadDate);
        cwmFilePO.setUploadUserId(uploadUser);
        cwmFilePO.setFilecatalog("COMMON");
        cwmFilePO.setFilesize(fileSize);
        cwmFileRepository.save(cwmFilePO);
        long fileId = cwmFilePO.getFileid();
        // 返回值
        fileVO.setId(fileId);
        fileVO.setName(fileName);
        fileVO.setSuffix(suffix);
        return fileVO;
    }

    /**
     * 上传文件到CDH集群中的HDFS中
     *
     * @param in
     * @param destPath 文件存储路径
     * @return
     */
    public boolean uploadFileToHDFS(InputStream in, String destPath) {
        boolean flag = false;
        try {
            HdfsApi hdfsApi = new HdfsApi(conf, user);
            flag = hdfsApi.uploadFile(in, destPath, true);
            hdfsApi.fsClose();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 判断文件是否存在在HDFS
     *
     * @param destPath
     * @return
     */
    public boolean isExitsFileInHDFS(String destPath){
        boolean flag = false;
        try {
            HdfsApi hdfsApi = new HdfsApi(conf, user);
            flag = hdfsApi.existFile(destPath);
            hdfsApi.fsClose();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 从HDFS中下载文件
     *
     * @param request
     * @param response
     * @param filePath 文件路径
     * @param fileName 文件名
     */
    public void downloadFileFromHDFS(HttpServletRequest request, HttpServletResponse response, String filePath, String fileName) {
        try {
            HdfsApi hdfsApi = new HdfsApi(conf, user);
            hdfsApi.downLoadFile(request, response, filePath, fileName);
            hdfsApi.fsClose();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public CwmFilePO getCwmFile(Long fileId) {
        List<CwmFilePO> cwmFilePOList = cwmFileRepository.findByFileid(fileId);
        return cwmFilePOList.get(0);
    }

    public String getFilePathByFileId(String fileId) {
        CwmFilePO cwmFileEntity = getCwmFile(Long.valueOf(fileId));
        return cwmFileEntity.getFilelocation();
    }

    /**
     * 得到文件的后缀
     *
     * @param fileName 文件名称
     * @return String 后缀名称
     */
    public static String getSuffix(String fileName) {
        String returnSuffix = "";
        String tmp = "";
        try {
            int index = fileName.lastIndexOf(".");
            if (index == -1) {
                tmp = "";
            } else
                tmp = fileName.substring(index + 1, fileName.length());
        } catch (Exception e) {
            tmp = "";
        }
        returnSuffix = tmp;
        return returnSuffix;
    }

    private static final List<String> imageTypes = new ArrayList<String>() {{
        add("jpg");
        add("jpeg");
        add("jpe");
        add("png");
        add("gif");
        add("tiff");
        add("tif");
        add("svg");
        add("svgz");
    }};

    private static final List<String> videoTypes = new ArrayList<String>() {{
        add("mp4");
        add("wmv");
        add("mov");
        add("avi");
    }};

    private static final List<String> voiceTypes = new ArrayList<String>() {{
        add("mp3");
        add("wav");
    }};


}
