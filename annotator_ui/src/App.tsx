import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Main from './components/Main/Main';
import Results from './components/Results/Results';

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/adnotacje" element={<Main />} />
        <Route path="/wyniki" element={<Results />} />
        <Route path="/" element={<Main />} />
      </Routes>
    </Router>
  );
};

export default App;
