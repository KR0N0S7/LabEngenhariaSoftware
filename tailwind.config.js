const defaultTheme = require("tailwindcss/defaultTheme"); 
const plugin = require("tailwindcss/plugin");
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    container:{
      padding: '8rem',
      center: true
    },
    extend: {
      colors: {
        'gray':'#E4E4E4',
        'gray-dark':'#A9A9A9',
        'purple':'#7C3FFF',
        'skin-color' : '#f4d0a3',
      },
    },
  },
  plugins: [
    plugin(function ({ addVariant }) {
      addVariant("swiper", ".swiper&");
      addVariant("swiper-slide", ".swiper-slide&");
      addVariant("swiper-button-next", ".swiper-button-next&");
      addVariant("swiper-button-prev", ".swiper-button-prev&");
      addVariant("swiper-pagination", ".swiper-pagination&");
      addVariant("swiper-wrapper", ".swiper-wrapper&");
      addVariant("swiper-pagination-bullet", ".swiper-pagination-bullet&");
  }),
  ],
}

