package com.bit.geha.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.geha.criteria.Criteria;
import com.bit.geha.dao.CommentDao;
import com.bit.geha.dto.LikeDto;
import com.bit.geha.dto.ReplyReviewDto;
import com.bit.geha.service.CommentService;
import com.bit.geha.util.RoomPageMaker;

@Controller
@RequestMapping("/comment")
public class CommentController {

	 @Resource(name="com.bit.geha.service.CommentService")
	    CommentService mCommentService;
	 
	    @Autowired
	    CommentDao commentDao;
	    @Autowired
	    RoomPageMaker pageMaker;

	    
	    @RequestMapping("/list") //댓글 리스트
	    @ResponseBody
	    public List<ReplyReviewDto> mCommentServiceList(@ModelAttribute("cri") Criteria cri,@RequestParam("page") int page,@RequestParam("guestHouseCode") int guestHouseCode, Model model) throws Exception{

	    	cri.setPage(page);

	    	pageMaker.setCri(cri);
	    	pageMaker.setTotalCount(commentDao.commentCnt1(cri, guestHouseCode));

	        return mCommentService.commentListService(pageMaker,cri,guestHouseCode);

	    }
	    
	    @RequestMapping("/paging")
	    @ResponseBody
	    public RoomPageMaker mCommentServicePaging(@ModelAttribute("cri") Criteria cri,  @RequestParam("guestHouseCode") int guestHouseCode, Model model) throws Exception{   
	    	pageMaker.setTotalCount(commentDao.commentCnt1(cri, guestHouseCode));
	    	return pageMaker;
	    
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

	        return mCommentService.selectlikeService(memberCode);
	    }
	    
	    
	    
	    @RequestMapping("/update") //댓글 수정  
	    @ResponseBody
	    public int mCommentServiceUpdateProc(@RequestParam int reviewNo, @RequestParam String replyContent) throws Exception{
	        ReplyReviewDto comment = new ReplyReviewDto();
	        comment.setReviewNo(reviewNo);
	        comment.setReplyContent(replyContent);
	        System.out.println("2");
	        return mCommentService.commentUpdateService(comment);
	    }

}
