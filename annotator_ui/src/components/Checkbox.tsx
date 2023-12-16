import React from 'react';

interface CheckboxProps {
  label: string;
  onChange: () => void;
  checked: boolean;
}

export const Checkbox: React.FC<CheckboxProps> = ({ label, onChange, checked }) => {
  return (
    <label>
      <input type="checkbox" onChange={onChange} checked={checked} />
      {label}
    </label>
  );
};
