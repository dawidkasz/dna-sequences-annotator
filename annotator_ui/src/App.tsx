import React, {useEffect, useState} from 'react';
import './App.css';

interface CalculationResult {
  a: number;
  b: number;
  result: number;
}

function App() {
  const [num1, setNum1] = useState("0");
  const [num2, setNum2] = useState("0");
  const [calculations, setCalculations] = useState<CalculationResult[]>([]);

  useEffect(() => {
    const interval = setInterval(async() => {
      let response;
      try {
         response = await fetch("http://localhost:8080/list");
      } catch (e) {
        console.log(e);
        return;
      }

        const data: CalculationResult[] = await response.json();
        setCalculations(data);
    }, 500);

    return () => clearInterval(interval);
  }, []);

  const handleSubmit = async(e: React.SyntheticEvent) => {
    e.preventDefault();

    try {
      await fetch('http://localhost:8080/calculate', {
        method: 'POST',
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        body: JSON.stringify({a: num1, b: num2})
      });
    } catch (e) {
      console.log(e);
      return;
    }

    setNum1("0");
    setNum2("0");
  }

  return (
    <div style={{padding: 16}}>
      <div>
        <form onSubmit={handleSubmit}>
          Num 1: <input type={"number"} value={num1} onChange={e => setNum1(e.target.value)}/> <br/>
          Num 2: <input type={"number"} value={num2} onChange={e => setNum2(e.target.value)}/> < br/>
          <input type={"submit"} value={"Submit"}/>
        </form>
      </div>
      <div style={{marginTop: 24}}>
        {
          calculations.map(c =>
            <div>{c.a} + {c.b} = {c.result}</div>
          )
        }
      </div>
    </div>
  );
}

export default App;
