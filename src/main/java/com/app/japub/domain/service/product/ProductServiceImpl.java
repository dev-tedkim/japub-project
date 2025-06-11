package com.app.japub.domain.service.product;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.japub.common.DbConstants;
import com.app.japub.domain.dao.product.ProductDao;
import com.app.japub.domain.dto.Criteria;
import com.app.japub.domain.dto.ProductDto;
import com.app.japub.domain.service.file.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ProductDao productDao;
	private final FileService fileService;
	private static final int THUMBNAIL_SIZE = 400;

	@Override
	public boolean insert(MultipartFile multipartFile, ProductDto productDto, File uploadPath, String datePath) {
		try {
			upload(multipartFile, productDto, uploadPath, datePath);
			return productDao.insert(productDto) == DbConstants.SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("productService insert error");
			return false;
		}
	}

	@Override
	public boolean deleteByProductNum(Long productNum) {
		try {
			return productDao.deleteByProductNum(productNum) == DbConstants.SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("productService deleteByProductNum error");
			return false;
		}
	}

	@Override
	public boolean update(MultipartFile multipartFile, ProductDto productDto, File uploadPath, String datePath) {
		try {
			System.out.println("업데이트 전" +productDto);
			if (!multipartFile.isEmpty()) {
				System.out.println("multipartFile.isEmpty 들어옴");
				upload(multipartFile, productDto, uploadPath, datePath);
			}
			System.out.println("업데이트 후" +productDto);
			return productDao.update(productDto) == DbConstants.SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("productService update error");
			return false;
		}
	}

	@Override
	public List<ProductDto> findByCriteria(Criteria criteria) {
		try {
			return productDao.findByCriteria(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("productService findByCriteria error");
			return Collections.emptyList();
		}
	}

	@Override
	public Long countByCriteria(Criteria criteria) {
		try {
			return productDao.countByCriteria(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("productService countByCriteria error");
			return 0l;
		}
	}

	@Override
	public void upload(MultipartFile multipartFile, ProductDto productDto, File uploadPath, String datePath) {
		String productUuid = UUID.randomUUID().toString();
		String originalProductName = multipartFile.getOriginalFilename();
		String productName = productUuid + "_" + originalProductName;
		File product = new File(uploadPath, productName);
		try {
			multipartFile.transferTo(product);
			productDto.setProductUuid(productUuid);
			productDto.setProductName(originalProductName);
			productDto.setProductUploadPath(datePath);
			if (!fileService.isImage(product)) {
				throw new RuntimeException("productService upload no image");
			}
			File thumbnailProduct = new File(uploadPath, "t_" + productName);
			fileService.createThumbnails(product, thumbnailProduct, THUMBNAIL_SIZE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("productService upload error", e);
		}
	}

	@Override
	public String getProductThumbnailUrl(ProductDto productDto) {
		return productDto.getProductUploadPath() + "/t_" + productDto.getProductUuid() + "_"
				+ productDto.getProductName();

	}

	@Override
	public int getProductDiscountPrice(ProductDto productDto) {
		return (int) (productDto.getProductPrice() * 0.9);
	}

	@Override
	public ProductDto findByProductNum(Long productNum) {
		try {
			return productDao.findByProductNum(productNum);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("productService findByProductNum error");
			return null;
		}
	}

	@Override
	public List<ProductDto> findByProductIsRecommend(boolean productIsRecommend) {
		try {
			return productDao.findByProductIsRecommend(productIsRecommend);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("productService findByProductIsRecommend error");
			return Collections.emptyList();
		}
	}

	@Override
	public boolean updateProductIsRecommend(Long productNum, boolean productIsRecommend) {
		try {
			return productDao.updateProductIsRecommend(productNum, productIsRecommend) == DbConstants.SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("productService updateProductIsRecommend error");
			return false;
		}
	}

	@Override
	public List<ProductDto> findByYesterDay() {
		try {
			return productDao.findByYesterDay();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("productService findByYesterDay error");
			return Collections.emptyList();
		}
	}

	@Override
	public void autoDeleteFiles(List<ProductDto> yesterdayProducts, String defaultPath, String datePath) {

		List<Path> paths = yesterdayProducts.stream().map(product -> Paths.get(defaultPath,
				product.getProductUploadPath(), product.getProductUuid() + "_" + product.getProductName()))
				.collect(Collectors.toList());

		yesterdayProducts.stream()
				.map(product -> Paths.get(defaultPath, product.getProductUploadPath(),
						"t_" + product.getProductUuid() + "_" + product.getProductName()))
				.collect(Collectors.toList()).forEach(paths::add);

		File[] deleteFiles = Paths.get(defaultPath, datePath).toFile()
				.listFiles(product -> !paths.contains(product.toPath()));

		if (deleteFiles != null) {
			Arrays.asList(deleteFiles).forEach(File::delete);
		}

	}

}
