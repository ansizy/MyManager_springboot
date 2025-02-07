package com.example.demo.entity;

import lombok.Data;

/**
 * movie的子类
 */
@Data
public class UniqueId {
    private String type;
    private boolean isDefault;
    private String value;
}
