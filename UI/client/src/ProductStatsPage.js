import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './ProductStatsPage.css'; // Đảm bảo bạn đã import CSS
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, PieChart, Pie, LabelList, Cell,LineChart,Line } from 'recharts';


const ProductStatsPage = () => {
    const [tivis, setTivis] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showChart, setShowChart] = useState(false); // State để kiểm soát việc hiển thị biểu đồ
    const [chartType, setChartType] = useState('bar'); // Trạng thái lưu kiểu biểu đồ (bar hoặc pie)
    // Lấy dữ liệu từ API khi component được mount
    useEffect(() => {
        axios.get('http://localhost:5000/api/tivi') // Địa chỉ API backend
            .then(response => {
                setTivis(response.data); // Lưu dữ liệu vào state
                setLoading(false); // Đặt loading là false khi dữ liệu đã tải xong
            })
            .catch(err => {
                setError('Không thể tải dữ liệu');
                setLoading(false);
            });
    }, []);

    // Thống kê số lượng sản phẩm của từng hãng
    const manufacturers = [...new Set(tivis.map(tivi => tivi.manufacturer))];
    const manufacturerStats = manufacturers.map(manufacturer => ({
        name: manufacturer,
        count: tivis.filter(tivi => tivi.manufacturer === manufacturer).length
    }));

    // Phân khúc giá sản phẩm
    const priceRanges = [
        { range: 'Dưới 5 triệu', count: tivis.filter(tivi => tivi.price < 5000000).length },
        { range: '5 triệu - 10 triệu', count: tivis.filter(tivi => tivi.price >= 5000000 && tivi.price < 10000000).length },
        { range: '10 triệu - 15 triệu', count: tivis.filter(tivi => tivi.price >= 10000000 && tivi.price < 15000000).length },
        { range: 'Trên 15 triệu', count: tivis.filter(tivi => tivi.price >= 15000000).length }
    ];

    // Tính giá cao nhất và thấp nhất
    const highestPrice = Math.max(...tivis.map(tivi => tivi.price));
    const lowestPrice = Math.min(...tivis.map(tivi => tivi.price));

    // Tổng số sản phẩm
    const totalProducts = tivis.length;

    // Dữ liệu cho biểu đồ giá
    const priceData = [
        { name: 'Giá thấp nhất', price: lowestPrice },
        { name: 'Giá cao nhất', price: highestPrice }
    ];
// Xử lý chuyển đổi kiểu biểu đồ
    const toggleChartType = () => {
        // Xóa màu đã tạo khi chuyển đổi biểu đồ
        generatedColors.length = 0;  // Xóa tất cả màu trong mảng

        // Chuyển kiểu biểu đồ: từ cột -> hình tròn -> đường -> cột
        if (chartType === 'bar') {
            setChartType('pie'); // Nếu đang ở dạng cột, chuyển sang hình tròn
        } else if (chartType === 'pie') {
            setChartType('line'); // Nếu đang ở dạng hình tròn, chuyển sang đường
        } else {
            setChartType('bar'); // Nếu đang ở dạng đường, chuyển sang cột
        }
    };

