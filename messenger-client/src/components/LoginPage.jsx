import React, { useState } from 'react';

const LoginPage = ({ sendLoginDetails }) => {
  const [formData, setFormData] = useState({
    name: '',
    password: '',
  });

  const [errors, setErrors] = useState({});

  // Handle input change and update formData
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  // Form validation logic
  const validateForm = () => {
    const newErrors = {};
    if (!formData.name.trim()) {
      newErrors.name = 'Name is required';
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSignIn = (e) => {
    e.preventDefault();
    if (validateForm()) {
      sendLoginDetails(formData);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="w-full max-w-sm py-8 px-12 space-y-6 bg-white shadow-lg rounded-lg">
        <h2 className="text-2xl font-bold text-center text-gray-800">Login</h2>
        <form onSubmit={handleSignIn} className="space-y-4">
          <div className="space-y-1">
            <input
              id="name"
              name="name"
              type="text"
              placeholder="Enter your name"
              value={formData.name}
              onChange={handleChange}
              className={`w-full px-4 py-2 text-gray-800 border-b bg-transparent outline-none focus:border-blue-400 focus:bg-transparent`}
            />
            {errors.name && (
              <p className="text-sm text-red-500">{errors.name}</p>
            )}
          </div>
          <div className="flex items-center justify-between">
            <button
              type="submit"
              disabled={!formData.name}
              className={`w-full px-4 py-2 font-semibold text-white rounded-lg focus:ring focus:ring-blue-300 focus:outline-none ${
                formData.name
                  ? 'bg-blue-600 hover:bg-blue-700'
                  : 'bg-gray-400 cursor-not-allowed'
              }`}
            >
              Login
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;
