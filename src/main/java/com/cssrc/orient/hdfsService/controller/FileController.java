package com.cssrc.orient.hdfsService.controller;

import com.cssrc.orient.hdfsService.bean.CommonAjaxResponse;
import com.cssrc.orient.hdfsService.business.FileDeleteBusiness;
import com.cssrc.orient.hdfsService.business.FileStorageBusiness;
import com.cssrc.orient.hdfsService.exception.OrientBaseAjaxException;
import com.cssrc.orient.hdfsService.model.FileOperationVO;
import com.cssrc.orient.hdfsService.model.FileVO;
import com.cssrc.orient.hdfsService.model.entity.CwmFilePO;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rain
 * @date
 */

@RestController
@RequestMapping("file")
public class FileController {

    private final static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    FileStorageBusiness fileStorageBusiness;

    @Autowired
    FileDeleteBusiness fileDeleteBusiness;


    /**
     * 上传文件
     *
     * @param request
     * @param modelId 所属模型id  非必要参数
     * @param dataId  所属数据id  非必要参数
     * @param userId  上传用户id  非必要参数
     * @return
     */
    @PostMapping()
    public CommonAjaxResponse<List<FileVO>> upload(HttpServletRequest request, String modelId, String dataId, String userId) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        List<FileVO> results = new ArrayList<>();
        if (isMultipart) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            // DefaultMultipartHttpServletRequest multipartHttpServletRequest = (DefaultMultipartHttpServletRequest) request;
            MultiValueMap<String, MultipartFile> fileMultiValueMap = multipartHttpServletRequest.getMultiFileMap();
            // 存储文件
            fileMultiValueMap.forEach((key, value) -> value.forEach(multipartFile -> {
                long fileSize = multipartFile.getSize();
                if (fileSize > 0) {
                    InputStream in = null;
                    try {
                        String fileName = multipartFile.getOriginalFilename();
                        in = multipartFile.getInputStream();
                        FileVO fileVO = fileStorageBusiness.saveFile(fileName, fileSize, in, modelId, dataId, userId);
                        results.add(fileVO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }));
        }
        return new CommonAjaxResponse<>("", true, results);
    }


    /**
     * 通过文件id获取文件的基本信息
     *
     * @param fileId
     * @return
     */
    @GetMapping(value = "/{fileId}")
    public CommonAjaxResponse<FileVO> getFileInfo(@PathVariable Long fileId) {
        CommonAjaxResponse<FileVO> retVal = new CommonAjaxResponse<>();
        CwmFilePO cwmFilePO = fileStorageBusiness.getCwmFile(fileId);
        if (cwmFilePO != null) {
            FileVO fileVO = new FileVO();
            fileVO.setId(fileId);
            fileVO.setName(cwmFilePO.getFilename());
            fileVO.setSuffix(FileStorageBusiness.getSuffix(cwmFilePO.getFilename()));
            retVal.setSuccess(true);
            retVal.setResults(fileVO);
        } else {
            retVal.setSuccess(false);
            retVal.setMsg("文件已经删除");
        }
        return retVal;
    }


    /**
     * 上传一个文件，严格限制所有参数必须传值
     *
     * @param file    文件对象
     * @param modelId 模型id
     * @param dataId  数据id
     * @param userId  用户id
     * @return
     */
    @PostMapping(value = "/upload")
    public CommonAjaxResponse<FileVO> uploadOneFile(@RequestParam(value = "file") CommonsMultipartFile file, @RequestParam(value = "modelId") String modelId,
                                                    @RequestParam(value = "dataId") String dataId, @RequestParam(value = "userId") String userId) {
        FileVO result = null;
        long fileSize = file.getSize();
        if (fileSize > 0) {
            InputStream in = null;
            try {
                String fileName = file.getOriginalFilename();
                in = file.getInputStream();
                result = fileStorageBusiness.saveFile(fileName, fileSize, in, modelId, dataId, userId);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            throw new OrientBaseAjaxException("500100", "请求中不包含文件信息");
        }
        return new CommonAjaxResponse<>("", true, result);
    }

    /**
     * 下载文件
     *
     * @param request
     * @param response
     * @param fileId
     */
    @GetMapping(value = "/download/{fileId}")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable Long fileId) {
        FileOperationVO fileOperationVO = fileStorageBusiness.getFile(fileId, "download");
        // CwmFilePO filePO = fileStorageBusiness.getCwmFile(fileId);
        if(null != fileOperationVO) {
            String fileName = fileOperationVO.getFileName();
            String filePath = fileOperationVO.getFileLocation();
            fileStorageBusiness.downloadFileFromHDFS(request, response, filePath, fileName);
        }
    }


    /**
     * 预览文件
     * TODO 待测试
     *
     * @param request
     * @param response
     * @param fileId
     */
    @GetMapping(value = "/preview/{fileId}")
    public void preview(HttpServletRequest request, HttpServletResponse response, @PathVariable Long fileId) {
        FileOperationVO fileOperationVO = fileStorageBusiness.getFile(fileId, "preview");
        if(null != fileOperationVO) {
            String fileName = fileOperationVO.getFileName();
            String filePath = fileOperationVO.getFileLocation();
            fileStorageBusiness.downloadFileFromHDFS(request, response, filePath, fileName);
        }

    }

    /**
     * 删除一个文件
     *
     * @param fileId
     * @return
     */
    @DeleteMapping(value = "/{fileId}")
    public CommonAjaxResponse delete(@PathVariable Long fileId) {
        CommonAjaxResponse retVal = new CommonAjaxResponse();
        fileDeleteBusiness.deleteFile(fileId);
        retVal.setSuccess(true);
        return retVal;
    }

    /**
     * 根据modelId和dataId批量删除相关文件
     *
     * @param modelId
     * @param dataId
     * @return
     */
    @DeleteMapping(value = "/delete/{modelId}/{dataId}")
    public CommonAjaxResponse deleteAllFilesByModelIdAndDataId(@PathVariable String modelId, @PathVariable String dataId) {
        CommonAjaxResponse retVal = new CommonAjaxResponse();
        fileDeleteBusiness.deleteAllFilesByModelIdAndDataId(modelId, dataId);
        retVal.setSuccess(true);
        return retVal;
    }

}
