package com.example.spring61.domain.service.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring61.domain.dao.board.BoardDao;
import com.example.spring61.domain.dto.BoardDto;
import com.example.spring61.domain.dto.Criteria;
import com.example.spring61.domain.dto.FileDto;
import com.example.spring61.domain.service.file.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	@Autowired
	private final BoardDao boardDao;
	@Autowired
	private final FileService fileService;
	private final int SUCCESS_CODE = 1;

	@Override
	public List<BoardDto> findAllByCriteria(Criteria criteria) {
		try {
			return boardDao.findAllByCriteria(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("boardService findAllByCriteria error");
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(BoardDto boardDto) {
		try {
			if (boardDao.insert(boardDto) != SUCCESS_CODE) {
				throw new RuntimeException("boardService insert error");
			}
			List<FileDto> files = boardDto.getFiles();
			if (files != null) {
				for (FileDto fileDto : files) {
					fileDto.setBoardNum(boardDto.getBoardNum());
					if (!fileService.insert(fileDto)) {
						throw new RuntimeException("boardService file insert error");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(BoardDto boardDto) {
		try {
			List<FileDto> files = boardDto.getFiles();
			if (files != null) {
				fileService.deleteByBoardNum(boardDto.getBoardNum());
				for (FileDto fileDto : files) {
					fileDto.setBoardNum(boardDto.getBoardNum());
					if (!fileService.insert(fileDto)) {
						throw new RuntimeException("boardService file update error");
					}
				}
			}
			if (boardDao.update(boardDto) != SUCCESS_CODE) {
				throw new RuntimeException("boardService update error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return true;
	}

	@Override
	public boolean delete(Long boardNum, Long userNum) {
		try {
			return boardDao.delete(boardNum, userNum) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("boardService delete error");
		}
		return false;
	}

	@Override
	public BoardDto findByBoardNum(Long boardNum) {
		try {
			return boardDao.findByBoardNum(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("boardService findByBoardNum error");
		}
		return null;
	}

	@Override
	public boolean updateBoardHitByBoardNum(Long boardNum) {
		try {
			return boardDao.updateBoardHitByBoardNum(boardNum) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("boardService updateBoardHitByBoardNum error");
		}
		return false;
	}

	@Override
	public int getTotal(Criteria criteria) {
		try {
			return boardDao.getTotal(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("boardService getTotal error");
		}
		return 0;
	}
}
