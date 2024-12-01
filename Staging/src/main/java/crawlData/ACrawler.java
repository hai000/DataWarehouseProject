package crawlData;

import db.Connections;
import db.DataSource;
import jdbiInterface.DataSourceInterface;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class ACrawler<T> {
    protected  String mainUrl;
    protected  String fileLocation;
    public abstract String crawlData();
    public abstract String sendRequest(String urlString) throws Exception;
    public abstract T getProductDetail(T product) throws Exception;
    public abstract void addProducts(List<T> products, Document doc) throws Exception;
    public String exportProductsToCsv(List<T> products, DataSource dataSource) {
        String filename = String.format(dataSource.getFile_name_format(), dataSource.getFile_location(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        File file = new File(filename);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Ghi tiêu đề từ tên biến
            Field[] fields = getAllFields(products.get(0).getClass());
            StringBuilder header = new StringBuilder();
            for (int i = 0; i < fields.length; i++) {
                header.append(fields[i].getName());
                header.append(",");
            }
            header.append("crawlDate");
            writer.println(header.toString());

            // Ghi từng sản phẩm
            for (T product : products) {
                StringBuilder line = new StringBuilder();
                for (Field field : fields) {
                    field.setAccessible(true); // Đảm bảo có quyền truy cập vào biến riêng
                    String value = String.valueOf(field.get(product)); // Lấy giá trị của biến
                    line.append(escapeCsvValue(value)); // Escape giá trị
                    line.append(","); // Thêm dấu phẩy
                }
                line.append(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                writer.println(line.toString());
            }
            return file.getAbsolutePath().replaceAll("\\\\","/");
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Field[] getAllFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields(); // Lấy thuộc tính riêng của lớp
        Class<?> superClass = clazz.getSuperclass(); // Lấy lớp cha

        // Nếu có lớp cha, lấy các thuộc tính của lớp cha
        if (superClass != null) {
            Field[] superFields = getAllFields(superClass);

            Field[] allFields = new Field[fields.length + superFields.length];
            System.arraycopy(superFields, 0, allFields, 0, superFields.length);
            System.arraycopy(fields, 0, allFields, superFields.length, fields.length);

            return allFields;
        }

        return fields;
    }
    public String escapeCsvValue(String value) {
        if (value == null) {
            return ""; // Trả về chuỗi rỗng nếu giá trị là null
        }
        value = value.replace("\"", "\"\""); // Thay thế dấu nháy kép bằng hai dấu nháy kép
        return "\"" + value + "\""; // Bao quanh giá trị bằng dấu nháy kép
    }
    public DataSource loadConfig(String source) {
        DataSource dataSource = Connections.getControlJDBI().onDemand(DataSourceInterface.class).getDataSource(source);
        this.mainUrl = dataSource.getAddress();
        this.fileLocation = dataSource.getFile_location();
        return dataSource;
    }
}
