package com.kh.mvc.member.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.kh.mvc.member.model.service.MemberService;
import com.kh.mvc.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
//@RestController
@SessionAttributes("loginMember")
public class MemberController {
	
/*
	사용자의 파라미터를 전송받는 방법(요청 시 전달밧을 처리하는 방법)
	  
	1. HttpServletRequest를 통해서 전송받기(기존 JSP/Servlet 방식을 사용)
	  - 메소드에 매개변수로 HttpServletRequest를 작성하면 메소드 실행 시 
	    스프링 컨테이너가 자동으로 객체를 인자로 주입해준다.
	
	@RequestMapping(value = "/login", method = {RequestMethod.POST})
	public String login(HttpServletRequest request) {
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		
		log.info("{}, {}", userId, userPwd);
		
		return "home";
	}
	2. @RequestParam 어노테이션 방식
	  - 스프링에서 더 간편하게 파라미터를 받아올 수 있는 방법 중 하나이다.
	  - Request 객체를 이용해서 데이터를 전송받는 방법이다.
	  - 파라미터 name 속성에 없는 값이 넘어올 경우 에러발생하지만 @RequestParam(required = false)로 지정하면 null 값을 넘겨준다.
	  - defaultValue 속성을 사용하면 파라미터 name 속성에 값이 없을 경우 기본값을 지정할 수 있다.
	  - 단, 매개변수의 이름과 name값이 동일하게 설정된 경우 자동으로 주입된다.
	    (어노테이션을 사용하는 것이 아니기 때문에 defaultValue, required 설정이 불가능하다.)
	@RequestMapping(value = "/login", method = {RequestMethod.POST})
	public String login(@RequestParam("userId")String userId, 
						@RequestParam(value = "userPwd", defaultValue = "1234")String userPwd,
						@RequestParam(value="addr", required = false) String addr) {
//	public String login(String userId, String userPwd) {
		
		log.info("{}, {}", userId, userPwd);
		System.out.println(addr);
		
		return "home";
	}	
	3. @ModelAttribute 어노테이션 방식
	  - 요청 파라미터가 많은 경우 객체 타입으로 넘겨 받는 방법으로 커맨드 객체 방식이라고 한다.
	  - 파라미터 name 속성의 값과 동일한 인스턴스 변수명을 가진 변수에 값을 주입해준다.
	  - 어노테이션을 생략해도 자동으로 객체로 매핑되지만 가독성을 위해서 적어주는 것이 좋다.
	@RequestMapping(value = "/login", method = {RequestMethod.POST})
	public String login(@ModelAttribute Member member) {
//	public String login(Member member) {
				
		log.info("{}, {}", member.getUserId(), member.getUserPwd());
		
		return "home";
	}
	
	4. 패스 파라미터로 요청 받기
	  - URL 패스상에 있는 특정 값을 가져오기 위해 사용하는 방법
	  - @PathVariable 어노테이션을 사용한다.
	  - @PathVariable 어노테이션의 괄호 부분은 생략이 되지만 어노테이션 자체는 생략이 되지 않는다.
	    (단, 패스 파라미터 이름과 매개 변수의 이름이 동일한 경우)
	    
	@RequestMapping("board/{id}")
//	public String getBoard(@PathVariable("id") int boardId) {
	public String getBoard(@PathVariable int id) {
		
//		log.info("Board ID : {}", boardId);
		log.info("Board ID : {}", id);
		
		return "home";
	}
 */
	
	@Autowired
	private MemberService service;
	
/*	로그인 처리	
	 1. HttpSession과 Model 객체 사용
	   - Model이라는 객체는 컨트롤러에서 데이터를 뷰로 전달하고자 할 때 사용하는 객체이다.
	     전달하고자하는 데이터를 맵형태(key, value)로 담을 수 있다.
	     데이터를 담을 때는 setAttribute()가 아닌 addAttribute()를 사용한다.
	   - Model 객체의 scope는 request이다.
	@RequestMapping(value = "/login", method = {RequestMethod.POST})
	public String login(HttpSession session, Model model,
			@RequestParam("userId")String userId, @RequestParam("userPwd")String userPwd) {
		
		log.info("{}, {}", userId, userPwd);	
		
		Member loginMember =  service.login(userId, userPwd);
		
		if(loginMember != null) {
			session.setAttribute("loginMember", loginMember);
			
			
			 // return "home";
			 //   - forwarding 방식으로 여기서 리턴한 view 명이 ViewResolver에 의해 WEB-INF/views/home.jsp로 요청을 넘긴다.
			  
			 // return "redirect:/";
			 //  - redirect 방식으로 여기서 리턴한 경로로 브라우저에서 다시 요청 하도록 반환한다.
			
			return "redirect:/";
		} else {
			model.addAttribute("msg", "아이디나 패스워드가 일치하지 않습니다.");
			model.addAttribute("location", "/");
			
			return "common/msg";
		}
	}
	
	// 로그아웃 처리 1 (기존 서블릿 방식)
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		
		session.invalidate();
		
		return "redirect:/";
	}	
	
 
 	2. @SessionAttributes과 ModelAndView 객체를 사용
 	  @SessionAttributes
 	    - @SessionAttributes("키값")은 Model 객체에 Attribute로 해당 키값으로 담기는 데이터를 세션 scope까지 범위를 확장시킨다.
 	    - 기존 방법으로 Session을 정리할 수 없고 SessionStatus 객체를 통해서 세션 scope까지 범위가 확장된 데이터를 정리할 수 있다.
 	      (session.invalidate() - X, status.setComplete() - O)
 	
 	  ModelAndView
 	    - ModelAndView 객체는 컨트롤러에서 데이터를 뷰로 전달하는 기능과 forward 할 뷰에 정보를 담는 객체이다.
 	    - addAttribute()가 아닌 addObject()를 통해서 데이터를 담을 수 있다.
 */
	@RequestMapping(value = "/login", method = {RequestMethod.POST})
	public ModelAndView login(ModelAndView model,
			@RequestParam("userId")String userId, @RequestParam("userPwd")String userPwd) {
		
		log.info("{}, {}", userId, userPwd);
		
		Member loginMember =  service.login(userId, userPwd);
		
		if(loginMember != null) {
			model.addObject("loginMember", loginMember);
			model.setViewName("redirect:/");
		} else {
			model.addObject("msg", "아이디나 패스워드가 일치하지 않습니다.");
			model.addObject("location", "/");
			model.setViewName("common/msg");
		}
		
		return model;
	}
	
