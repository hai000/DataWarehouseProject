import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './ProductStatsPage.css'; // Đảm bảo bạn đã import CSS
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, PieChart, Pie, LabelList, Cell, LineChart, Line } from 'recharts';

const ProductStatsPage = () => {
    const [stats, setStats] = useState({
        total: 0,
        tiviType: [],
        screenSize: [],
        imageTechnology: [],
        priceRange: [],
        releaseYear: [],
        warrantyPeriod: [],
        soundTechnology: [],
        averagePrice: 0,
        manufacturer: [],
        highestPrice: 0, // Thêm biến highestPrice
        lowestPrice: 0  // Thêm biến lowestPrice
    });

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [chartType, setChartType] = useState('bar');
    const [showChart, setShowChart] = useState(false);

    // Lấy dữ liệu từ API khi component được mount
    useEffect(() => {
        const fetchStats = async () => {
            try {
                const [
                    totalRes,
                    tiviTypeRes,
                    screenSizeRes,
                    imageTechnologyRes,
                    priceRangeRes,
                    releaseYearRes,
                    warrantyPeriodRes,
                    soundTechnologyRes,
                    averagePriceRes,
                    manufacturerRes, // Thêm phần gọi API thống kê nhãn hiệu
                    highestPriceRes, // Thêm phần gọi API thống kê giá cao nhất
                    lowestPriceRes   // Thêm phần gọi API thống kê giá thấp nhất
                ] = await Promise.all([
                    axios.get('http://localhost:5000/api/stats/total'),
                    axios.get('http://localhost:5000/api/stats/tivi-type'),
                    axios.get('http://localhost:5000/api/stats/screen-size'),
                    axios.get('http://localhost:5000/api/stats/image-technology'),
                    axios.get('http://localhost:5000/api/stats/price-range'),
                    axios.get('http://localhost:5000/api/stats/release-year'),
                    axios.get('http://localhost:5000/api/stats/warranty-period'),
                    axios.get('http://localhost:5000/api/stats/sound-technology'),
                    axios.get('http://localhost:5000/api/stats/average-price'),
                    axios.get('http://localhost:5000/api/stats/manufacturer'),
                    axios.get('http://localhost:5000/api/stats/highest-price'), // Gọi API thống kê giá cao nhất
                    axios.get('http://localhost:5000/api/stats/lowest-price')  // Gọi API thống kê giá thấp nhất
                ]);

                setStats({
                    total: totalRes.data.total,
                    tiviType: tiviTypeRes.data,
                    screenSize: screenSizeRes.data,
                    imageTechnology: imageTechnologyRes.data,
                    priceRange: priceRangeRes.data,
                    releaseYear: releaseYearRes.data,
                    warrantyPeriod: warrantyPeriodRes.data,
                    soundTechnology: soundTechnologyRes.data,
                    averagePrice: averagePriceRes.data.average_price,
                    manufacturer: manufacturerRes.data,
                    highestPrice: highestPriceRes.data.highest_price, // Lưu giá cao nhất
                    lowestPrice: lowestPriceRes.data.lowest_price   // Lưu giá thấp nhất
                });
                setLoading(false);
            } catch (err) {
                setError('Không thể tải dữ liệu thống kê');
                setLoading(false);
            }
        };

        fetchStats();
    }, []);

    // Xử lý chuyển đổi kiểu biểu đồ
    const toggleChartType = () => {
        setChartType(chartType === 'bar' ? 'pie' : chartType === 'pie' ? 'line' : 'bar');
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
            <h1>Thống Kê Sản Phẩm</h1>

            {loading ? (
                <p>Đang tải dữ liệu...</p>
            ) : error ? (
                <p>{error}</p>
            ) : (
                <>
                    <div className="stats">
                        <h3>Thống Kê Tổng Số Sản Phẩm</h3>
                        <p>Tổng số sản phẩm: {stats.total}</p>

                        <h3>Thống Kê Theo Loại Sản Phẩm</h3>
                        <ul>
                            {stats.tiviType.map((item, index) => (
                                <li key={index}>{item.tiviType}: {item.count} sản phẩm</li>
                            ))}
                        </ul>

                        <h3>Thống Kê Theo Kích Thước Màn Hình</h3>
                        <ul>
                            {stats.screenSize.map((item, index) => (
                                <li key={index}>{item.screenSize}: {item.count} sản phẩm</li>
                            ))}
                        </ul>

                        <h3>Thống Kê Theo Công Nghệ Hình Ảnh</h3>
                        <ul>
                            {stats.imageTechnology.map((item, index) => (
                                <li key={index}>{item.imageTechnology}: {item.count} sản phẩm</li>
                            ))}
                        </ul>

                        <h3>Thống Kê Theo Phân Khúc Giá</h3>
                        <ul>
                            {stats.priceRange.map((item, index) => (
                                <li key={index}>{item.priceRange}: {item.count} sản phẩm</li>
                            ))}
                        </ul>

                        <h3>Thống Kê Theo Năm Phát Hành</h3>
                        <ul>
                            {stats.releaseYear.map((item, index) => (
                                <li key={index}>{item.releaseYear}: {item.count} sản phẩm</li>
                            ))}
                        </ul>

                        <h3>Thống Kê Theo Thời Gian Bảo Hành</h3>
                        <ul>
                            {stats.warrantyPeriod.map((item, index) => (
                                <li key={index}>{item.warrantyPeriod} năm: {item.count} sản phẩm</li>
                            ))}
                        </ul>

                        <h3>Thống Kê Theo Công Nghệ Âm Thanh</h3>
                        <ul>
                            {stats.soundTechnology.map((item, index) => (
                                <li key={index}>{item.soundTechnology}: {item.count} sản phẩm</li>
                            ))}
                        </ul>

                        <h3>Thống Kê Theo Nhãn Hiệu</h3>
                        <ul>
                            {stats.manufacturer.map((item, index) => (
                                <li key={index}>{item.manufacturer}: {item.count} sản phẩm</li>
                            ))}
                        </ul>

                        <div className="price-stats">
                            <ul>
                                <li><strong>Giá Trung Bình:</strong> {stats.averagePrice} VND</li>
                                <li><strong>Giá Cao Nhất:</strong> {stats.highestPrice} VND</li>
                                <li><strong>Giá Thấp Nhất:</strong> {stats.lowestPrice} VND</li>
                            </ul>
                        </div>

                    </div>

                    {/* Nút hiển thị biểu đồ */}
                    <button className="button" onClick={() => setShowChart(!showChart)}>
                        {showChart ? 'Ẩn Biểu Đồ' : 'Hiển Thị Biểu Đồ'}
                    </button>

                    {/* Nút chuyển đổi kiểu biểu đồ */}
                    {showChart && (
                        <button onClick={toggleChartType}>
                            Chuyển sang biểu
                            đồ {chartType === 'bar' ? 'Hình Tròn' : chartType === 'line' ? 'Cột' : 'Đường'}
                        </button>
                    )}
                    {showChart && (
                        <div className="charts">
                            {/* Biểu đồ phân khúc giá */}
                            <div className="chart-section">
                                <h3>Biểu Đồ Phân Khúc Giá</h3>
                                <ResponsiveContainer width="100%" height={400}>
                                    {chartType === 'bar' ? (
                                        <BarChart data={stats.priceRange}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="priceRange" />
                                            <YAxis />
                                            <Tooltip />
                                            <Legend />
                                            <Bar dataKey="count" fill="#8884d8">
                                                <LabelList dataKey="count" position="top" />
                                            </Bar>
                                        </BarChart>
                                    ) : chartType === 'line' ? (
                                        <LineChart data={stats.priceRange}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="priceRange" />
                                            <YAxis />
                                            <Tooltip />
                                            <Legend />
                                            <Line type="monotone" dataKey="count" stroke="#8884d8" />
                                        </LineChart>
                                    ) : (
                                        <PieChart>
                                            <Pie
                                                data={stats.priceRange}
                                                dataKey="count"
                                                nameKey="priceRange"
                                                cx="50%"
                                                cy="50%"
                                                outerRadius={150}
                                                label
                                                isAnimationActive={false}
                                            >
                                                {stats.priceRange.map((entry, index) => (
                                                    <Cell key={`cell-${index}`} fill={getRandomColor()} />
                                                ))}
                                            </Pie>
                                            <Legend />
                                        </PieChart>
                                    )}
                                </ResponsiveContainer>
                            </div>

                            {/* Biểu đồ giá thấp nhất, trung bình, cao nhất */}
                            <div className="chart-section">
                                <h3>Biểu Đồ Giá Thấp Nhất, Trung Bình, Cao Nhất</h3>
                                <ResponsiveContainer width="100%" height={400}>
                                    {chartType === 'bar' ? (
                                        <BarChart data={[
                                            { name: 'Giá Thấp Nhất', value: stats.lowestPrice },
                                            { name: 'Giá Trung Bình', value: stats.averagePrice },
                                            { name: 'Giá Cao Nhất', value: stats.highestPrice }
                                        ]}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="name" />
                                            <YAxis />
                                            <Tooltip />
                                            <Legend />
                                            <Bar dataKey="value" fill="#8884d8">
                                                <LabelList dataKey="value" position="top" />
                                            </Bar>
                                        </BarChart>
                                    ) : chartType === 'line' ? (
                                        <LineChart data={[
                                            { name: 'Giá Thấp Nhất', value: stats.lowestPrice },
                                            { name: 'Giá Trung Bình', value: stats.averagePrice },
                                            { name: 'Giá Cao Nhất', value: stats.highestPrice }
                                        ]}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="name" />
                                            <YAxis />
                                            <Tooltip />
                                            <Legend />
                                            <Line type="monotone" dataKey="value" stroke="#8884d8" />
                                        </LineChart>
                                    ) : (
                                        <PieChart>
                                            <Pie
                                                data={[
                                                    { name: 'Giá Thấp Nhất', value: stats.lowestPrice },
                                                    { name: 'Giá Trung Bình', value: stats.averagePrice },
                                                    { name: 'Giá Cao Nhất', value: stats.highestPrice }
                                                ]}
                                                dataKey="value"
                                                nameKey="name"
                                                cx="50%"
                                                cy="50%"
                                                outerRadius={150}
                                                label
                                                isAnimationActive={false}
                                            >
                                                <Cell key="low" fill="#ff7300" />
                                                <Cell key="avg" fill="#8884d8" />
                                                <Cell key="high" fill="#82ca9d" />
                                            </Pie>
                                            <Legend />
                                        </PieChart>
                                    )}
                                </ResponsiveContainer>
                            </div>


                            {/* Biểu đồ theo loại sản phẩm */}
                            <div className="chart-section">
                                <h3>Biểu Đồ Theo Loại Sản Phẩm</h3>
                                <ResponsiveContainer width="100%" height={400}>
                                    {chartType === 'bar' ? (
                                        <BarChart data={stats.tiviType}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="tiviType" />
                                            <YAxis />
                                            <Tooltip />
                                            <Legend />
                                            <Bar dataKey="count" fill="#82ca9d">
                                                <LabelList dataKey="count" position="top" />
                                            </Bar>
                                        </BarChart>
                                    ) : chartType === 'line' ? (
                                        <LineChart data={stats.tiviType}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="tiviType" />
                                            <YAxis />
                                            <Tooltip />
                                            <Legend />
                                            <Line type="monotone" dataKey="count" stroke="#82ca9d" />
                                        </LineChart>
                                    ) : (
                                        <PieChart>
                                            <Pie
                                                data={stats.tiviType}
                                                dataKey="count"
                                                nameKey="tiviType"
                                                cx="50%"
                                                cy="50%"
                                                outerRadius={150}
                                                label
                                                isAnimationActive={false}
                                            >
                                                {stats.tiviType.map((entry, index) => (
                                                    <Cell key={`cell-${index}`} fill={getRandomColor()} />
                                                ))}
                                            </Pie>
                                            <Legend />
                                        </PieChart>
                                    )}
                                </ResponsiveContainer>
                            </div>

                            {/* Biểu đồ theo kích thước màn hình */}
                            <div className="chart-section">
                                <h3>Biểu Đồ Theo Kích Thước Màn Hình</h3>
                                <ResponsiveContainer width="100%" height={400}>
                                    {chartType === 'bar' ? (
                                        <BarChart data={stats.screenSize}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="screenSize" />
                                            <YAxis />
                                            <Tooltip />
                                            <Legend />
                                            <Bar dataKey="count" fill="#ff7300">
                                                <LabelList dataKey="count" position="top" />
                                            </Bar>
                                        </BarChart>
                                    ) : chartType === 'line' ? (
                                        <LineChart data={stats.screenSize}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="screenSize" />
                                            <YAxis />
                                            <Tooltip />
                                            <Legend />
                                            <Line type="monotone" dataKey="count" stroke="#ff7300" />
                                        </LineChart>
                                    ) : (
                                        <PieChart>
                                            <Pie
                                                data={stats.screenSize}
                                                dataKey="count"
                                                nameKey="screenSize"
                                                cx="50%"
                                                cy="50%"
                                                outerRadius={150}
                                                label
                                                isAnimationActive={false}
                                            >
                                                {stats.screenSize.map((entry, index) => (
                                                    <Cell key={`cell-${index}`} fill={getRandomColor()} />
                                                ))}
                                            </Pie>
                                            <Legend />
                                        </PieChart>
                                    )}
                                </ResponsiveContainer>
                            </div>

                            {/* Biểu đồ theo nhãn hiệu */}
                            <div className="chart-section">
                                <h3>Biểu Đồ Theo Nhãn Hiệu</h3>
                                <ResponsiveContainer width="100%" height={400}>
                                    {chartType === 'bar' ? (
                                        <BarChart data={stats.manufacturer}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="manufacturer" />
                                            <YAxis />
                                            <Tooltip />
                                            <Legend />
                                            <Bar dataKey="count" fill="#ff7300">
                                                <LabelList dataKey="count" position="top" />
                                            </Bar>
                                        </BarChart>
                                    ) : chartType === 'line' ? (
                                        <LineChart data={stats.manufacturer}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="manufacturer" />
                                            <YAxis />
                                            <Tooltip />
                                            <Legend />
                                            <Line type="monotone" dataKey="count" stroke="#ff7300" />
                                        </LineChart>
                                    ) : (
                                        <PieChart>
                                            <Pie
                                                data={stats.manufacturer}
                                                dataKey="count"
                                                nameKey="manufacturer"
                                                cx="50%"
                                                cy="50%"
                                                outerRadius={150}
                                                label
                                                isAnimationActive={false}
                                            >
                                                {stats.manufacturer.map((entry, index) => (
                                                    <Cell key={`cell-${index}`} fill={getRandomColor()} />
                                                ))}
                                            </Pie>
                                            <Legend />
                                        </PieChart>
                                    )}
                                </ResponsiveContainer>
                            </div>
                        </div>
                    )}

                </>
            )}
        </div>
    );
};

export default ProductStatsPage;
