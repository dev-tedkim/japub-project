package com.example.spring61.domain.service.file;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring61.domain.dao.file.FileDao;
import com.example.spring61.domain.dto.FileDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
	@Autowired
	private final FileDao fileDao;
	private final int SUCCESS_CODE = 1;

	@Override
	public List<FileDto> findByBoardNum(Long boardNum) {
		try {
			return fileDao.findByBoardNum(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fileService findByBoardNum error");
		}
		return null;
	}

	@Override
	public boolean insert(FileDto fileDto) {
		try {
			return fileDao.insert(fileDto) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fileService insert error");
		}
		return false;
	}

	@Override
	public int deleteByBoardNum(Long boardNum) {
		try {
			return fileDao.deleteByBoardNum(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fileService deleteByBoardNum error");
		}
		return 0;
	}
}
