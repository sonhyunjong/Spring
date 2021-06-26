package com.kh.mvc.board.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.mvc.board.model.vo.Board;
import com.kh.mvc.common.util.PageInfo;

public interface BoardService {

	int save(Board board);

	String saveFile(MultipartFile upfile, String savePath);

	int getBoardCount();

	List<Board> getBoardList(PageInfo pageInfo);

	Board findByNo(int boardNo);

	void deleteFile(String filePath);
	
}