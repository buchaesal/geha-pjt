package com.bit.geha.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.geha.dao.CommentDao;
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
	    
	   /* @RequestMapping("/insert") //댓글 작성 
	    @ResponseBody
	    public int mCommentServiceInsert(@RequestParam String content) throws Exception{
	        
	        CommentVO comment = new CommentVO();
	        comment.setBno(1);
	        comment.setContent(content);
	        //로그인 기능을 구현했거나 따로 댓글 작성자를 입력받는 폼이 있다면 입력 받아온 값으로 사용하면 됩니다. 저는 따로 폼을 구현하지 않았기때문에 임시로 "test"라는 값을 입력해놨습니다.
	        comment.setWriter("test");  
	        
	        return mCommentService.commentInsertService(comment);
	    }
	    

	    
	    @RequestMapping("/delete/{cno}") //댓글 삭제  
	    @ResponseBody
	    public int mCommentServiceDelete(@PathVariable int cno) throws Exception{
	        
	        return mCommentService.commentDeleteService(cno);
	    }
	    */
	    
	    @RequestMapping("/update") //댓글 수정  
	    @ResponseBody
	    public int mCommentServiceUpdateProc(@RequestParam int reviewNo, @RequestParam String replyContent) throws Exception{

	        ReplyReviewDto comment = new ReplyReviewDto();
	        comment.setReviewNo(reviewNo);
	        comment.setReplyContent(replyContent);

	        return mCommentService.commentUpdateService(comment);
	    }

}
