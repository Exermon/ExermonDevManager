package com.exermon.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * 基本数据类型
 */
@Getter @Setter @Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseData {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Integer id;
}
