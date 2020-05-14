package com.cssrc.orient.hdfsService.hdfs.model;

/**
 * @author rain
 * @date 2020/4/27 17:14
 */
public class HdfsVO {

    // 源路径
    private String srcPath;
    // 目标路径
    private String destPath;
    // 是否递归删除
    private boolean recursive = true;
    // 是否覆盖源文件
    private boolean override = true;

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }
}
