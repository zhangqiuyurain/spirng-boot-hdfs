package com.cssrc.orient.hdfsService.controller;




import com.cssrc.orient.hdfsService.bean.AjaxResponseData;
import com.cssrc.orient.hdfsService.business.FileStorageBusiness;
import com.cssrc.orient.hdfsService.exception.OrientBaseAjaxException;
import com.cssrc.orient.hdfsService.hdfs.HdfsApi;
import com.cssrc.orient.hdfsService.model.FileVO;
import com.cssrc.orient.hdfsService.util.Configration;
import org.apache.hadoop.conf.Configuration;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

// @CrossOrigin
@RestController
@RequestMapping("/cdhService/hdfsTest")
public class HdfsApiController {

    @Autowired
    private Configuration conf;

    @Autowired
    private Configration con;

    @Value("root")
    private String user;

    @Autowired
    FileStorageBusiness fileStorageBusiness;

    public static final String UPLOAD_ROOT= "上传文件";
    public static final String UPLOAD_DEPART= "数据中心";
    public static final String FILENAME_CONNECTOR= "_";
    public static final String FILE_SEPARATOR = "/";
    public static final SimpleDateFormat pathFormat = new SimpleDateFormat("-yyyy-MM-dd-HHmmss-");
    public static final SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @RequestMapping("/upload")
    public AjaxResponseData<String> uploadFile(HttpServletRequest request) {
        final boolean[] flag = {false};
        final Long[] fileId = new Long[1];
        final String[] fileNameArray = new String[1];
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        AjaxResponseData<String> results = new AjaxResponseData<>();
        if(isMultipart) {
            // 无法转换
            // DefaultMultipartHttpServletRequest multipartHttpServletRequest = (DefaultMultipartHttpServletRequest) request;
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            MultiValueMap<String, MultipartFile> fileMultiValueMap = multipartHttpServletRequest.getMultiFileMap();
            fileMultiValueMap.forEach((key, value) -> value.forEach(multipartFile -> {
                long fileSize = multipartFile.getSize();
                if (fileSize > 0) {
                    String fileName = multipartFile.getOriginalFilename();
                    try {
                        Date uploadDate = new Date();
                        String timeSuffer = fileNameFormat.format(uploadDate);
                        // 上传到集群的文件路径
                        String finalFileName = timeSuffer + FILENAME_CONNECTOR +fileName;
                        String destPath = FILE_SEPARATOR + UPLOAD_ROOT + FILE_SEPARATOR + UPLOAD_DEPART + FILE_SEPARATOR + finalFileName;
                        InputStream in = multipartFile.getInputStream();
                        HdfsApi api = new HdfsApi(conf, user);
                        // TODO 将文件信息存入到CWM_FILES中。如果保存成功，则将信息保存到CWM_FILES表中
                        flag[0] = api.uploadFile(in,destPath,true);
                        if(flag[0]) {
                            FileVO fileVO = fileStorageBusiness.saveFileInfoToDB(fileName,finalFileName,destPath,fileSize,user,uploadDate);
                            fileId[0] = fileVO.getId();
                            fileNameArray[0] = fileName;
                        }
                        System.out.println(flag[0]);
                        api.fsClose();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }));
        }else {
            throw new OrientBaseAjaxException("500100","请求中不包含文件信息");
        }
        String url = con.getServerAddress() + ":" + con.getServerPort() + con.getServletContextPath()
                + "/cdhService/hdfsTest/download?fileId=" + fileId[0] + "&fileName=" + fileNameArray[0];

        results.setMsg(String.valueOf(fileId[0]));
        results.setResults(url);
        return results;
    }

    @RequestMapping("/uploadFile1")
    public AjaxResponseData<String> uplaodFile1(String srcFilePath) throws Exception {
        boolean flag = false;
        long fileId = 1;
        Date uploadDate = new Date();
        AjaxResponseData<String> results = new AjaxResponseData<>();
        String fileName = srcFilePath.substring(srcFilePath.lastIndexOf(File.separator)+1);
        String timeSuffer = fileNameFormat.format(uploadDate);
        long fileSize = 100;
        // 上传到集群的文件路径
        String finalFileName = timeSuffer + FILENAME_CONNECTOR +fileName;
        String destPath = FILE_SEPARATOR + UPLOAD_ROOT + FILE_SEPARATOR + UPLOAD_DEPART + FILE_SEPARATOR +
                finalFileName;
        HdfsApi api = new HdfsApi(conf, user);
        flag = api.uploadFile(srcFilePath,destPath,true);
        if(flag) {
            FileVO fileVO = fileStorageBusiness.saveFileInfoToDB(fileName,finalFileName,destPath,fileSize,user,uploadDate);
            fileId = fileVO.getId();
        }
        System.out.println(flag);
        api.fsClose();
        String url = con.getServerAddress() + ":" + con.getServerPort() + con.getServletContextPath()
                + "/cdhService/hdfsTest/download1?fileId=" + fileId;
        results.setMsg(String.valueOf(fileId));
        results.setResults(url);
        return results;
    }

    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response, String fileId, String fileName) throws IOException, InterruptedException {
        if(!StringUtils.isEmpty(fileId)) {
            String filePath = fileStorageBusiness.getFilePathByFileId(fileId);
            HdfsApi api = new HdfsApi(conf,user);
            api.downLoadFile(filePath,response);
            api.fsClose();
        }
    }

    @RequestMapping("/download1")
    public void download1(String fileId) throws IOException, InterruptedException {
        if(!StringUtils.isEmpty(fileId)) {
            String filePath = fileStorageBusiness.getFilePathByFileId(fileId);
            HdfsApi api = new HdfsApi(conf,user);
            api.downLoadFile(filePath, "C:\\Users\\张秋雨\\Desktop\\download.sql");
            api.fsClose();
        }
    }

}
