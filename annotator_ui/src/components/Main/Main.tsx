import React, { useState } from 'react';
import { Checkbox } from '../Checkbox';
import Navigation from '../Navigation';
import { FaUserCircle } from 'react-icons/fa';

const Main: React.FC = () => {
  const allAlgorithms = ['Algorytm1', 'Algorytm2', 'Algorytm3'];
  const [selectedAlgorithms, setSelectedAlgorithms] = useState<string[]>(allAlgorithms);

  const handleCheckboxChange = (algorithm: string) => {
    setSelectedAlgorithms((prevSelected) => {
      if (prevSelected.includes(algorithm)) {
        return prevSelected.filter((a) => a !== algorithm);
      } else {
        return [...prevSelected, algorithm];
      }
    });
  };

  return (
    <div>
      <h1 className='header'>Panel adnotacji</h1>

      <div className="container">
        <div className="left-section">
          <FaUserCircle className="user-icon" />

          {/* Styled file input */}
          <label className="file-input-label">
            <input type="file" className="file-input" />
            Załaduj plik
          </label>

          <div className='checkbox-container'>
            {allAlgorithms.map((algorithm) => (
              <Checkbox
                key={algorithm}
                label={algorithm}
                onChange={() => handleCheckboxChange(algorithm)}
                checked={selectedAlgorithms.includes(algorithm)}
              />
            ))}
          </div>
          <Navigation />
        </div>
        <div className="right-section">
          <h1>Krótki przewodnik</h1>
          <p>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
          </p>
        </div>
      </div>
    </div>
  );
};

export default Main;
