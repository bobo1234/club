package com.java.back.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


public class BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public BaseBean() {

	}
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
