const $productForm = $("form[name=productForm]");
const $productPostForm = $("form[name=productPostForm]");

$("a.page").on("click", function(e) {
	e.preventDefault();
	$productForm.find("input[name=page]").val($(this).attr("href"));
	$productForm.submit();
});


$("li.sort-key > a").on("click", function(e) {
	e.preventDefault();
	$productForm.find("input[name=sort]").val($(this).attr("href"));
	$productForm.find("input[name=page]").val(1);
	$productForm.submit();
});

$("a.product-delete-btn").on("click", function(e) {
	e.preventDefault();
	if (!confirm("정말로 삭제 하시겠습니까?")) { return; }
	const $btn = $(this);
	let productNum = $btn.closest("li").data("productNum");
	$productPostForm.attr("action", $btn.attr("href"));
	$productPostForm.find("input[name=productNum").val(productNum);
	$productPostForm.submit();
});

$("a.product-recommend-btn").on("click", function(e) {
	e.preventDefault();
	const $btn = $(this);
	const $li = $(this).closest("li");
	if ($btn.prop("disabled")) { return; }
	if (!confirm("이 상품을 추천 도서로 지정하시겠습니까?")) { return; }
	if ($li.data("isRecommend")) { alert("이미 추천 도서로 지정된 상품입니다."); return; }
	$btn.prop("disabled", true);
	let productNum = $li.data("productNum");
	$productPostForm.find("input[name=productNum]").val(productNum);
	$productPostForm.attr("action", $btn.attr("href")).submit();
});

$(".product-search-btn").on("click", function(e) {
	e.preventDefault();
	const $productSearchForm = $(this).closest("form");
	const keyword = $productSearchForm.find("input[name=keyword]").val().trim();
	if (!keyword) { alert("내용을 입력해 주세요."); return; }
	$productSearchForm.submit();
});
