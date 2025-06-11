// 섬네일 이미지 미리보기 기능
const $thumbnail = $("#thumbnailPreview");
const $fileInput = $("input[name=multipartFile]");


$fileInput.on("change", function() {
	const file = $(this)[0].files[0];
	if (!file) { return; }
	if (!file.type.startsWith("image/")) {
		alert("이미지형식만 업로드 가능합니다.");
		$thumbnail.hide();
		$(this).val("");
		return;
	}
	const reader = new FileReader();
	reader.onload = (e) => {
		$thumbnail.attr("src", e.target.result).show();
	}
	reader.readAsDataURL(file);
});

$("button.product-register-btn").on("click", function(e) {
	e.preventDefault();
	const $btn = $(this);
	if ($btn.prop("disabled")) return;
	if (!validateProductForm(false)) { return; }
	$btn.prop("disabled", true);
	$btn.closest("form").submit();
});

function validateProductForm(isUpdate = false) {
	const $productForm = $("form[name=productForm]");
	if (!isUpdate) {
		if (!$productForm.find("input[type=file]")[0].files[0]) { alert("이미지를 업로드해 주세요."); return false; }
	}
	if (!$productForm.find("input[name=productTitle]").val()) { alert("제목을 입력해 주세요"); return false; }
	if (!$productForm.find("input[name=productPrice]").val()) { alert("가격을 입력해 주세요"); return false; }
	if (!$productForm.find("input[name=productUrl]").val()) { alert("url을 입력해 주세요"); return false; }
	if (!$productForm.find("select").val()) { alert("카테고리를 선택해 주세요"); return false; }
	return true;
}
