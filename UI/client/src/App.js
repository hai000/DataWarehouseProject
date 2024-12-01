import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import TiviList from './TiviList';
import ProductStatsPage from './ProductStatsPage';
import './App.css'; // Đảm bảo bạn import file CSS

function App() {
    return (
        <Router>
            <div>
                {/* Navigation */}
                <nav className="app__nav">
                    <ul className="app__nav-list">
                        <li>
                            <Link to="/tivi">Danh sách Tivi</Link>
                        </li>
                        <li>
                            <Link to="/product-stats">Thống kê Sản phẩm</Link>
                        </li>
                    </ul>
                </nav>

                {/* Nội dung chính */}
                <div className="app__content">
                    <Routes>
                        <Route path="/tivi" element={<TiviList />} />
                        <Route path="/product-stats" element={<ProductStatsPage />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;
