import React from 'react';
import Navigation from '../Navigation';

const annotatedFiles = [
  {
    id: 1,
    name: "File1",
  },
  {
    id: 2,
    name: "File2",
  },
  {
    id: 3,
    name: "File3",
  },
  {
    id: 4,
    name: "File4",
  },
  {
    id: 5,
    name: "File5",
  },
  {
    id: 6,
    name: "File6",
  },
  {
    id: 7,
    name: "File7",
  },
  {
    id: 8,
    name: "File8",
  },
  {
    id: 9,
    name: "File9",
  },
  {
    id: 10,
    name: "File10",
  },
  {
    id: 11,
    name: "File11",
  },
  {
    id: 12,
    name: "File12",
  },
  {
    id: 13,
    name: "File13",
  },
  {
    id: 14,
    name: "File14",
  },
];

const History: React.FC = () => {

  const handleDownload = (fileName: string) => {
    console.log(`Downloading ${fileName}`);
  };

  return (
    <div>
      <h1 className='header'>Historia</h1>

      <Navigation />
      <div className="container">
        <div className="content">
          {annotatedFiles.map((file) => (
            <div key={file.id} className="history-item" onClick={() => handleDownload(file.name)}>
              <span>{file.name}</span>
              <button className="download-button">Pobierz</button>
            </div>
          ))}
        </div>
      </div>
    </div>

  );
};

export default History;
