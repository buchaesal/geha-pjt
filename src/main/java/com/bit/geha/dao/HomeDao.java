package com.bit.geha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bit.geha.dto.SearchDto;

@Mapper
public interface HomeDao {
	public List<SearchDto> listGehaRating();
	public List<SearchDto> listGehaReview();
}
