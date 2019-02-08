package com.bit.geha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit.geha.dao.CommentDao;
import com.bit.geha.dto.ReplyReviewDto;

@Service("com.bit.geha.service.CommentService")
public class CommentService {

	   @Autowired
	    CommentDao mCommentMapper;
	    
    public List<ReplyReviewDto> commentListService(int guestHouseCode) throws Exception{
	        
	         return mCommentMapper.commentList(guestHouseCode);
	    }
    /*		    
	    public int commentInsertService(ReplyReviewDto comment) throws Exception{
	        
	        return mCommentMapper.commentInsert(comment);
	    }
	    
	    public int commentDeleteService(int cno) throws Exception{
	        
	        return mCommentMapper.commentDelete(cno);
	    }*/
	    
	    public int commentUpdateService(ReplyReviewDto comment) throws Exception{
	        
	        return mCommentMapper.commentUpdate(comment);
	    }
	    



}
