package com.cssrc.orient.hdfsService.business;

import com.cssrc.orient.hdfsService.hdfs.HdfsApi;
import com.cssrc.orient.hdfsService.model.entity.CwmFilePO;
import com.cssrc.orient.hdfsService.model.repository.CwmFileRepository;
import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author rain
 * @date 2020/4/25 15:38
 */
@Component
public class FileDeleteBusiness {


    @Autowired
    private Configuration conf;

    @Value("root")
    private String user;


    @Autowired
    CwmFileRepository cwmFileRepository;

    @Autowired
    FileStorageBusiness fileStorageBusiness;


    public void deleteAllFilesByModelIdAndDataId(String modelId, String dataId) {
        List<CwmFilePO> cwmFilePOList = cwmFileRepository.findByTableidAndDataid(modelId, dataId);
        cwmFilePOList.forEach(cwmFilePO -> deleteFile(cwmFilePO.getFileid()));
    }

    public void deleteFile(Long fileId) {
        try {
            CwmFilePO filePO = fileStorageBusiness.getCwmFile(fileId);
            String filePath = filePO.getFilelocation();
            // 删除CWM_FILE中的文件信息
            cwmFileRepository.deleteByFileid(fileId);
            // 删除文件
            HdfsApi hdfsApi = new HdfsApi(conf, user);
            // TODO 是否删除空目录
            // TODO 若是图片文件，是否删除缩略图文件，暂不删除
            hdfsApi.deleteFile(filePath, false);
            hdfsApi.fsClose();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
