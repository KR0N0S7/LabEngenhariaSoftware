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
  plugins: [],
}

