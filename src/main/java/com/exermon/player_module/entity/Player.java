package com.exermon.player_module.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import com.exermon.entity.BaseData;

@Entity
@Getter
@Setter
public class Player extends BaseData {

    @Column(nullable = false, length = 64)
    public String username;
    @Column(nullable = false, length = 512)
    public String password;

    @Column(length = 128)
    public String email;
    @Column(length = 32)
    public String phone;
}
