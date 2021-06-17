package com.kh.mvc.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kh.mvc.member.model.vo.Member;

// @Repository
@Mapper
public interface MemberMapper {
	Member selectMember(@Param("id") String id);

	int insertMember(Member member);

	int updateMember(Member member);

	int deleteMember(@Param("no") int no);
}