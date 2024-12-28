import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import ReactDOM from 'react-dom/client'

import './index.css'
import App from './App.jsx'

ReactDOM.createRoot(document.getElementById("root")  as HTMLInputElement).render(
  <StrictMode>
    <App />
  </StrictMode>,
);