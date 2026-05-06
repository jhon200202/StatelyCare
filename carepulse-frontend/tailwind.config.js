/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#074469',
          container: '#2a5c82',
          onContainer: '#a5d4ff',
          fixed: '#cde5ff',
          dim: '#9ccbf7',
        },
        secondary: {
          DEFAULT: '#3a6848',
          container: '#bcefc6',
          onContainer: '#406e4d',
          fixed: '#bcefc6',
          dim: '#a0d2ab',
        },
        tertiary: {
          DEFAULT: '#653400',
          container: '#884800',
          onContainer: '#ffc394',
          fixed: '#ffdcc3',
          dim: '#ffb77d',
        },
        surface: {
          DEFAULT: '#f8f9ff',
          dim: '#cbdbf5',
          bright: '#f8f9ff',
          containerLowest: '#ffffff',
          containerLow: '#eff4ff',
          container: '#e5eeff',
          containerHigh: '#dce9ff',
          containerHighest: '#d3e4fe',
          variant: '#d3e4fe',
        },
        background: {
          DEFAULT: '#f8f9ff',
        },
        on: {
          primary: '#ffffff',
          secondary: '#ffffff',
          tertiary: '#ffffff',
          surface: '#0b1c30',
          surfaceVariant: '#41474e',
          background: '#0b1c30',
          error: '#ffffff',
        },
        error: {
          DEFAULT: '#ba1a1a',
          container: '#ffdad6',
          onContainer: '#93000a',
        },
        outline: {
          DEFAULT: '#72787f',
          variant: '#c1c7cf',
        }
      },
      fontFamily: {
        headline: ['Manrope', 'sans-serif'],
        body: ['Public Sans', 'sans-serif'],
      },
      spacing: {
        unit: '8px',
        gutter: '24px',
        page: '40px',
      },
      borderRadius: {
        sm: '0.25rem',
        DEFAULT: '0.5rem',
        md: '0.75rem',
        lg: '1rem',
        xl: '1.5rem',
      }
    },
  },
  plugins: [],
}
