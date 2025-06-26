$("button.product-update-btn").on("click", function(e) {
	e.preventDefault();
	const $btn = $(this);
	if ($btn.prop("disabled")) return;
	if (!validateProductForm(true)) { return; }
	$btn.prop("disabled", true);
	$btn.closest("form").submit();
});