const slide_swiper = new Swiper('#slide-swiper', {
	// Optional parameters
	direction: 'horizontal',
	loop: true,

	// If we need pagination
	pagination: {
		el: '.swiper-pagination',
		clickable: true,
	},
	// Navigation arrows
	speed: 800,
	autoplay: {
		// 자동 슬라이드 설정 , 비 활성화 시 false
		delay: 3000, // 시간 설정
		disableOnInteraction: false, // false로 설정하면 스와이프 후 자동 재생이 비활성화 되지 않음
	},
});

var newbook_swiper = new Swiper('#book-swiper', {
	slidesPerView:4,
	navigation: {
		nextEl: '.swiper-button-next',
		prevEl: '.swiper-button-prev',
	}
});

