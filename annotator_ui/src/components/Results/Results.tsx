import React, { useState } from 'react';
import Navigation from '../Navigation';

const Results: React.FC = () => {
  const [orderId, setOrderId] = useState('');
  const [result, setResult] = useState('');

  const handleOrderIdChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setOrderId(event.target.value);
  };

  const handleFetchResults = async () => {
    try {
      const response = await fetch(`http://localhost:8080/annotate/results/${orderId}`);
      const data = await response.json();
      setResult(data);
    } catch (error) {
      console.error('Error fetching results:', error);
    }
  };

  return (
    <div>
      <h1 className='header'>Wyniki</h1>

      <Navigation />
      <div className="container">
        <div className="content">
          <div className="left-section">
            <label htmlFor="orderId">Order ID:</label>
            <input
              type="text"
              id="orderId"
              value={orderId}
              onChange={handleOrderIdChange}
              placeholder="Enter Order ID"
              className="rounded-input"
            />
            <button className="green-button" onClick={handleFetchResults}>
              Akceptuj
            </button>
          </div>
          <div className="right-section">
            {result && (
              <div>
                <h2>Results:</h2>
                <p>{result}</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Results;
