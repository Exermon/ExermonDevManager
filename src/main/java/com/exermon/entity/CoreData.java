package com.exermon.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 核心数据类型
 */

@Getter @Setter @Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CoreData extends BaseData {

    @Column(nullable = false)
    protected String name;
    @Column(length = 512)
    protected String description;

}
