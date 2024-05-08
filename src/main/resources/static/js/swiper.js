var swiper = new Swiper(".swiper-roupas", {
  slidesPerView: 'auto',
  spaceBetween: 30,
  loop: true,
  autoplay: {
    delay: 1000,
    disableOnInteraction: false,
  },
  
  navigation: {
    nextEl: ".swiper-button-next",
    prevEl: ".swiper-button-prev",
  },
});