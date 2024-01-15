import React from 'react';
import { useNavigate } from 'react-router-dom';

const Navigation: React.FC = () => {

    const navigate = useNavigate();

    const handleNavigation = (path: string) => {
        navigate(path);
    };
    return (
        <div className="navigation">
            <button className='nav-link' onClick={() => handleNavigation('/adnotacje')}>Adnotacje</button>
            <button className='nav-link' onClick={() => handleNavigation('/wyniki')}>Wyniki</button>
        </div>
    );
};

export default Navigation;
