import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './ProductStatsPage.css';
import { Bar, Line, Pie } from 'react-chartjs-2';  // Thêm import cho biểu đồ đường và tròn
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement, LineElement, PointElement } from 'chart.js';

// Đăng ký các thành phần của Chart.js
ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
    ArcElement,  // Đăng ký thành phần cho biểu đồ tròn
    LineElement,  // Đăng ký thành phần cho biểu đồ đường
    PointElement  // Đăng ký thành phần cho các điểm trong biểu đồ đường
);

const ProductStatsPage = () => {
    const [audioTechData, setAudioTechData] = useState([]);
    const [avgPriceData, setAvgPriceData] = useState([]);
    const [manufacturerCountData, setManufacturerCountData] = useState([]);
    const [imageTechData, setImageTechData] = useState([]);
    const [priceSummaryData, setPriceSummaryData] = useState([]);
    const [error, setError] = useState('');
    const [showChart, setShowChart] = useState(false); // Trạng thái cho việc hiển thị biểu đồ
    const [chartType, setChartType] = useState('bar');  // Trạng thái để lưu loại biểu đồ (bar, line, pie)
    const [totalCount, setTotalCount] = useState([]);  // Khai báo state cho dữ liệu mới

    const fetchData = async () => {
        try {
            const audioTechResponse = await axios.get('http://localhost:5000/api/audio-tech');
            setAudioTechData(audioTechResponse.data);

            const avgPriceResponse = await axios.get('http://localhost:5000/api/avg-price');
            setAvgPriceData(avgPriceResponse.data);

            const manufacturerCountResponse = await axios.get('http://localhost:5000/api/manufacturer-count');
            setManufacturerCountData(manufacturerCountResponse.data);

            const imageTechResponse = await axios.get('http://localhost:5000/api/image-tech');
            setImageTechData(imageTechResponse.data);

            const priceSummaryResponse = await axios.get('http://localhost:5000/api/price-summary');
            setPriceSummaryData(priceSummaryResponse.data);
            const totalCountResponse = await axios.get('http://localhost:5000/api/total-count');  // API mới
            setTotalCount(totalCountResponse.data);
        } catch (err) {
            console.error("API Error:", err);
            setError('Đã xảy ra lỗi khi lấy dữ liệu.');
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    // Hàm sinh màu ngẫu nhiên
    const getRandomColor = () => {
        const r = Math.floor(Math.random() * 256);
        const g = Math.floor(Math.random() * 256);
        const b = Math.floor(Math.random() * 256);
        return `rgba(${r}, ${g}, ${b}, 0.6)`;
    };

    // Hàm chuyển đổi dữ liệu cho biểu đồ cột (Số lượng sản phẩm theo công nghệ âm thanh)
    const getAudioTechChartData = () => {
        return {
            labels: audioTechData.map((item) => item.tech_audio_name === 'NULL' ? 'Không Có Tên' : item.tech_audio_name),
            datasets: [
                {
                    label: 'Số Lượng Sản Phẩm',
                    data: audioTechData.map((item) => item.product_count),
                    backgroundColor: getRandomColor(),  // Sử dụng màu ngẫu nhiên
                    borderColor: getRandomColor(),
                    borderWidth: 1
                }
            ]
        };
    };

    // Hàm chuyển đổi dữ liệu cho biểu đồ đường (Số lượng sản phẩm theo công nghệ âm thanh)
    const getAudioTechLineData = () => {
        return {
            labels: audioTechData.map((item) => item.tech_audio_name === 'NULL' ? 'Không Có Tên' : item.tech_audio_name),
            datasets: [
                {
                    label: 'Số Lượng Sản Phẩm',
                    data: audioTechData.map((item) => item.product_count),
                    fill: false,
                    borderColor: getRandomColor(),  // Sử dụng màu ngẫu nhiên
                    tension: 0.1
                }
            ]
        };
    };

    // Hàm chuyển đổi dữ liệu cho biểu đồ tròn (Số lượng sản phẩm theo công nghệ âm thanh)
    const getAudioTechPieData = () => {
        return {
            labels: audioTechData.map((item) => item.tech_audio_name === 'NULL' ? 'Không Có Tên' : item.tech_audio_name),
            datasets: [
                {
                    data: audioTechData.map((item) => item.product_count),
                    backgroundColor: audioTechData.map(() => getRandomColor()),  // Sử dụng màu ngẫu nhiên cho từng phần
                }
            ]
        };
    };

    // Hàm chuyển đổi dữ liệu cho biểu đồ cột (Số lượng sản phẩm theo nhà sản xuất)
    const getManufacturerCountChartData = () => {
        return {
            labels: manufacturerCountData.map((item) => item.manufacturer_name),
            datasets: [
                {
                    label: 'Số Lượng Sản Phẩm',
                    data: manufacturerCountData.map((item) => item.product_count),
                    backgroundColor: manufacturerCountData.map(() => getRandomColor()),  // Áp dụng màu ngẫu nhiên cho từng phần
                    borderColor: manufacturerCountData.map(() => getRandomColor()),  // Áp dụng màu ngẫu nhiên cho từng phần
                    borderWidth: 1
                }
            ]
        };
    };

    // Hàm chuyển đổi dữ liệu cho biểu đồ cột (Giá cao nhất, thấp nhất, trung bình)
    const getPriceSummaryChartData = () => {
        return {
            labels: priceSummaryData.map((item) => `Tóm Tắt ${item.price_summary_id}`),
            datasets: [
                {
                    label: 'Giá Cao Nhất',
                    data: priceSummaryData.map((item) => item.highest_price),
                    backgroundColor: getRandomColor(),  // Sử dụng màu ngẫu nhiên
                    borderColor: getRandomColor(),
                    borderWidth: 1
                },
                {
                    label: 'Giá Thấp Nhất',
                    data: priceSummaryData.map((item) => item.lowest_price),
                    backgroundColor: getRandomColor(),  // Sử dụng màu ngẫu nhiên
                    borderColor: getRandomColor(),
                    borderWidth: 1
                },
                {
                    label: 'Giá Trung Bình',
                    data: avgPriceData.map((item) => item.average_price),
                    backgroundColor: getRandomColor(),  // Sử dụng màu ngẫu nhiên
                    borderColor: getRandomColor(),
                    borderWidth: 1
                }
            ]
        };
    };

    // Hàm chuyển đổi dữ liệu cho biểu đồ cột (Số lượng sản phẩm theo công nghệ hình ảnh)
    const getImageTechChartData = () => {
        return {
            labels: imageTechData.map((item) => item.tech_image_name === 'NULL' ? 'Không Có Tên' : item.tech_image_name),
            datasets: [
                {
                    label: 'Số Lượng Sản Phẩm',
                    data: imageTechData.map((item) => item.product_count),
                    backgroundColor: getRandomColor(),  // Sử dụng màu ngẫu nhiên
                    borderColor: getRandomColor(),
                    borderWidth: 1
                }
            ]
        };
    };

    // Hàm chuyển đổi dữ liệu cho biểu đồ đường (Số lượng sản phẩm theo công nghệ hình ảnh)
    const getImageTechLineData = () => {
        return {
            labels: imageTechData.map((item) => item.tech_image_name === 'NULL' ? 'Không Có Tên' : item.tech_image_name),
            datasets: [
                {
                    label: 'Số Lượng Sản Phẩm',
                    data: imageTechData.map((item) => item.product_count),
                    fill: false,
                    borderColor: getRandomColor(),  // Sử dụng màu ngẫu nhiên
                    tension: 0.1
                }
            ]
        };
    };

    // Hàm chuyển đổi dữ liệu cho biểu đồ tròn (Số lượng sản phẩm theo công nghệ hình ảnh)
    const getImageTechPieData = () => {
        return {
            labels: imageTechData.map((item) => item.tech_image_name === 'NULL' ? 'Không Có Tên' : item.tech_image_name),
            datasets: [
                {
                    data: imageTechData.map((item) => item.product_count),
                    backgroundColor: imageTechData.map(() => getRandomColor()),  // Sử dụng màu ngẫu nhiên cho từng phần
                }
            ]
        };
    };

    return (
        <div>
            <h1>Thông Tin Sản Phẩm</h1>
            {error && <div style={{ color: 'red' }}>{error}</div>}

            {/* Nút để hiển thị biểu đồ */}
            <button onClick={() => setShowChart(!showChart)}>
                {showChart ? 'Ẩn Biểu Đồ' : 'Hiển Thị Biểu Đồ'}
            </button>

            {/* Lựa chọn loại biểu đồ */}
            {showChart && (
                <div style={{ marginTop: '20px' }}>
                    <label>Chọn Loại Biểu Đồ: </label>
                    <select onChange={(e) => setChartType(e.target.value)} value={chartType}>
                        <option value="bar">Biểu Đồ Cột</option>
                        <option value="line">Biểu Đồ Đường</option>
                        <option value="pie">Biểu Đồ Tròn</option>
                    </select>
                </div>
            )}

            {/* Biểu đồ số lượng sản phẩm theo công nghệ âm thanh */}
            {showChart && audioTechData.length > 0 && (
                <section>
                    <h2>Số Lượng Sản Phẩm Theo Công Nghệ Âm Thanh</h2>
                    {chartType === 'bar' && (
                        <Bar
                            data={getAudioTechChartData()}
                            options={{
                                responsive: true,
                                plugins: {
                                    title: {
                                        display: true,
                                        text: 'Sản Phẩm Theo Công Nghệ Âm Thanh'
                                    }
                                }
                            }}
                            width={400}  // Kích thước width nhỏ hơn
                            height={200} // Kích thước height nhỏ hơn
                        />
                    )}
                    {chartType === 'line' && (
                        <Line
                            data={getAudioTechLineData()}
                            options={{
                                responsive: true,
                                plugins: {
                                    title: {
                                        display: true,
                                        text: 'Sản Phẩm Theo Công Nghệ Âm Thanh'
                                    }
                                }
                            }}
                            width={400}  // Kích thước width nhỏ hơn
                            height={200} // Kích thước height nhỏ hơn
                        />
                    )}
                    {chartType === 'pie' && (
                        <Pie
                            data={getAudioTechPieData()}
                            options={{
                                responsive: true,
                                plugins: {
                                    title: {
                                        display: true,
                                        text: 'Sản Phẩm Theo Công Nghệ Âm Thanh'
                                    }
                                }
                            }}
                            width={400}  // Kích thước width nhỏ hơn
                            height={200} // Kích thước height nhỏ hơn
                        />
                    )}
                </section>
            )}

            {/* Biểu đồ số lượng sản phẩm theo hãng */}
            {showChart && manufacturerCountData.length > 0 && (
                <section>
                    <h2>Số Lượng Sản Phẩm Theo Nhà Sản Xuất</h2>
                    {chartType === 'bar' && (
                        <Bar
                            data={getManufacturerCountChartData()}
                            options={{
                                responsive: true,
                                plugins: {
                                    title: {
                                        display: true,
                                        text: 'Sản Phẩm Theo Nhà Sản Xuất'
                                    }
                                }
                            }}
                            width={400}  // Kích thước width nhỏ hơn
                            height={200} // Kích thước height nhỏ hơn
                        />
                    )}
                    {chartType === 'line' && (
                        <Line
                            data={getManufacturerCountChartData()}
                            options={{
                                responsive: true,
                                plugins: {
                                    title: {
                                        display: true,
                                        text: 'Sản Phẩm Theo Nhà Sản Xuất'
                                    }
                                }
                            }}
                            width={400}  // Kích thước width nhỏ hơn
                            height={200} // Kích thước height nhỏ hơn
                        />
                    )}
                    {chartType === 'pie' && (
                        <Pie
                            data={getManufacturerCountChartData()}
                            options={{
                                responsive: true,
                                plugins: {
                                    title: {
                                        display: true,
                                        text: 'Sản Phẩm Theo Nhà Sản Xuất'
                                    }
                                }
                            }}
                            width={400}  // Kích thước width nhỏ hơn
                            height={200} // Kích thước height nhỏ hơn
                        />
                    )}
                </section>
            )}

            {/* Biểu đồ tóm tắt giá */}
            {showChart && priceSummaryData.length > 0 && (
                <section>
                    <h2>Tóm Tắt Giá (Cao Nhất, Thấp Nhất, Trung Bình)</h2>
                    {chartType === 'bar' && (
                        <Bar
                            data={getPriceSummaryChartData()}
                            options={{
                                responsive: true,
                                plugins: {
                                    title: {
                                        display: true,
                                        text: 'Tóm Tắt Giá (Cao Nhất, Thấp Nhất, Trung Bình)'
                                    }
                                }
                            }}
                            width={400}  // Kích thước width nhỏ hơn
                            height={200} // Kích thước height nhỏ hơn
                        />
                    )}
                    {chartType === 'line' && (
                        <Line
                            data={getPriceSummaryChartData()}
                            options={{
                                responsive: true,
                                plugins: {
                                    title: {
                                        display: true,
                                        text: 'Tóm Tắt Giá (Cao Nhất, Thấp Nhất, Trung Bình)'
                                    }
                                }
                            }}
                            width={400}  // Kích thước width nhỏ hơn
                            height={200} // Kích thước height nhỏ hơn
                        />
                    )}
                    {chartType === 'pie' && (
                        <Pie
                            data={getPriceSummaryChartData()}
                            options={{
                                responsive: true,
                                plugins: {
                                    title: {
                                        display: true,
                                        text: 'Tóm Tắt Giá (Cao Nhất, Thấp Nhất, Trung Bình)'
                                    }
                                }
                            }}
                            width={400}  // Kích thước width nhỏ hơn
                            height={200} // Kích thước height nhỏ hơn
                        />
                    )}
                </section>
            )}


            {/* Các phần tóm tắt dữ liệu khác */}
            {/* Bảng Tổng Số Lượng Sản Phẩm */}
            <section>
                <h2>Tổng Số Lượng Sản Phẩm</h2>
                {totalCount.length > 0 ? (
                    <table>
                        <thead>
                        <tr>
                            <th>Tổng số Lượng Sản Phẩm</th>
                        </tr>
                        </thead>
                        <tbody>
                        {totalCount.map((item) => (
                            <tr key={item.total_count_id}>
                                <td>{item.total_product_count}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p>Không có dữ liệu giá trung bình.</p>
                )}
            </section>
            {/* Bảng Giá Trung Bình */}
            <section>
                <h2>Giá Trung Bình</h2>
                {avgPriceData.length > 0 ? (
                    <table>
                        <thead>
                        <tr>
                            <th>Giá Trung Bình</th>
                        </tr>
                        </thead>
                        <tbody>
                        {avgPriceData.map((item) => (
                            <tr key={item.id}>
                                <td>{item.average_price}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p>Không có dữ liệu giá trung bình.</p>
                )}
            </section>

            {/* Dữ liệu Công Nghệ Âm Thanh */}
            <section>
                <h2>Tóm Tắt Công Nghệ Âm Thanh</h2>
                {audioTechData.length > 0 ? (
                    <table>
                        <thead>
                        <tr>
                            <th>ID Công Nghệ Âm Thanh</th>
                            <th>Tên Công Nghệ Âm Thanh</th>
                            <th>Số Lượng Sản Phẩm</th>
                        </tr>
                        </thead>
                        <tbody>
                        {audioTechData.map((item) => (
                            <tr key={item.tech_audio_id}>
                                <td>{item.tech_audio_id}</td>
                                <td>{item.tech_audio_name === 'NULL' ? 'Không Có Tên' : item.tech_audio_name}</td>
                                <td>{item.product_count}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p>Không có dữ liệu công nghệ âm thanh.</p>
                )}
            </section>

            {/* Dữ liệu Giá Trung Bình */}
            <section>
                <h2>Tóm Tắt Giá Trung Bình</h2>
                {avgPriceData.length > 0 ? (
                    <table>
                        <thead>
                        <tr>
                            <th>Giá Trung Bình</th>
                        </tr>
                        </thead>
                        <tbody>
                        {avgPriceData.map((item) => (
                            <tr>
                                <td>{item.average_price}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p>Không có dữ liệu giá trung bình.</p>
                )}
            </section>

            {/* Dữ liệu Số Lượng Theo Nhà Sản Xuất */}
            <section>
                <h2>Số Lượng Theo Nhà Sản Xuất</h2>
                {manufacturerCountData.length > 0 ? (
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên Nhà Sản Xuất</th>
                            <th>Số Lượng Sản Phẩm</th>
                        </tr>
                        </thead>
                        <tbody>
                        {manufacturerCountData.map((item) => (
                            <tr key={item.id}>
                                <td>{item.manufacturer_id}</td>
                                <td>{item.manufacturer_name}</td>
                                <td>{item.product_count}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p>Không có dữ liệu số lượng theo nhà sản xuất.</p>
                )}
            </section>

            {/* Dữ liệu Công Nghệ Hình Ảnh */}
            <section>
                <h2>Tóm Tắt Công Nghệ Hình Ảnh</h2>
                {imageTechData.length > 0 ? (
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên Công Nghệ</th>
                            <th>Số lượng sản phẩm</th>
                        </tr>
                        </thead>
                        <tbody>
                        {imageTechData.map((item) => (
                            <tr key={item.tech_image_id}>
                                <td>{item.tech_image_id}</td>
                                <td>{item.tech_image_name}</td>
                                <td>{item.product_count}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p>Không có dữ liệu công nghệ hình ảnh.</p>
                )}
            </section>

            {/* Dữ liệu Tóm Tắt Giá */}
            <section>
                <h2>Tóm Tắt Giá</h2>
                {priceSummaryData.length > 0 ? (
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Giá Cao Nhất</th>
                            <th>Tên Sản Phẩm Giá Cao Nhất</th>
                            <th>Giá Thấp Nhất</th>
                            <th>Tên Sản Phẩm Giá Thấp Nhất</th>
                        </tr>
                        </thead>
                        <tbody>
                        {priceSummaryData.map((item) => (
                            <tr key={item.price_summary_id}>
                                <td>{item.price_summary_id}</td>
                                <td>{item.highest_price}</td>
                                <td>{item.highest_price_product_name}</td>
                                <td>{item.lowest_price}</td>
                                <td>{item.lowest_price_product_name}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p>Không có dữ liệu tóm tắt giá.</p>
                )}
            </section>
        </div>
    );
};

export default ProductStatsPage;
