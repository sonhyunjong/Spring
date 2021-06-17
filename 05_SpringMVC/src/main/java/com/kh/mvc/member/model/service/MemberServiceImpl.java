package com.kh.mvc.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.mvc.member.model.mapper.MemberMapper;
import com.kh.mvc.member.model.vo.Member;

// @Service("빈 ID를 지정")
@Service
//@Transactional
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberMapper mapper;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
//	@Autowired
//	private SqlSession session;

	@Override
	public Member login(String id, String pwd) {
		Member member = this.findById(id);
		
//		System.out.println(member.getPassword());
//		System.out.println(passwordEncoder.encode(pwd));
//		System.out.println(passwordEncoder.matches(pwd, member.getPassword()));
		
//		if(member != null && member.getPassword().equals(passwordEncoder.encode(pwd))) {
//			return member;			
//		} else {
//			return null;
//		}
		
		return member != null && 
				passwordEncoder.matches(pwd, member.getPassword()) ? member : null;
	}

	@Override
	@Transactional
	public int save(Member member) {
		int result = 0;
		
		if(member.getNo() != 0) {
			result = mapper.updateMember(member);
		} else {
			member.setPassword(passwordEncoder.encode(member.getPassword()));
			
			result = mapper.insertMember(member);
		}
		
//		if(true) {
//			throw new RuntimeException();
//		}		
		
		return result;
	}

	@Override
	public boolean validate(String id) {				
		
		return this.findById(id) != null;
	}

	@Override
	public Member findById(String id) {
		
		return mapper.selectMember(id);
	}

	@Override
	public int delete(int no) {
		
		return mapper.deleteMember(no);
	}
}