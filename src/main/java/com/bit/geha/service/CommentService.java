package com.bit.geha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit.geha.dao.CommentDao;
import com.bit.geha.dto.LikeDto;
import com.bit.geha.dto.ReplyReviewDto;

@Service("com.bit.geha.service.CommentService")
public class CommentService {

	   @Autowired
	    CommentDao mCommentMapper;
	    
    public List<ReplyReviewDto> commentListService(int guestHouseCode) throws Exception{
	        
	         return mCommentMapper.commentList(guestHouseCode);
	    }
    		    //좋아요
	    public int addLikeService(LikeDto likeDto) throws Exception{

	        return mCommentMapper.addlike(likeDto);
	    }
	    //좋아요취소 
	    public int deletelikeService(int roomCode, int memberCode) throws Exception{
	    	
	    	return mCommentMapper.deletelike(roomCode,memberCode);
	    }
	    
	    //좋아요유지
	    public List<LikeDto> selectlikeService(int memberCode) throws Exception	{
	    	return mCommentMapper.selectlike(memberCode);
	    }
	    
	    
	    public int commentDeleteService(int reviewNo) throws Exception{
	        
	        return mCommentMapper.commentDelete(reviewNo);
	    }
	    
	    public int commentUpdateService(ReplyReviewDto comment) throws Exception{
	        
	        return mCommentMapper.commentUpdate(comment);
	    }
	    



}
