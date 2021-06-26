package com.kh.mvc.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.kh.mvc.board.model.vo.Board;

@Mapper
public interface BoardMapper {

	int selectBoardCount();	

	Board selectBoardByNo(@Param("boardNo") int boardNo);
	
	List<Board> selectBoardList(RowBounds rowBounds);
	
	int insertBoard(Board board);

	int updateBoard(Board board);
}