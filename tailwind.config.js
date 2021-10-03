const defaultTheme = require('tailwindcss/defaultTheme')
module.exports = {
  mode: "jit",
  purge: {
   content: process.env.NODE_ENV == 'production' ? ["./resources/public/index.html", "./resources/public/js/main.js", "./resources/public/js/cljs-runtime/*.js"] : ["./resources/public/index.html", "./resources/public/js/cljs-runtime/*.js"]
  },
  darkMode: false,
  theme: {
    extend: {
      fontFamily: {
        'mono': ['"Source Code Pro"', 'Menlo', 'Monaco', 'Consolas', '"Liberation Mono"', '"Courier New"', 'monospace'],
        'sans': ['Lato', 'system-ui', '"Segoe UI"', 'Roboto', '"Helvetica Neue"']
      },
      flex: {
        '0': '0 1 0px',
        '1': '1 1 0px',
        '2': '2 1 0px',
        '3': '3 1 0px',
        '4': '4 1 0px',
        '5': '5 1 0px',
        '6': '6 1 0px',
        '7': '7 1 0px',
        '8': '8 1 0px',
        '9': '9 1 0px',
        '10': '10 1 0px',
        '11': '11 1 0px',
        '12': '12 1 0px',
      }}},
  variants: {},
  plugins: [
    require('@tailwindcss/forms')
  ]
}
