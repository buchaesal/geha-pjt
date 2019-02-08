package com.bit.geha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bit.geha.dto.LikeDto;
import com.bit.geha.dto.ReplyReviewDto;
import com.bit.geha.dto.SearchDto;

@Mapper
public interface CommentDao {
    // 댓글 개수
    public SearchDto commentCnt(int guestHouseCode) throws Exception;
 
    // 댓글 목록
    public List<ReplyReviewDto> commentList(int guestHouseCode) throws Exception;
    
    
    // 좋아요
    public int addlike(LikeDto likeDto) throws Exception;
    
    //좋아요 취소
    public int deletelike(int roomCode, int memberCode) throws Exception;
    
    //좋아요 유지
    public List<LikeDto> selectlike(int memberCode) throws Exception;
    
    // 댓글 삭제
    public int commentDelete(int reviewNo) throws Exception;
    
    // 댓글 수정
    public int commentUpdate(ReplyReviewDto comment) throws Exception;
 



}
