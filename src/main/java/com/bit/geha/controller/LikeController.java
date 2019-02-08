package com.bit.geha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bit.geha.dao.RoomDao;
import com.bit.geha.dto.LikeDto;



@Controller
@RequestMapping("/room")
public class LikeController {

	@Autowired
	RoomDao roomDao;


    @RequestMapping(value="/like", method = RequestMethod.POST,  consumes = "application/json", produces = "application/json")
    public String addlike(@RequestBody LikeDto likeDto) {

    	roomDao.addlike(likeDto);
    return "/room/like";	
    }

    
}
