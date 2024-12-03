require('dotenv').config();

const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');

const app = express();

// Lấy cổng và các thông tin kết nối từ file .env
const port = process.env.PORT || 5000;

// Sử dụng CORS để frontend có thể truy cập backend
app.use(cors());

// Kết nối MySQL
const db = mysql.createConnection({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME
});

db.connect(err => {
    if (err) throw err;
    console.log('Connected to MySQL');
});

// API để lấy dữ liệu tivi
app.get('/api/tivi', (req, res) => {
    const query = 'SELECT * FROM data_tivi'; // Thay đổi tên bảng nếu cần
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching data');
        } else {
            res.json(result);
        }
    });
});
// API thống kê theo nhãn hiệu
app.get('/api/stats/manufacturer', (req, res) => {
    const query = 'SELECT manufacturer, COUNT(*) AS count FROM data_tivi GROUP BY manufacturer ORDER BY count DESC'; // Thống kê theo nhãn hiệu, sắp xếp theo số lượng giảm dần
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching manufacturer stats');
        } else {
            res.json(result);
        }
    });
});



// API thống kê tổng số tivi
app.get('/api/stats/total', (req, res) => {
    const query = 'SELECT COUNT(*) AS total FROM data_tivi'; // Tổng số TV
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching total count');
        } else {
            res.json(result[0]);
        }
    });
});

// API thống kê theo loại tivi
app.get('/api/stats/tivi-type', (req, res) => {
    const query = 'SELECT tiviType, COUNT(*) AS count FROM data_tivi GROUP BY tiviType ORDER BY count DESC'; // Thống kê theo loại tivi, sắp xếp theo số lượng giảm dần
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching tivi type stats');
        } else {
            res.json(result);
        }
    });
});


// API thống kê theo kích thước màn hình
app.get('/api/stats/screen-size', (req, res) => {
    const query = 'SELECT screenSize, COUNT(*) AS count FROM data_tivi GROUP BY screenSize ORDER BY count DESC'; // Thống kê theo kích thước màn hình, sắp xếp theo số lượng giảm dần
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching screen size stats');
        } else {
            res.json(result);
        }
    });
});



// API thống kê theo công nghệ hình ảnh
app.get('/api/stats/image-technology', (req, res) => {
    const query = 'SELECT imageTechnology, COUNT(*) AS count FROM data_tivi GROUP BY imageTechnology ORDER BY count DESC'; // Thống kê theo công nghệ hình ảnh, sắp xếp theo số lượng giảm dần
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching image technology stats');
        } else {
            res.json(result);
        }
    });
});


// API thống kê mức giá
app.get('/api/stats/price-range', (req, res) => {
    const query = `
        SELECT 
            CASE
                WHEN price < 5000000 THEN 'Dưới 5 triệu'
                WHEN price BETWEEN 5000000 AND 10000000 THEN 'Từ 5-10 triệu'
                ELSE 'Trên 10 triệu'
            END AS priceRange, 
            COUNT(*) AS count 
        FROM data_tivi 
        GROUP BY priceRange
        ORDER BY count DESC`; // Sắp xếp theo số lượng phân khúc giá giảm dần
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching price range stats');
        } else {
            res.json(result);
        }
    });
});


// API thống kê theo năm phát hành
app.get('/api/stats/release-year', (req, res) => {
    const query = 'SELECT releaseYear, COUNT(*) AS count FROM data_tivi GROUP BY releaseYear ORDER BY releaseYear DESC'; // Sắp xếp theo năm phát hành mới nhất
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching release year stats');
        } else {
            res.json(result);
        }
    });
});


// API thống kê theo bảo hành
app.get('/api/stats/warranty-period', (req, res) => {
    const query = 'SELECT warrantyPeriod, COUNT(*) AS count FROM data_tivi GROUP BY warrantyPeriod ORDER BY warrantyPeriod DESC'; // Sắp xếp theo thời gian bảo hành giảm dần
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching warranty period stats');
        } else {
            res.json(result);
        }
    });
});

// API thống kê theo công nghệ âm thanh
app.get('/api/stats/sound-technology', (req, res) => {
    const query = 'SELECT soundTechnology, COUNT(*) AS count FROM data_tivi GROUP BY soundTechnology ORDER BY count DESC'; // Sắp xếp theo số lượng công nghệ âm thanh giảm dần
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching sound technology stats');
        } else {
            res.json(result);
        }
    });
});


// API thống kê mức giá trung bình
app.get('/api/stats/average-price', (req, res) => {
    const query = 'SELECT AVG(price) AS average_price FROM data_tivi'; // Mức giá trung bình không cần sắp xếp
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching average price');
        } else {
            res.json(result[0]);
        }
    });
});

// API thống kê giá cao nhất
app.get('/api/stats/highest-price', (req, res) => {
    const query = 'SELECT MAX(price) AS highest_price FROM data_tivi'; // Giá cao nhất không cần sắp xếp
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching highest price');
        } else {
            res.json(result[0]);
        }
    });
});


// API thống kê giá thấp nhất
app.get('/api/stats/lowest-price', (req, res) => {
    const query = 'SELECT MIN(price) AS lowest_price FROM data_tivi'; // Giá thấp nhất không cần sắp xếp
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching lowest price');
        } else {
            res.json(result[0]);
        }
    });
});


// Lắng nghe trên cổng đã cấu hình
app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});
