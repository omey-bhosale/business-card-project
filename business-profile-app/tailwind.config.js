/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  plugins: [],
  theme: {
  extend: {
    animation: {
      'fade-in': 'fadeIn 0.3s ease-in-out',
    },
    keyframes: {
      fadeIn: {
        from: { opacity: '0' },
        to: { opacity: '1' }
      }
    }
  }
}
}
