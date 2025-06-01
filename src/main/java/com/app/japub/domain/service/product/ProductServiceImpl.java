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
import com.app.japub.common.MessageConstants;
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
	public boolean insert(ProductDto productDto) {
		try {
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
	public boolean update(ProductDto productDto) {
		try {
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
	public ProductDto upload(MultipartFile multipartFile, ProductDto productDto, File uploadPath, String datePath) {
		String productUuid = productDto.getProductUuid();
		String uuid = productUuid == null ? UUID.randomUUID().toString() : productUuid;
		String originalImageName = multipartFile.getOriginalFilename();
		String imageName = uuid + "_" + originalImageName;
		File productImage = new File(uploadPath, imageName);
		try {
			multipartFile.transferTo(productImage);
			if (productDto.getProductUuid() == null) {
				productDto.setProductUuid(uuid);
			}
			productDto.setProductImageName(originalImageName);
			productDto.setProductUploadPath(datePath);
			if (!fileService.isImage(productImage)) {
				throw new RuntimeException("productService upload no image");
			}
			File productThumbnailImage = new File(uploadPath, "t_" + imageName);
			fileService.createThumbnails(productImage, productThumbnailImage, THUMBNAIL_SIZE);
			return productDto;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("productService upload error", e);
		}
	}

	@Override
	public void setProductThumbnailUrl(List<ProductDto> products) {
		if (products == null || products.isEmpty()) {
			return;
		}
		products.forEach(product -> {
			String productThumbnailUrl = product.getProductUploadPath() + "/t_" + product.getProductUuid() + "_"
					+ product.getProductImageName();
			product.setProductThumbnailUrl(productThumbnailUrl);
		});
	}

	@Override
	public void setProductDiscountPrice(List<ProductDto> products) {
		if (products == null || products.isEmpty()) {
			return;
		}
		products.forEach(product -> {
			product.setProductDiscountPrice((int) (product.getProductPrice() * 0.9));
		});

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
				product.getProductUploadPath(), product.getProductUuid() + "_" + product.getProductImageName()))
				.collect(Collectors.toList());

		yesterdayProducts.stream()
				.map(product -> Paths.get(defaultPath, product.getProductUploadPath(),
						"t_" + product.getProductUuid() + "_" + product.getProductImageName()))
				.collect(Collectors.toList()).forEach(paths::add);

		File[] deleteFiles = Paths.get(defaultPath, datePath).toFile()
				.listFiles(product -> !paths.contains(product.toPath()));

		if (deleteFiles != null) {
			Arrays.asList(deleteFiles).forEach(File::delete);
		}

	}

}
