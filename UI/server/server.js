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
    const query = 'SELECT * FROM raw_tivi_data'; // Thay đổi tên bảng nếu cần
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching data');
        } else {
            res.json(result);
        }
    });
});

// API để lấy dữ liệu từ bảng product_audio_technology_summary
app.get('/api/audio-tech', (req, res) => {
    const query = 'SELECT * FROM product_audio_technology_summary';
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching audio technology data');
        } else {
            res.json(result);
        }
    });
});

// API để lấy dữ liệu từ bảng product_avg_price
app.get('/api/avg-price', (req, res) => {
    const query = 'SELECT * FROM product_avg_price';
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching average price data');
        } else {
            res.json(result);
        }
    });
});

// API để lấy dữ liệu từ bảng product_count_by_manufacturer
app.get('/api/manufacturer-count', (req, res) => {
    const query = 'SELECT * FROM product_count_by_manufacturer';
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching manufacturer count data');
        } else {
            res.json(result);
        }
    });
});

// API để lấy dữ liệu từ bảng product_image_technology_summary
app.get('/api/image-tech', (req, res) => {
    const query = 'SELECT * FROM product_image_technology_summary';
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching image technology data');
        } else {
            res.json(result);
        }
    });
});

// API để lấy dữ liệu từ bảng product_price_summary
app.get('/api/price-summary', (req, res) => {
    const query = 'SELECT * FROM product_price_summary';
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching price summary data');
        } else {
            res.json(result);
        }
    });
});

// API để lấy dữ liệu từ bảng product_total_count
app.get('/api/total-count', (req, res) => {
    const query = 'SELECT * FROM product_total_count'; // Truy vấn lấy toàn bộ dữ liệu từ bảng product_total_count
    db.query(query, (err, result) => {
        if (err) {
            res.status(500).send('Error fetching total product count data');
        } else {
            res.json(result);
        }
    });
});


// Lắng nghe trên cổng đã cấu hình
app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});
