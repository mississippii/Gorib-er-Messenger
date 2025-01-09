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
    if (!formData.password.trim()) {
      newErrors.password = 'Password is required';
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
      <div className="w-full max-w-md p-8 space-y-6 bg-white shadow-lg rounded-lg">
        <h2 className="text-2xl font-bold text-center text-gray-800">Login</h2>
        <form onSubmit={handleSignIn} className="space-y-4">
          <div className="space-y-1">
            <label htmlFor="name" className="text-sm font-medium text-gray-600">
              Name
            </label>
            <input
              id="name"
              name="name"
              type="text"
              placeholder="Enter your name"
              value={formData.name}
              onChange={handleChange}
              className={`w-full px-4 py-2 text-gray-800 bg-gray-50 border ${
                errors.name ? 'border-red-500' : 'border-gray-300'
              } rounded-lg focus:ring focus:ring-blue-300 focus:outline-none`}
            />
            {errors.name && (
              <p className="text-sm text-red-500">{errors.name}</p>
            )}
          </div>
          <div className="space-y-1">
            <label
              htmlFor="password"
              className="text-sm font-medium text-gray-600"
            >
              Password
            </label>
            <input
              id="password"
              name="password"
              type="password"
              placeholder="Enter your password"
              value={formData.password}
              onChange={handleChange}
              className={`w-full px-4 py-2 text-gray-800 bg-gray-50 border ${
                errors.password ? 'border-red-500' : 'border-gray-300'
              } rounded-lg focus:ring focus:ring-blue-300 focus:outline-none`}
            />
            {errors.password && (
              <p className="text-sm text-red-500">{errors.password}</p>
            )}
          </div>
          <div className="flex items-center justify-between">
            <button
              type="submit"
              disabled={!formData.name || !formData.password}
              className={`w-full px-4 py-2 font-semibold text-white rounded-lg focus:ring focus:ring-blue-300 focus:outline-none ${
                formData.name && formData.password
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
