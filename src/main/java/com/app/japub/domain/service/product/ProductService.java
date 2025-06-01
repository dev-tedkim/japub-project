package com.app.japub.domain.service.product;

import java.io.File;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.app.japub.domain.dto.Criteria;
import com.app.japub.domain.dto.FileDto;
import com.app.japub.domain.dto.ProductDto;

public interface ProductService {
	public abstract boolean insert(ProductDto productDto);
	public abstract boolean deleteByProductNum(Long productNum);
	public abstract boolean update(ProductDto productDto);
	public abstract List<ProductDto> findByCriteria(Criteria criteria);
	public abstract Long countByCriteria(Criteria criteria);
	public abstract ProductDto upload(MultipartFile multipartFile, ProductDto productDto, File uploadPath, String datePath);
	public abstract void setProductThumbnailUrl(List<ProductDto> products);
	public abstract void setProductDiscountPrice(List<ProductDto> products);
	public abstract ProductDto findByProductNum(Long productNum);
	public abstract List<ProductDto> findByProductIsRecommend(boolean productIsRecommend);
	public abstract boolean updateProductIsRecommend(@Param("productNum") Long productNum, @Param("productIsRecommend") boolean productIsRecommend);
	public abstract List<ProductDto> findByYesterDay();
	public abstract void autoDeleteFiles(List<ProductDto> yesterdayProducts, String defaultPath, String datePath);
	
}
