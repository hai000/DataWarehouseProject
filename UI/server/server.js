const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');

const app = express();
const port = 5000; // Cổng backend

// Sử dụng CORS để frontend có thể truy cập backend
app.use(cors());

// Kết nối MySQL
const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',  // Thêm mật khẩu nếu cần
    database: 'data_mart', // Tên cơ sở dữ liệu của bạn
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

// Lắng nghe trên cổng 5000
app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});
