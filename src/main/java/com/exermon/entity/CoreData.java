package com.exermon.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 核心数据类型
 */
@Getter
@Setter
public abstract class CoreData extends BaseData {

    @Column(nullable = false)
    public String name;
    @Column(length = 512)
    public String description;

}
