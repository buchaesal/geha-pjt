package com.bit.geha.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.geha.dao.CommentDao;
import com.bit.geha.dto.LikeDto;
import com.bit.geha.dto.ReplyReviewDto;
import com.bit.geha.service.CommentService;

@Controller
@RequestMapping("/comment")
public class CommentController {

	 @Resource(name="com.bit.geha.service.CommentService")
	    CommentService mCommentService;
	 
	    @Autowired
	    CommentDao commentDao;
	    

	    
	    @RequestMapping("/list") //댓글 리스트
	    @ResponseBody
	    public List<ReplyReviewDto> mCommentServiceList(@RequestParam("guestHouseCode") int guestHouseCode, Model model) throws Exception{

	    	List<ReplyReviewDto> comment = commentDao.commentList(guestHouseCode);
	    	
	    	model.addAttribute("comment",comment);
	    	
	        return mCommentService.commentListService(guestHouseCode);
	    }
	    
	    

	    @RequestMapping("/addlike") //좋아요
	    @ResponseBody
	    public int addLike(@RequestParam int roomCode,@RequestParam int memberCode) throws Exception{

	        LikeDto likeDto= new LikeDto();
	        likeDto.setMemberCode(memberCode);
	        likeDto.setRoomCode(roomCode);

	        return mCommentService.addLikeService(likeDto);
	    }
	    
	    @RequestMapping("/deletelike") //좋아요취소 
	    @ResponseBody
	    public int deletelike(@RequestParam int roomCode,@RequestParam int memberCode) throws Exception{

	        
	        return mCommentService.deletelikeService(roomCode,memberCode);
	    }
	    
	    
	    @RequestMapping("/delete/{reviewNo}") //댓글취소 
	    @ResponseBody
	    public int mCommentServiceDelete(@PathVariable int reviewNo) throws Exception{
	        
	        return mCommentService.commentDeleteService(reviewNo);
	    }

	    
	    @RequestMapping("/selectlike") //좋아요 리스트
	    @ResponseBody
	    public List<LikeDto> deletelikeService(@RequestParam("memberCode") int memberCode, Model model) throws Exception{

	    	List<LikeDto> like = commentDao.selectlike(memberCode);
	    	
	    	model.addAttribute("like",like);
	    	
	        return mCommentService.selectlikeService(memberCode);
	    }
	    
	    
	    
	    @RequestMapping("/update") //댓글 수정  
	    @ResponseBody
	    public int mCommentServiceUpdateProc(@RequestParam int reviewNo, @RequestParam String replyContent) throws Exception{

	        ReplyReviewDto comment = new ReplyReviewDto();
	        comment.setReviewNo(reviewNo);
	        comment.setReplyContent(replyContent);

	        return mCommentService.commentUpdateService(comment);
	    }

}
