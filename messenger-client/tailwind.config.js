/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    extend: {
      boxShadow: {
        'right': '5px 0px 10px -5px rgba(0, 0, 0, 0.1)', // Adjust values as needed
      },
    },
  },
  plugins: [require('daisyui')],
};
