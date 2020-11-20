package com.exermon.player_module.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import com.exermon.entity.BaseData;

@Getter @Setter @Entity
public class Player extends BaseData {

    @Column(nullable = false, length = 64)
    private String username;
    @Column(nullable = false, length = 512)
    private String password;

    @Column(length = 128)
    private String email;
    @Column(length = 32)
    private String phone;
}
