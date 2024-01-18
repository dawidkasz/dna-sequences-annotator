import React, { useState } from 'react';
import Navigation from '../Navigation';

interface OrderResult {
  orderId: string;
  annotations: any[];
}

const Results: React.FC = () => {
  const [orderId, setOrderId] = useState('');
  const [results, setResults] = useState<OrderResult[]>([]);

  const handleOrderIdChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setOrderId(event.target.value);
  };

  const handleFetchResults = async () => {
    try {
      const response = await fetch(`http://localhost:8080/annotate/results/${orderId}`);

      if (response.ok) {
        const data: OrderResult = await response.json();
        console.log("Response: ", data);
        setResults([data]);
      } else {
        setResults([]);
      }
    } catch (error) {
      console.error('Error fetching results:', error);
      setResults([]);
    }
  };

  const handleFetchAllResults = async () => {
    try {
      const response = await fetch(`http://localhost:8080/annotate/results/list`);

      if (response.ok) {
        const data: OrderResult[] = await response.json();
        console.log("Response: ", data);
        setResults(data);
      } else {
        setResults([]);
      }
    } catch (error) {
      console.error('Error fetching results:', error);
      setResults([]);
    }
  }

  const buildFileDownloadUrl = (data: any) => {
    return `data:text/plain;base64,${btoa(JSON.stringify(data))}`;
  }

  const buildFileDownloadName = (orderId: string) => {
    return `${orderId}.json`;
  }

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
            <button className="green-button" onClick={() => {
              if (orderId === "") {
                handleFetchAllResults()
              } else {
                handleFetchResults();
              }
            }}>
              Szukaj
            </button>
          </div>
          <div className="right-section">
            {
              (results.length > 0) ? results.map(result =>
                <div>
                  <div>Identyfikator: <b>{result.orderId}</b></div>
                  <div style={{marginTop: "8px"}}>
                    <a
                        download={buildFileDownloadName(result.orderId)}
                        href={buildFileDownloadUrl(result.annotations)}
                    >
                    Pobierz
                    </a>
                  </div>
                </div>
              ) : "Brak wyników"
            }
          </div>
        </div>
      </div>
    </div>
  );
};

export default Results;
