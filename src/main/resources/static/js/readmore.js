$(function() {
	$('.more-btn').on('click', function() {
		if ($(this).children().is('.open')) {
			$(this).html('<p class="exit">닫기</p>').addClass('close-btn');
			$(this).parent().removeClass('slide-up').addClass('slide-down');
		} else {
			$(this).html('<p class="open">더보기</p>').removeClass('close-btn');
			$(this).parent().removeClass('slide-down').addClass('slide-up');
		}
	});
});