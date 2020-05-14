package com.cssrc.orient.hdfsService.model;

import java.io.Serializable;

public class FileVO implements Serializable {

    private Long id;

    private String name;

    private String suffix;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
