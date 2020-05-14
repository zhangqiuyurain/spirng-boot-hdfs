package com.cssrc.orient.hdfsService.model.repository;

import com.cssrc.orient.hdfsService.model.entity.CwmFilePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author rain
 * @date 2020/4/27 17:23
 */
public interface CwmFileRepository extends JpaRepository<CwmFilePO, Integer> {

    /**
     * 根据ModelId和DataId获取数据
     *
     * @param tableId
     * @param dataId
     * @return
     */
    List<CwmFilePO> findByTableidAndDataid(String tableId, String dataId);

    List<CwmFilePO> findByFileid(Long fileId);

    /**
     * 根据fileId删除文件
     *
     * @param fileId
     */
    @Transactional
    void deleteByFileid(Long fileId);

}
