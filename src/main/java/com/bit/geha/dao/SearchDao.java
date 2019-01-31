package com.bit.geha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bit.geha.criteria.SearchCriteria;
import com.bit.geha.dto.SearchDto;

@Mapper
public interface SearchDao {
	public List<SearchDto> listGeha(SearchCriteria sc);
}
