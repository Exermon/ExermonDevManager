package com.exermon.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * 基本数据类型
 */
@Getter
@Setter
public abstract class BaseData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

}
