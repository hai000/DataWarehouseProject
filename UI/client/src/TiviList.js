import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ReactPaginate from 'react-paginate';
import './TiviList.css'; // Đảm bảo bạn có file CSS cho bảng

const TiviList = () => {
    const [tivis, setTivis] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage] = useState(30); // Giới hạn 50 dòng mỗi trang
    const [searchTerm, setSearchTerm] = useState(''); // State cho tìm kiếm
    const [filteredTivis, setFilteredTivis] = useState([]); // State cho danh sách tivi đã lọc

    // Lấy dữ liệu từ API khi component được mount
    useEffect(() => {
        axios.get('http://localhost:5000/api/tivi') // Địa chỉ API backend
            .then(response => {
                setTivis(response.data); // Lưu dữ liệu vào state
                setFilteredTivis(response.data); // Cập nhật danh sách tivi ban đầu
                setLoading(false); // Đặt loading là false khi dữ liệu đã tải xong
            })
            .catch(err => {
                setError('Không thể tải dữ liệu');
                setLoading(false);
            });
    }, []);

    // Lọc dữ liệu khi người dùng nhập vào ô tìm kiếm
    const handleSearch = (e) => {
        const searchValue = e.target.value;
        setSearchTerm(searchValue);

        // Lọc tivi theo tên sản phẩm
        if (searchValue) {
            const filteredData = tivis.filter(tivi =>
                tivi.name.toLowerCase().includes(searchValue.toLowerCase())

            );
            setFilteredTivis(filteredData);
        } else {
            setFilteredTivis(tivis); // Hiển thị lại tất cả nếu không có từ khóa tìm kiếm
        }

        // Đặt lại trang về trang đầu tiên khi tìm kiếm
        setCurrentPage(1);
    };

    if (loading) {
        return <div className="loading">Đang tải dữ liệu...</div>;
    }

    if (error) {
        return <div className="error">{error}</div>;
    }

    // Tính toán chỉ số bắt đầu và kết thúc của các dòng trên trang hiện tại
    const indexOfLastTivi = currentPage * itemsPerPage;
    const indexOfFirstTivi = indexOfLastTivi - itemsPerPage;
    const currentTivis = filteredTivis.slice(indexOfFirstTivi, indexOfLastTivi);

    // Tính tổng số trang
    const totalPages = Math.ceil(filteredTivis.length / itemsPerPage);

    // Xử lý sự kiện khi chuyển trang
    const handlePageClick = (event) => {
        setCurrentPage(event.selected + 1);
    };


    return (
        <div className="table-container">
            {/* Tiêu đề "Danh sách Tivi" */}
            <h1>Danh sách Tivi</h1>

            {/* Thanh tìm kiếm */}
            <div className="search-container">
                <input
                    type="text"
                    className="search-input"
                    placeholder="Tìm kiếm sản phẩm..."
                    value={searchTerm}
                    onChange={handleSearch}  // Chỉnh sửa gọi hàm handleSearch
                />
            </div>

            <table className="tivi-table">
                <thead>
                <tr>
                    <th>Product ID</th>
                    <th>Name</th>
                    <th>Image</th>
                    <th>Price</th>
                    <th>Old Price</th>
                    <th>Discount Percent</th>
                    <th>Screen Size</th>
                    <th>Resolution</th>
                    <th>Operating System</th>
                    <th>Image Technology</th>
                    <th>Processor</th>
                    <th>Refresh Rate</th>
                    <th>Speaker Power</th>
                    <th>Internet Connection</th>
                    <th>Wireless Connectivity</th>
                    <th>USB Ports</th>
                    <th>Video/Audio Ports</th>
                    <th>Manufacturer</th>
                    <th>Manufactured In</th>
                    <th>Release Year</th>
                    <th>Screen Type</th>
                    <th>Audio Output Ports</th>
                    <th>Warranty Period</th>
                    <th>Item Gift</th>
                    <th>Tivi Type</th>
                    <th>HDR</th>
                    <th>Sound Technology</th>
                    <th>Memory</th>
                    <th>Voice Search</th>
                    <th>Crawl Date</th>
                    <th>Source ID</th>

                </tr>
                </thead>
                <tbody>
                {currentTivis.map((tivi) => (
                    <tr key={tivi.product_id}>
                        <td>{tivi.product_id}</td>
                        <td>{tivi.name}</td>
                        <td><img src={tivi.imgLink} alt={tivi.name}
                                 style={{width: '100px', height: 'auto'}}/></td>
                        <td>{tivi.price}</td>
                        <td>{tivi.oldPrice}</td>
                        <td>{tivi.discountPercent}</td>
                        <td>{tivi.screenSize}</td>
                        <td>{tivi.resolution}</td>
                        <td>{tivi.operatingSystem}</td>
                        <td>{tivi.imageTechnology}</td>
                        <td>{tivi.processor}</td>
                        <td>{tivi.refreshRate}</td>
                        <td>{tivi.speakerPower}</td>
                        <td>{tivi.internetConnection}</td>
                        <td>{tivi.wirelessConnectivity}</td>
                        <td>{tivi.usbPorts}</td>
                        <td>{tivi.videoAudioInputPorts}</td>
                        <td>{tivi.manufacturer}</td>
                        <td>{tivi.manufacturedIn}</td>
                        <td>{tivi.releaseYear}</td>
                        <td>{tivi.screenType}</td>
                        <td>{tivi.audioOutputPorts}</td>
                        <td>{tivi.warrantyPeriod}</td>
                        <td>{tivi.itemGift}</td>
                        <td>{tivi.tiviType}</td>
                        <td>{tivi.hdr}</td>
                        <td>{tivi.soundTechnology}</td>
                        <td>{tivi.memory}</td>
                        <td>{tivi.voiceSearch}</td>
                        <td>{tivi.crawlDate}</td>
                        <td>{tivi.sourceId}</td>

                    </tr>
                ))}
                </tbody>
            </table>


            {/* Phân trang */}
            <ReactPaginate
                pageCount={totalPages}
                pageRangeDisplayed={5}
                marginPagesDisplayed={2}
                onPageChange={handlePageClick}
                containerClassName="pagination"
                activeClassName="active"
            />
        </div>
    );
};

export default TiviList;