// Mảng lưu trữ các màu đã được tạo ra
    const generatedColors = [];

    const getRandomColor = () => {
        const letters = '0123456789ABCDEF';
        let color = '#';
        for (let i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }

        // Kiểm tra nếu màu này đã tồn tại, nếu có thì tạo màu khác
        while (generatedColors.includes(color)) {
            color = '#';
            for (let i = 0; i < 6; i++) {
                color += letters[Math.floor(Math.random() * 16)];
            }
        }

        // Lưu màu vào mảng để đảm bảo không trùng lặp
        generatedColors.push(color);

        return color;
    };
    return (
        <div className="container">
            <h1>Thống Kê Sản Phẩm TV</h1>

            {/* Thống kê phân khúc giá */}
            {/* Thống kê phân khúc giá */}
            <div className="stats">
                <h3>Thống Kê Phân Khúc Giá</h3>
                <ul>
                    {priceRanges.map((range, index) => (
                        <li key={index}>{range.range}: {range.count} sản phẩm</li>
                    ))}
                </ul>

                {/* Thống kê sản phẩm theo hãng */}
                <h3>Thống Kê Sản Phẩm Theo Hãng</h3>
                <ul>
                    {manufacturerStats.map((stat, index) => (
                        <li key={index}>{stat.name}: {stat.count} sản phẩm</li>
                    ))}
                </ul>

                {/* Thống kê giá cao nhất và thấp nhất */}
                <h3>Thống Kê Giá</h3>
                <ul>
                    <li>Giá cao nhất: {highestPrice.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })}</li>
                    <li>Giá thấp nhất: {lowestPrice.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })}</li>
                </ul>

                {/* Tổng số sản phẩm */}
                <h3>Tổng Số Sản Phẩm</h3>
                <ul>
                    <li>Tổng số sản phẩm: {totalProducts}</li>
                </ul>
            </div>
            {/* Nút hiển thị biểu đồ */}
            <button className="button" onClick={() => setShowChart(!showChart)}>
                {showChart ? 'Ẩn Biểu Đồ' : 'Hiển Thị Biểu Đồ'}
            </button>

            {/* Nút chuyển đổi kiểu biểu đồ (chỉ hiển thị khi showChart = true) */}
            {showChart && (
                <button onClick={toggleChartType}>
                    Chuyển sang biểu đồ {chartType === 'bar' ? 'Hình Tròn' : chartType === 'line' ? 'Cột' : 'Đường'}
                </button>
            )}


            {showChart && (
                <div className="charts">

                    {/* Biểu đồ phân khúc giá */}
                    <div className="chart-section">
                        <h3>Biểu Đồ Phân Khúc Giá</h3>
                        <ResponsiveContainer width="100%" height={400}>
                            {chartType === 'bar' ? (
                                <BarChart data={priceRanges}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="range" />
                                    <YAxis />
                                    <Tooltip />
                                    <Legend />
                                    <Bar dataKey="count" fill="#8884d8">
                                        <LabelList dataKey="count" position="top" />
                                    </Bar>
                                </BarChart>
                            ) : chartType === 'line' ? (
                                <LineChart data={priceRanges}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="range" />
                                    <YAxis />
                                    <Tooltip />
                                    <Legend />
                                    <Line type="monotone" dataKey="count" stroke="#8884d8" />
                                </LineChart>
                            ) : (
                                <PieChart>
                                    <Pie
                                        data={priceRanges}
                                        dataKey="count"
                                        nameKey="range"
                                        cx="50%"
                                        cy="50%"
                                        outerRadius={150}
                                        label
                                        isAnimationActive={false}
                                    >
                                        {priceRanges.map((entry, index) => (
                                            <Cell key={`cell-${index}`} fill={getRandomColor()} />
                                        ))}
                                    </Pie>
                                    {/* Thêm Legend để hiển thị chú thích */}
                                    <Legend
                                        iconType="circle"
                                        layout="vertical"
                                        verticalAlign="middle"
                                        align="right"
                                        formatter={(value, entry, index) => (
                                            <span style={{ color: entry.payload.fill }}>
                  {value}
                </span>
                                        )}
                                    />
                                </PieChart>
                            )}
                        </ResponsiveContainer>
                    </div>

                    {/* Biểu đồ số lượng sản phẩm theo hãng */}
                    <div className="chart-section">
                        <h3>Biểu Đồ Số Lượng Sản Phẩm Theo Hãng</h3>
                        <ResponsiveContainer width="100%" height={400}>
                            {chartType === 'bar' ? (
                                <BarChart data={manufacturerStats}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="name" />
                                    <YAxis />
                                    <Tooltip />
                                    <Legend />
                                    <Bar dataKey="count" fill="#82ca9d">
                                        <LabelList dataKey="count" position="top" />
                                    </Bar>
                                </BarChart>
                            ) : chartType === 'line' ? (
                                <LineChart data={manufacturerStats}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="name" />
                                    <YAxis />
                                    <Tooltip />
                                    <Legend />
                                    <Line type="monotone" dataKey="count" stroke="#82ca9d" />
                                </LineChart>
                            ) : (
                                <PieChart>
                                    <Pie
                                        data={manufacturerStats}
                                        dataKey="count"
                                        nameKey="name"
                                        cx="50%"
                                        cy="50%"
                                        outerRadius={150}
                                        label
                                        isAnimationActive={false}
                                    >
                                        {manufacturerStats.map((entry, index) => (
                                            <Cell key={`cell-${index}`} fill={getRandomColor()} />
                                        ))}
                                    </Pie>
                                    {/* Thêm Legend để hiển thị chú thích */}
                                    <Legend
                                        iconType="circle"
                                        layout="vertical"
                                        verticalAlign="middle"
                                        align="right"
                                        formatter={(value, entry, index) => (
                                            <span style={{ color: entry.payload.fill }}>
                  {value}
                </span>
                                        )}
                                    />
                                </PieChart>
                            )}
                        </ResponsiveContainer>
                    </div>

                    {/* Biểu đồ giá cao nhất và thấp nhất */}
                    <div className="chart-section">
                        <h3>Biểu Đồ Giá Cao Nhất Và Thấp Nhất</h3>
                        <ResponsiveContainer width="100%" height={400}>
                            {chartType === 'bar' ? (
                                <BarChart data={priceData}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="name" />
                                    <YAxis />
                                    <Tooltip />
                                    <Legend />
                                    <Bar dataKey="price" fill="#ff7300">
                                        <LabelList dataKey="price" position="top" />
                                    </Bar>
                                </BarChart>
                            ) : chartType === 'line' ? (
                                <LineChart data={priceData}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="name" />
                                    <YAxis />
                                    <Tooltip />
                                    <Legend />
                                    <Line type="monotone" dataKey="price" stroke="#ff7300" />
                                </LineChart>
                            ) : (
                                <PieChart>
                                    <Pie
                                        data={priceData}
                                        dataKey="price"
                                        nameKey="name"
                                        cx="50%"
                                        cy="50%"
                                        outerRadius={150}
                                        label
                                        isAnimationActive={false}
                                    >
                                        {priceData.map((entry, index) => (
                                            <Cell key={`cell-${index}`} fill={getRandomColor()} />
                                        ))}
                                    </Pie>
                                    {/* Thêm Legend để hiển thị chú thích */}
                                    <Legend
                                        iconType="circle"
                                        layout="vertical"
                                        verticalAlign="middle"
                                        align="right"
                                        formatter={(value, entry, index) => (
                                            <span style={{ color: entry.payload.fill }}>
                  {value}
                </span>
                                        )}
                                    />
                                </PieChart>
                            )}
                        </ResponsiveContainer>
                    </div>

                </div>
            )}


        </div>
    );
};

export default ProductStatsPage;