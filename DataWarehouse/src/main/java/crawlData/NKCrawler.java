package crawlData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NKCrawler {
    private static String mainUrl ;
    private static String fileLocation;
    private static final int MAX_REDIRECTS = 100;
    public static void main(String[] args) {
        mainUrl = "https://www.nguyenkim.com";
        fileLocation = "./data/NK";
        crawlDNKata("tivi");
    }
    public static void crawlDNKata(String category) {
        try {
//            String idCategory = parseCategory(category);
            int page = 0;
            boolean isCrawlAllProduct = true;
            List<NKProduct> products = new ArrayList<>();

            while (isCrawlAllProduct) {
                String url = String.format("%s/%s/page-%d", mainUrl, category, page);
                System.out.println("Đang crawl page: " + page + "\nurl: " + url);

                String response = sendGetRequest(url);
//                System.out.println(response);
                if (response != null) {
                    Document doc = Jsoup.parse(response);
                    Element productsContent = doc.getElementById("pagination_contents");
//                    System.out.println(productsContent);
                    if(productsContent ==null){
                        System.out.println("Trang không có dữ liệu! Dừng crawl");
                        isCrawlAllProduct = false;
                        break;
                    }
                    Elements productsElement = productsContent.select("a.product-render");
//                    System.out.println(products);
//                    for(Element product:products){
//
//                        System.out.println(product.attr("href"));
//                    }
                    addProducts(products, productsElement);
                    page++;
                } else {
                    System.out.println("Error: Unable to fetch data.");
                    break;
                }
            }

            System.out.println("Tổng số sản phẩm crawl: " + products.size());
            exportProductsToCsv(products, "NK", fileLocation);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String sendGetRequest(String urlString) throws IOException {
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
            }
        } else if (statusCode == HttpURLConnection.HTTP_MOVED_PERM || statusCode == HttpURLConnection.HTTP_MOVED_TEMP) {
            String location = conn.getHeaderField("Location");
            // Thực hiện yêu cầu đến URL mới
            return sendGetRequest(location); // Gọi lại hàm cho URL mới
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
    private static NKProduct getProductDetail(NKProduct product) throws IOException {
        String response = sendGetRequest(product.getProductLink());
        if(response == null){
            return null;
        }
        try{
            Document doc = Jsoup.parse(response);
//        System.out.println(product.getProductLink());
                String img = mainUrl + doc.getElementById("content_description").selectFirst("img.imagelazyload")
                        .attr("data-src");
                product.setImgLink(img);
            String oldPrice =doc.selectFirst("span.nk-price-final").text().replaceAll(".","").replaceAll("đ","");
            String price = oldPrice;
            if(doc.selectFirst("p.nk-shock-price") != null){
                price =doc.selectFirst("p.nk-shock-price").text().replaceAll(".","").replaceAll("đ","");
            }
            String discountPercent ="0";
            if(doc.selectFirst("span.nk-product-discount-percent") != null){
                price =doc.selectFirst("span.nk-product-discount-percent").text().replaceAll("%","");
            }
            Element content = doc.getElementById("productSpecification_fullContent");
            product.setPrice(price);
            product.setOldPrice(oldPrice);
            product.setDiscountPercent(discountPercent);
            // Lấy các giá trị từ HTML
            product.setScreenSize(getValue(doc, "Kích thước màn hình:"));
            product.setResolution(getValue(doc, "Độ phân giải:"));
            product.setOperatingSystem(getValue(doc,"Hệ điều hành - Giao diện:")); // Thay thế với dữ liệu thực nếu có
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
            product.setReleaseYear("N/A"); // Thay thế với dữ liệu thực nếu có
            product.setWarrantyPeriod(getValue(doc, "Thời gian bảo hành:"));
            product.setTiviType(getValue(doc, "Loại Tivi:"));
            product.setHdr(getValue(doc, "HDR:"));
            product.setSoundTechnology(getValue(doc, "Công nghệ âm thanh:"));
            product.setMemory(getValue(doc, "Bộ nhớ:")); // Thay thế với dữ liệu thực nếu có
            product.setVoiceSearch(getValue(doc, "Tìm kiếm bằng giọng nói:")); // Thay thế với dữ liệu thực nếu có
//        System.out.println(product);
            return product;
        } catch (Exception e) {
            return null;
        }
    }
    private static void addProducts(List<NKProduct> products, Elements productsElement) throws IOException {
        for (Element productElement : productsElement) {
            String id = productElement.attr("product-id");
            String name = productElement.attr("name");
            String productLink = productElement.attr("href");
            NKProduct product = new NKProduct(id, name, productLink);
            product = getProductDetail(product);
//            break;
//            System.out.println(product.getItemGift());
//            System.out.println("-----------------------------");
            if (product == null) continue;
            products.add(product);
        }
    }



    private static void exportProductsToCsv(List<NKProduct> products, String source, String location) {
        String filename = String.format("%s/%sdata_%s.csv",location, source, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Ghi tiêu đề
            writer.write("id,name,price,oldPrice," +
                    "imgLink,discountPercent,productLink,screenSize," +
                    "resolution,operatingSystem,imageTechnology," +
                    "processor,refreshRate,speakerPower,internetConnection," +
                    "wirelessConnectivity,usbPorts,videoAudioInputPorts," +
                    "manufacturer,manufacturedIn,releaseYear,warrantyPeriod," +
                    "tiviType,hdr,soundTechnology,memory,voiceSearch,crawlDate\n");

            // Ghi từng sản phẩm
            for (NKProduct product : products) {
//                System.out.println(product.getPrice());
                String st = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        escapeCsvValue(product.getId()),
                        escapeCsvValue(product.getName()),
                        escapeCsvValue(product.getPrice()),
                        escapeCsvValue(product.getOldPrice()),
                        escapeCsvValue(product.getImgLink()),
                        escapeCsvValue(product.getDiscountPercent()),
                        escapeCsvValue(product.getProductLink()),
                        escapeCsvValue(product.getScreenSize()),
                        escapeCsvValue(product.getResolution()),
                        escapeCsvValue(product.getOperatingSystem()),
                        escapeCsvValue(product.getImageTechnology()),
                        escapeCsvValue(product.getProcessor()),
                        escapeCsvValue(product.getRefreshRate()),
                        escapeCsvValue(product.getSpeakerPower()),
                        escapeCsvValue(product.getInternetConnection()),
                        escapeCsvValue(product.getWirelessConnectivity()),
                        escapeCsvValue(product.getUsbPorts()),
                        escapeCsvValue(product.getVideoAudioInputPorts()),
                        escapeCsvValue(product.getManufacturer()),
                        escapeCsvValue(product.getManufacturedIn()),
                        escapeCsvValue(product.getReleaseYear()),
                        escapeCsvValue(product.getWarrantyPeriod()),
                        escapeCsvValue(product.getTiviType()),
                        escapeCsvValue(product.getHdr()),
                        escapeCsvValue(product.getSoundTechnology()),
                        escapeCsvValue(product.getMemory()),
                        escapeCsvValue(product.getVoiceSearch()),
                        new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                writer.write(st);
//                System.out.println(st);

            }
            System.out.println("Data has been exported to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    // Phương thức để escape giá trị cho CSV
    private static String escapeCsvValue(String value) {
        if (value == null) {
            return ""; // Trả về chuỗi rỗng nếu giá trị là null
        }
        value = value.replace("\"", "\"\""); // Thay thế dấu nháy kép bằng hai dấu nháy kép
        return "\"" + value + "\""; // Bao quanh giá trị bằng dấu nháy kép
    }
    private static String getValue(Document doc, String title) {
        Element row = doc.select("td.title:contains(" + title + ")").first();
        if (row != null) {
            return row.nextElementSibling().text(); // Lấy giá trị từ ô bên cạnh
        }
        return "N/A"; // Hoặc giá trị mặc định nếu không tìm thấy
    }
}
