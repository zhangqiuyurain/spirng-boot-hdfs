package com.cssrc.orient.hdfsService.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * @author rain
 * @date 2020/4/27 17:22
 */
@Entity
@Table(name = "cwm_file", schema = "orient_cdh", catalog = "")
public class CwmFilePO {
    private String schemaid;
    private String tableid;
    private long fileid;
    private String filename;
    private String filedescription;
    private String filetype;
    private String filelocation;
    private Long filesize;
    private String parseRule;
    private String uploadUserId;
    private Date uploadDate;
    private String deleteUserId;
    private Date deleteDate;
    private String dataid;
    private String finalname;
    private String edition;
    private Boolean isValid;
    private String filesecrecy;
    private String uploadUserMac;
    private String uploadStatus;
    private String fileFolder;
    private Long isFoldFile;
    private Boolean isWholeSearch;
    private Boolean isDataFile;
    private String cwmFolderId;
    private String converState;
    private String filecatalog;
    private String filemark;
    private String contentmd5;
    private Date lastModifyDate;

    @Basic
    @Column(name = "schemaid")
    public String getSchemaid() {
        return schemaid;
    }

    public void setSchemaid(String schemaid) {
        this.schemaid = schemaid;
    }

    @Basic
    @Column(name = "tableid")
    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "fileid")
    public long getFileid() {
        return fileid;
    }

    public void setFileid(long fileid) {
        this.fileid = fileid;
    }

    @Basic
    @Column(name = "filename")
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Basic
    @Column(name = "filedescription")
    public String getFiledescription() {
        return filedescription;
    }

    public void setFiledescription(String filedescription) {
        this.filedescription = filedescription;
    }

    @Basic
    @Column(name = "filetype")
    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    @Basic
    @Column(name = "filelocation")
    public String getFilelocation() {
        return filelocation;
    }

    public void setFilelocation(String filelocation) {
        this.filelocation = filelocation;
    }

    @Basic
    @Column(name = "filesize")
    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }

    @Basic
    @Column(name = "parse_rule")
    public String getParseRule() {
        return parseRule;
    }

    public void setParseRule(String parseRule) {
        this.parseRule = parseRule;
    }

    @Basic
    @Column(name = "upload_user_id")
    public String getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(String uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    @Basic
    @Column(name = "upload_date")
    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Basic
    @Column(name = "delete_user_id")
    public String getDeleteUserId() {
        return deleteUserId;
    }

    public void setDeleteUserId(String deleteUserId) {
        this.deleteUserId = deleteUserId;
    }

    @Basic
    @Column(name = "delete_date")
    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    @Basic
    @Column(name = "dataid")
    public String getDataid() {
        return dataid;
    }

    public void setDataid(String dataid) {
        this.dataid = dataid;
    }

    @Basic
    @Column(name = "finalname")
    public String getFinalname() {
        return finalname;
    }

    public void setFinalname(String finalname) {
        this.finalname = finalname;
    }

    @Basic
    @Column(name = "edition")
    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    @Basic
    @Column(name = "is_valid")
    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    @Basic
    @Column(name = "filesecrecy")
    public String getFilesecrecy() {
        return filesecrecy;
    }

    public void setFilesecrecy(String filesecrecy) {
        this.filesecrecy = filesecrecy;
    }

    @Basic
    @Column(name = "upload_user_mac")
    public String getUploadUserMac() {
        return uploadUserMac;
    }

    public void setUploadUserMac(String uploadUserMac) {
        this.uploadUserMac = uploadUserMac;
    }

    @Basic
    @Column(name = "upload_status")
    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    @Basic
    @Column(name = "file_folder")
    public String getFileFolder() {
        return fileFolder;
    }

    public void setFileFolder(String fileFolder) {
        this.fileFolder = fileFolder;
    }

    @Basic
    @Column(name = "is_fold_file")
    public Long getIsFoldFile() {
        return isFoldFile;
    }

    public void setIsFoldFile(Long isFoldFile) {
        this.isFoldFile = isFoldFile;
    }

    @Basic
    @Column(name = "is_whole_search")
    public Boolean getWholeSearch() {
        return isWholeSearch;
    }

    public void setWholeSearch(Boolean wholeSearch) {
        isWholeSearch = wholeSearch;
    }

    @Basic
    @Column(name = "is_data_file")
    public Boolean getDataFile() {
        return isDataFile;
    }

    public void setDataFile(Boolean dataFile) {
        isDataFile = dataFile;
    }

    @Basic
    @Column(name = "cwm_folder_id")
    public String getCwmFolderId() {
        return cwmFolderId;
    }

    public void setCwmFolderId(String cwmFolderId) {
        this.cwmFolderId = cwmFolderId;
    }

    @Basic
    @Column(name = "conver_state")
    public String getConverState() {
        return converState;
    }

    public void setConverState(String converState) {
        this.converState = converState;
    }

    @Basic
    @Column(name = "filecatalog")
    public String getFilecatalog() {
        return filecatalog;
    }

    public void setFilecatalog(String filecatalog) {
        this.filecatalog = filecatalog;
    }

    @Basic
    @Column(name = "filemark")
    public String getFilemark() {
        return filemark;
    }

    public void setFilemark(String filemark) {
        this.filemark = filemark;
    }

    @Basic
    @Column(name = "contentmd5")
    public String getContentmd5() {
        return contentmd5;
    }

    public void setContentmd5(String contentmd5) {
        this.contentmd5 = contentmd5;
    }

    @Basic
    @Column(name = "last_modify_date")
    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CwmFilePO cwmFilePO = (CwmFilePO) o;
        return fileid == cwmFilePO.fileid &&
                Objects.equals(schemaid, cwmFilePO.schemaid) &&
                Objects.equals(tableid, cwmFilePO.tableid) &&
                Objects.equals(filename, cwmFilePO.filename) &&
                Objects.equals(filedescription, cwmFilePO.filedescription) &&
                Objects.equals(filetype, cwmFilePO.filetype) &&
                Objects.equals(filelocation, cwmFilePO.filelocation) &&
                Objects.equals(filesize, cwmFilePO.filesize) &&
                Objects.equals(parseRule, cwmFilePO.parseRule) &&
                Objects.equals(uploadUserId, cwmFilePO.uploadUserId) &&
                Objects.equals(uploadDate, cwmFilePO.uploadDate) &&
                Objects.equals(deleteUserId, cwmFilePO.deleteUserId) &&
                Objects.equals(deleteDate, cwmFilePO.deleteDate) &&
                Objects.equals(dataid, cwmFilePO.dataid) &&
                Objects.equals(finalname, cwmFilePO.finalname) &&
                Objects.equals(edition, cwmFilePO.edition) &&
                Objects.equals(isValid, cwmFilePO.isValid) &&
                Objects.equals(filesecrecy, cwmFilePO.filesecrecy) &&
                Objects.equals(uploadUserMac, cwmFilePO.uploadUserMac) &&
                Objects.equals(uploadStatus, cwmFilePO.uploadStatus) &&
                Objects.equals(fileFolder, cwmFilePO.fileFolder) &&
                Objects.equals(isFoldFile, cwmFilePO.isFoldFile) &&
                Objects.equals(isWholeSearch, cwmFilePO.isWholeSearch) &&
                Objects.equals(isDataFile, cwmFilePO.isDataFile) &&
                Objects.equals(cwmFolderId, cwmFilePO.cwmFolderId) &&
                Objects.equals(converState, cwmFilePO.converState) &&
                Objects.equals(filecatalog, cwmFilePO.filecatalog) &&
                Objects.equals(filemark, cwmFilePO.filemark) &&
                Objects.equals(contentmd5, cwmFilePO.contentmd5) &&
                Objects.equals(lastModifyDate, cwmFilePO.lastModifyDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaid, tableid, fileid, filename, filedescription, filetype, filelocation, filesize, parseRule, uploadUserId, uploadDate, deleteUserId, deleteDate, dataid, finalname, edition, isValid, filesecrecy, uploadUserMac, uploadStatus, fileFolder, isFoldFile, isWholeSearch, isDataFile, cwmFolderId, converState, filecatalog, filemark, contentmd5, lastModifyDate);
    }
}
