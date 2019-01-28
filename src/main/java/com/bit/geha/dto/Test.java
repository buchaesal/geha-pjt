package com.bit.geha.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("test")
@Data
public class Test {

   private int bno;
   private String title;
   
}