	// 로그아웃 처리 2 (SessionStatus 사용)
	@RequestMapping("/logout")
	public String logout(SessionStatus status) {
		
		log.info("status.isComplete() : " + status.isComplete());

		status.setComplete();
		
		log.info("status.isComplete() : " + status.isComplete());		
		
		return "redirect:/";
	}
	
//	@RequestMapping(value = "/member/enroll", method = {RequestMethod.GET})
	@GetMapping("/member/enroll")
	public String enrollView() {
		log.info("회원가입 페이지 요청");
		
		return "member/enroll";
	}	
	
	
	@RequestMapping(value = "/member/enroll", method = {RequestMethod.POST})
	public ModelAndView enroll(ModelAndView model, @ModelAttribute Member member) {

		int result = service.save(member);		
		
		if(result > 0) {
			model.addObject("msg", "회원가입이 정상적으로 완료되었습니다.");
			model.addObject("location", "/");
		} else {
			model.addObject("msg", "회원가입을 실패하였습니다.");
			model.addObject("location", "/member/enroll");
		}
		
		model.setViewName("common/msg");
		
		return model;
	}
	
	/*
	  @ResponseBody
	    - 일반적으로 컨트롤러 메소드의 반환형이 String일 경우 뷰의 이름을 반환한다.
	    - 하지만 @ResponseBody 붙은 컨트롤러 메소드의 String 반환은 해당 요청을 보낸 클라이언트에 전달할 데이터를 의미한다.
	    - 즉, @ResponseBody 사용하면 클라이언트에 View를 리턴하지 않고 컨트롤러에서 직접 데이터를 리턴할 수 있다.
	      (스프링 4.0 부터는 @RestController 어노테이션을 제공해주는데 @RestController 선언해주면 
	       컨트롤러 클래스의 각 메소드마다 @ResponseBody 어노테이션이 기본으로 작동된다.)
	       
	  jackson 라이브러리
	    - 자바 객체를 JSON 형태의 데이터로 변환하기 위한 라이브러리이다.
	    - 스프링에서는 jackson 라이브러리를 추가하고 @ResponseBody을 사용하면 리턴되는 객체를 자동으로 JSON으로 변경해서 응답해준다.
	*/
	@GetMapping("/member/idCheck")
//	@ResponseBody
//	public Map<String, Object> idCheck(@RequestParam("id") String userId) {
	public ResponseEntity<Map<String, Object>> idCheck(@RequestParam("id") String userId) {
		log.info("User ID : {}", userId);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("validate", service.validate(userId));
		
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
	}
	
	@GetMapping("/member/view")
	public String view() {
		log.info("회원정보 페이지 요청");
		
		return "member/view";
	}
	
	@PostMapping("/member/update")
	public ModelAndView update(ModelAndView model, 
			@ModelAttribute Member member,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember) {
		int result = 0;
		
		if(loginMember.getId().equals(member.getId())) {
			member.setNo(loginMember.getNo());
			
			result = service.save(member);		
			
			if(result > 0) {
				model.addObject("loginMember" , service.findById(loginMember.getId()));
				model.addObject("msg", "회원정보 수정을 완료했습니다.");
				model.addObject("location", "/member/view");
			} else {
				model.addObject("msg", "회원정보 수정에 실패했습니다.");
				model.addObject("location", "/member/view");
			}			
		} else {
			model.addObject("msg", "잘못된 접근입니다");
			model.addObject("location", "/");
		}
		
		model.setViewName("common/msg");
		
		return model;
	}
	
	@GetMapping("/member/delete")
	public ModelAndView delete(ModelAndView model,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember) {
		
		int result = service.delete(loginMember.getNo());
		
		if(result > 0) {
			model.addObject("msg", "정상적으로 탈퇴되었습니다.");
			model.addObject("location", "/logout");
		} else {
			model.addObject("msg", "회원 탈퇴를 실패하였습니다.");
			model.addObject("location", "/member/view");
		}	
		
		model.setViewName("common/msg");
		
		return model;
	}
	
	
	
	
	
	
	
	
}