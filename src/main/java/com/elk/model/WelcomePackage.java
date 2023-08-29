package com.elk.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class WelcomePackage implements Serializable {

    private String name;

    private String message;
}
