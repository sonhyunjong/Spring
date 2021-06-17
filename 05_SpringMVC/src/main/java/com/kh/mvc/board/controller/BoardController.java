package com.kh.mvc.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.mvc.board.model.vo.Board;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BoardController {
	
	// 컨트롤러의 메소드의 리턴 타입이 void일 경우 Mapping URL을 유추해서 View 찾는다.
	@GetMapping("/board/write")
	public void writeView() {
		log.info("게시글 작성 페이지 요청");	
		
//		return "board/write";
	}
	
	@PostMapping("/board/write")
	public ModelAndView write(ModelAndView model,
//			@ModelAttribute Board board, @RequestParam("upfile") MultipartFile[] upfile) {
			@ModelAttribute Board board, @RequestParam("upfile") MultipartFile upfile) {
		log.info("게시글 작성 요청");
		
		
		
		model.setViewName("board/write");
		
		return model;
	}
}
