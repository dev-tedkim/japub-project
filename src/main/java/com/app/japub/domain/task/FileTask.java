package com.app.japub.domain.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.japub.domain.service.file.FileService;
import com.app.japub.domain.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileTask {
	private final FileService fileService;
	private final ProductService ProductService;
	private static final String DOWNLOAD_DERECTORY = "C:/upload/download";
	private static final String PRODUCTS_DERECTORY = "C:/upload/products";

	@Scheduled(cron = "0 0 2 * * *")
	public void autoDeleteFiles() {
		System.out.println("cron");
		fileService.autoDeleteFiles(fileService.findByYesterDay(), DOWNLOAD_DERECTORY, getDatePath());
		ProductService.autoDeleteFiles(ProductService.findByYesterDay(), PRODUCTS_DERECTORY, getDatePath());
	}

	private String getDatePath() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		return new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime());
	}
}
