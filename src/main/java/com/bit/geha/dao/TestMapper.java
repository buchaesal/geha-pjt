package com.bit.geha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.bit.geha.dto.Test;


@Mapper
public interface TestMapper {
	@Select("SELECT * FROM testtable ")
    List<Test> selectMembers();
}
