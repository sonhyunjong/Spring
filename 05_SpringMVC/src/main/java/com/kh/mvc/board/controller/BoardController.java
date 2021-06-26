package com.kh.mvc.board.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.mvc.board.model.service.BoardService;
import com.kh.mvc.board.model.vo.Board;
import com.kh.mvc.common.util.PageInfo;
import com.kh.mvc.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService service; 
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@GetMapping("/list")
	public ModelAndView list(ModelAndView model,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {

		List<Board> list = null;
		PageInfo pageInfo = new PageInfo(page, 10, service.getBoardCount(), 10); 
		
		list = service.getBoardList(pageInfo);
		
		model.addObject("list", list);
		model.addObject("pageInfo", pageInfo);
		model.setViewName("board/list");
		
		return model;		
	}	
	
	
	@GetMapping("/view")
	public ModelAndView view(ModelAndView model,
			@RequestParam("no") int boardNo) {
		
		Board board = service.findByNo(boardNo);

		model.addObject("board", board);
		model.setViewName("board/view");
		
		return model;
	}
	
	/*
	 HttpEntity
	   - SpringFramework에서 제공하는 클래스로 HTTP 요청 또는 응답에 해당하는 HTTP Header와 HTTP Body를 포함하는 클래스이다.
	 
	 ResponseEntity
	   - HttpEntity를 상속하는 클래스로 HTTP 응답 데이터를 포함하는 클래스이다.
	   - 개발자가 직접 HTTP Body, Header, Status Code를 세팅하여 반환할 수 있다.
	   - 기본적으로 컨트롤러에 리턴값은 View의 이름을 반환함으로써 ViewResolver에서 해당 View를 랜더링하는데 컨트롤러에서 ResponseEntity를 리턴하게되면 View에 대한 정보가 아닌 HTTP 정보를 반환하게 된다.
	   - ResponseEntity 객체를 반환해주면 클라이언트 측에서 HTTP 정보를 받을 수 있게된다. 
	 
	 @ResponseBody와의 차이점
	   - @ResponseBody나 ResponseEntity를 리턴하는 것은 결과적으로 같은 기능이지만 구현 방법이 다르다.
	   - Header 값을 변경해야할 경우 @ResponseBody는 파라미터로 HttpServletResponse 객체를 받아서 이 객체를 통해서 Header 값을 변경시켜야한다.
	   - ResponseEntity에서는 객체를 생성한뒤 객체에서 Header 값을 변경시키면 된다.
	 */	
	
	@GetMapping("/fileDown")
	public ResponseEntity<Resource> fileDown(
			@RequestParam("oriname") String oriname, @RequestParam("rename") String rename,
			@RequestHeader(name = "user-agent")String header) {
		
		try {
			Resource resource = resourceLoader.getResource("resources/upload/board/" + rename);		
	    	String downName = null;
	    	boolean isMSIE = header.indexOf("MSIE") != -1 || header.indexOf("Trident") != -1;
	    	
//	    	if(!resource.exists()) {
//	    		return ResponseEntity.badRequest().build();
//	    	}	    	
	    	
	    	if (isMSIE) {
	    		downName = URLEncoder.encode(oriname, "UTF-8").replaceAll("\\+", "%20");
	    	} else {    		
	    		downName = new String(oriname.getBytes("UTF-8"), "ISO-8859-1");
	    	}

	    	return ResponseEntity.ok()
	    			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + downName + "\"")
	    			.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()))
	    			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
	    			.body(resource);
		} catch (IOException e) {
			e.printStackTrace();
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/update")
	public ModelAndView updateView(ModelAndView model,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@RequestParam("no") int boardNo) {
		
		Board board = service.findByNo(boardNo);
		
		if (loginMember.getNo() == board.getWriterNo()) {
			model.addObject("board", board);
			model.setViewName("board/update");
		} else {
			model.addObject("msg", "잘못된 접근입니다");
			model.addObject("location", "/board/list");
			model.setViewName("common/msg");
		}
		
		return model;
	}
	
	@PostMapping("/update")
	public ModelAndView update(ModelAndView model,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			HttpServletRequest request,
			@ModelAttribute Board board, @RequestParam("reloadFile") MultipartFile reloadFile) {
		
		int result = 0;
		
		if(loginMember.getId().equals(board.getWriterId())) {
			if(reloadFile != null && !reloadFile.isEmpty()) {
				String rootPath = request.getSession().getServletContext().getRealPath("resources");
				String savePath = rootPath + "/upload/board";
				
				if(board.getRenamedFileName() != null) {
					// 이전에 업로드된 첨부파일 삭제
					service.deleteFile(savePath + "/" + board.getRenamedFileName());
				}
				
				String renameFileName = service.saveFile(reloadFile, savePath);
				
				if(renameFileName != null) {
					board.setOriginalFileName(reloadFile.getOriginalFilename());
					board.setRenamedFileName(renameFileName);
				}
			}
			
			result = service.save(board);
			
			if(result > 0) {
				model.addObject("msg", "게시글이 정상적으로 수정되었습니다.");
				model.addObject("location", "/board/view?no=" + board.getNo());
			} else {
				model.addObject("msg", "게시글 수정을 실패하였습니다.");
				model.addObject("location", "/board/update?no=" + board.getNo());
			}
		} else {
			model.addObject("msg", "잘못된 접근입니다");
			model.addObject("location", "/board/list");
		}
		
		model.setViewName("common/msg");
		
		return model;
	}
	
	
	// 컨트롤러의 메소드의 리턴 타입이 void일 경우 Mapping URL을 유추해서 View 찾는다.
	@GetMapping("/write")
	public void writeView() {
		log.info("게시글 작성 페이지 요청");	
		
//		return "board/write";
	}
	
	@PostMapping("/write")
	public ModelAndView write(ModelAndView model, HttpServletRequest request,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
//			@ModelAttribute Board board, @RequestParam("upfile") MultipartFile[] upfile) {
			@ModelAttribute Board board, @RequestParam("upfile") MultipartFile upfile) {
		int result = 0;
		
		log.info("게시글 작성 요청");

		// 파일을 업로드하지 않으면 ""
		// 파일을 업로드하면 "파일명"
		System.out.println(upfile.getOriginalFilename());	
		// 파일을 업로드하지 않으면 true
		// 파일을 업로드하면 flase
		System.out.println(upfile.isEmpty());		
		
		if(loginMember.getId().equals(board.getWriterId())) {
			board.setWriterNo(loginMember.getNo());
			
			// 1. 파일을 업로드 했는지 확인 후 파일 업로드
			if(upfile != null && !upfile.isEmpty()) {
				// 파일을 저장하는 로직 작성
				String rootPath = request.getSession().getServletContext().getRealPath("resources");
				String savePath = rootPath + "/upload/board";				
				String renameFileName = service.saveFile(upfile, savePath);
				
				if(renameFileName != null) {
					board.setOriginalFileName(upfile.getOriginalFilename());
					board.setRenamedFileName(renameFileName);
				}
			}
			
			System.out.println(board);
			
			// 2. 데이터 베이스에 저장
			result = service.save(board);
			
			if(result > 0) {
				model.addObject("msg", "게시글이 정상적으로 등록되었습니다.");
				model.addObject("location", "/");
			} else {
				model.addObject("msg", "게시글이 등록을 실패하였습니다.");
				model.addObject("location", "/");
			}
		} else {
			model.addObject("msg", "잘못된 접근입니다");
			model.addObject("location", "/");
		}		
		
		model.setViewName("common/msg");
		
		return model;
	}
}