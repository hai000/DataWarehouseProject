package crawlData;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NKCrawler extends ACrawler<NKProduct>{
    List<String> ids = new ArrayList<>();


    public void main(String[] args) {
        crawlData();
    }

    public String crawlData() {
        loadConfig("nk_tivi");
        String category = "tivi";
        try {
            int page = 1;
            boolean isCrawlAllProduct = true;
            List<NKProduct> products = new ArrayList<>();

            while (true) {
                String url = String.format("%s/%s/page-%d", mainUrl, category, page);
                System.out.println("Đang crawl page: " + page + "\nurl: " + url);

                String response = sendRequest(url);
                if (response != null) {
                    Document doc = Jsoup.parse(response);
                    Element productsContent = doc.getElementById("pagination_contents");
                    if (productsContent == null) {
                        System.out.println("Trang không có dữ liệu! Dừng crawl");
                        break;
                    }

                    addProducts(products, doc);
                    page++;
                } else {
                    System.out.println("Error: Unable to fetch data.");
                    break;
                }
            }

            System.out.println("Tổng số sản phẩm crawl: " + products.size());
            return exportProductsToCsv(products, "NK", fileLocation);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String sendRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Thiết lập phương thức POST
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setInstanceFollowRedirects(false); // Tắt theo dõi tự động

        // Kiểm tra mã phản hồi
        int statusCode = conn.getResponseCode();
//        System.out.println("Response Code: " + statusCode);

        if (statusCode == HttpURLConnection.HTTP_OK) {
            // Đọc phản hồi từ InputStream
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return response.toString();
            } catch (Exception e){
                return null;
            }
        } else if (statusCode == HttpURLConnection.HTTP_MOVED_PERM || statusCode == HttpURLConnection.HTTP_MOVED_TEMP) {
            String location = conn.getHeaderField("Location");
            // Thực hiện yêu cầu đến URL mới
            return sendRequest(location); // Gọi lại hàm cho URL mới
        } else {
            // Đọc phản hồi lỗi
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;

                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                System.err.println("Error Response: " + errorResponse);
                return null;
            }
        }
    }

    public NKProduct getProductDetail(NKProduct product) throws Exception {
        String response = sendRequest(product.getProductLink());
        if (response == null) {
            return null;
        }
        try {
            Document doc = Jsoup.parse(response);
            Element content = doc.getElementById("productSpecification_fullContent");
            // Lấy các giá trị từ HTML
            product.setScreenSize(getValue(doc, "Kích thước màn hình:"));
            product.setResolution(getValue(doc, "Độ phân giải:"));
            product.setOperatingSystem(getValue(doc, "Hệ điều hành - Giao diện:")); // Thay thế với dữ liệu thực nếu có
            product.setImageTechnology(getValue(doc, "Công nghệ xử lí hình ảnh:"));
            product.setProcessor(getValue(doc, "Bộ vi xử lí:")); // Thay thế với dữ liệu thực nếu có
            product.setRefreshRate(getValue(doc, "Tần số quét:")); // Thay thế với dữ liệu thực nếu có
            product.setSpeakerPower(getValue(doc, "Tổng công suất loa:"));
            product.setInternetConnection(getValue(doc, "Cổng Internet (LAN):"));
            product.setWirelessConnectivity(getValue(doc, "Cổng WiFi:"));
            product.setUsbPorts(getValue(doc, "Cổng USB:"));
            product.setVideoAudioInputPorts(getValue(doc, "Cổng HDMI:")); // Có thể cần điều chỉnh
            product.setManufacturer(getValue(doc, "Nhà sản xuất:"));
            product.setManufacturedIn(getValue(doc, "Xuất xứ:"));
            product.setReleaseYear(getValue(doc, "Năm ra mắt :")); // Thay thế với dữ liệu thực nếu có
            product.setWarrantyPeriod(getValue(doc, "Thời gian bảo hành:"));
            product.setTiviType(getValue(doc, "Loại Tivi:"));
            product.setHdr(getValue(doc, "HDR:"));
            product.setSoundTechnology(getValue(doc, "Công nghệ âm thanh:"));
            product.setMemory(getValue(doc, "Bộ nhớ:")); // Thay thế với dữ liệu thực nếu có
            product.setVoiceSearch(getValue(doc, "Tìm kiếm bằng giọng nói:")); // Thay thế với dữ liệu thực nếu có
            return product;
        } catch (Exception e) {
            return null;
        }
    }

    public void addProducts(List<NKProduct> products, Document doc) throws Exception {
        DecimalFormat decimalFormat = new DecimalFormat("###0.00"); // Định dạng với dấu phẩy và 2 chữ số thập phân

        Element productsContent = doc.getElementById("pagination_contents");
        Elements productsElement = productsContent.select("script.script-render");
        for (Element productElement : productsElement) {
            String scriptContent = productElement.html();

            // Kiểm tra xem nội dung có chứa dữ liệu cần tìm không
            Pattern pattern = Pattern.compile("dataRenderProduct.push\\((.*?)\\);", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(scriptContent);

            if (matcher.find()) {
                String jsonData = matcher.group(1); // Lấy phần JSON
                JSONObject productData = new JSONObject(jsonData); // Chuyển đổi chuỗi JSON thành JSONObject
                String id = productData.getString("product_id");
                String name = productData.getString("product");
                String price = decimalFormat.format(productData.getDouble("price"));
                String oldPrice = decimalFormat.format(productData.getDouble("offline_price"));

                String imgLink = productData.getString("url_digital_images");
                int discountPercent = productData.getInt("percent_discount");
                if(discountPercent==0) oldPrice = price;
                String productLink = doc.selectFirst("[product-id="+id+"]").attr("href");
                NKProduct product = new NKProduct(id, name, price, oldPrice, imgLink, discountPercent+"", productLink);
                if(!ids.contains(id)){
                    product = getProductDetail(product);
                    products.add(product);
                    ids.add(id);
                }
            }
        }
    }


    public String getValue(Document doc, String title) {
        Element row = doc.select("td.title:contains(" + title + ")").first();
        if (row != null) {
            return row.nextElementSibling().text(); // Lấy giá trị từ ô bên cạnh
        }
        return "";
    }
}
