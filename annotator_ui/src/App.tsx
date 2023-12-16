import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Main from './components/Main/Main';
import History from './components/History/History';

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/adnotacje" element={<Main />} />
        <Route path="/historia" element={<History />} />
        <Route path="/" element={<Main />} />
      </Routes>
    </Router>
  );
};

export default App;
