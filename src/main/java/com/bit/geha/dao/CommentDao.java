package com.bit.geha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bit.geha.dto.ReplyReviewDto;
import com.bit.geha.dto.RoomDto;

@Mapper
public interface CommentDao {
/*    // 댓글 개수
    public int commentCount() throws Exception;*/
 
    // 댓글 목록
    public List<ReplyReviewDto> commentList(int guestHouseCode) throws Exception;
    
    
/*    // 댓글 작성
    public int commentInsert(ReplyReviewDto comment) throws Exception;
    
    // 댓글 삭제
    public int commentDelete(int reviewNo) throws Exception;
    
    // 댓글 수정
*/    public int commentUpdate(ReplyReviewDto comment) throws Exception;
 



}
