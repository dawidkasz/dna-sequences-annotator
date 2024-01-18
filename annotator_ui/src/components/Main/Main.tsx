import React, { useState } from 'react';
import { Checkbox } from '../Checkbox';
import Navigation from '../Navigation';
import { FaUserCircle } from 'react-icons/fa';

interface AnnotationRequestResponse {
  annotationId: string;
}

const Main: React.FC = () => {
  const allAlgorithms = ['PANGOLIN', 'SPiP', 'TEST'];
  const [selectedAlgorithms, setSelectedAlgorithms] = useState<string[]>(allAlgorithms);
  const [file, setFile] = useState<File | null>(null);
  const [message, setMessage] = useState("");
  const [messageSeverity, setMessageSeverity] = useState<"info" | "error">("info");

  const csvData = [
    { GENE: 'BRCA1', '#CHROM': 17, POS: 41276135, REF: 'T', ALT: 'G' },
    { GENE: 'BRCA1', '#CHROM': 17, POS: 41276135, REF: 'C', ALT: 'T' },
  ];

  const handleCheckboxChange = (algorithm: string) => {
    setSelectedAlgorithms((prevSelected) => {
      if (prevSelected.includes(algorithm)) {
        return prevSelected.filter((a) => a !== algorithm);
      } else {
        return [...prevSelected, algorithm];
      }
    });
  };

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFile: File | null = event.target.files?.[0] || null;
    setFile(selectedFile);
  };


  const handleUpload = async () => {
    if (file) {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('algorithms', selectedAlgorithms[0]);

      try {
        const response = await fetch('http://localhost:8080/annotate/csv', {
          method: 'POST',
          body: formData,
        });

        if (response.ok) {
          const data: AnnotationRequestResponse = await response.json();
          console.log('Success:', data);
          setMessage(`Żądanie zostało wysłane. Identyfikator: ${data.annotationId}`)
          setMessageSeverity("info");
        } else {
          console.error('Error:', response.statusText);
        }
      } catch (error) {
        setMessage("Wystąpił błąd. Spróbuj ponownie.")
        setMessageSeverity("error");
        console.error('Error:', error);
      }
    }
  };


  return (
      <div>
        <h1 className='header'>Panel adnotacji</h1>

        <div className="container">
          <div className="left-section">
            <FaUserCircle className="user-icon" />

            <label className="green-button">
              <input type="file" className="file-input" onChange={handleFileChange} />
              Załaduj plik
            </label>

            <button className="green-button" onClick={handleUpload}>Adnotuj</button>

            <p className={messageSeverity === "info" ? "msg-info" : "msg-error"}>
              {message}
            </p>

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
              W celu zaadnotowania pliku, kliknij przycisk <button className="green-button">Załaduj plik</button> i wybierz plik, który chcesz zaadnotować <br/>
              Wybierz algorytmy zaznaczając odpowiednie checkboxy, po czym kliknij przycisk <button className="green-button">Adnotuj</button> <br/><br/>
              Poniżej znajduje się przykładowy plik csv, który można poddać adnotacji. Widać w nim strukturę kolumn i przykładowe dane:
            </p>
            <div className="table-container">
              <table className="csv-table">
                <thead>
                <tr>
                  <th>GENE</th>
                  <th>#CHROM</th>
                  <th>POS</th>
                  <th>REF</th>
                  <th>ALT</th>
                </tr>
                </thead>
                <tbody>
                {csvData.map((row, rowIndex) => (
                    <tr key={rowIndex}>
                      <td>{row.GENE}</td>
                      <td>{row['#CHROM']}</td>
                      <td>{row.POS}</td>
                      <td>{row.REF}</td>
                      <td>{row.ALT}</td>
                    </tr>
                ))}
                </tbody>
              </table>
            </div>

            <p>
              Po zakończeniu działania możesz przejść do zakładki <button className="blue-button">Wyniki</button> i pobrać plik z zaanotowanymi wariantami
            </p>
          </div>
        </div>
      </div>
  );
};

export default Main;
