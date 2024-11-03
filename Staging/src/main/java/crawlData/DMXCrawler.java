package crawlData;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.nio.charset.StandardCharsets;


public class DMXCrawler extends ACrawler<DMXProduct>{
    private String idCategory;
    public void main(String[] args) {
        System.out.println(this.crawlData());;
    }

    public String crawlData() {
        loadConfig("dmx_tivi");
        String category = "tivi";
        if (mainUrl == null) {
            System.out.println("Vui lòng thiết lập URL");
            return null;
        } else {
            try {
                 idCategory = parseCategory(category);
                int page = 0;
                boolean isCrawlAllProduct = true;
                List<DMXProduct> products = new ArrayList<>();

                while (true) {
                    String url = createUrlCategory(mainUrl, category, idCategory, 13, page);
                    System.out.println("Đang crawl page: " + page + "\nurl: " + url);

                    String response = sendRequest(url);
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);

                        // Lấy thông tin cần thiết
                        boolean isShowFilterMonopoly = jsonObject.getBoolean("isShowFilterMonopoly");
                        int totalProducts = jsonObject.getInt("total");
                        String listProductsHtml = jsonObject.getString("listproducts");
                        Document doc = Jsoup.parse(listProductsHtml);
                        addProducts(products, doc);

                        if (totalProducts < 20) {
                            break;
                        }
                        page++;
                    } else {
                        System.out.println("Error: Unable to fetch data.");
                        break;
                    }
                }

                System.out.println("Tổng số sản phẩm crawl: " + products.size());
                return exportProductsToCsv(products, "DMX", fileLocation);

            } catch (Exception e) {

                e.printStackTrace();
                return null;
            }
        }
    }

    public String sendRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setDoOutput(true); // Cho phép ghi dữ liệu vào thân yêu cầu

        // Chuẩn bị dữ liệu form
        String formData = "IsParentCate=False&IsShowCompare=True&prevent=True";

        // Ghi dữ liệu vào OutputStream
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = formData.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

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
//                System.out.println(response.toString());
                return response.toString();
            }
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

    public DMXProduct getProductDetail(DMXProduct product) throws IOException {
        DMXProduct result = (DMXProduct) product;
//        System.out.println(product.getProductLink());
        URL url = new URL(result.getProductLink());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");


        // Kiểm tra mã phản hồi
        int statusCode = conn.getResponseCode();


        if (statusCode == HttpURLConnection.HTTP_OK) {
            // Đọc phản hồi từ InputStream
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                Document doc = Jsoup.parse(response.toString());
                Elements mainContent = doc.select("div.specifications.tab-content.current");
                String screenSize = "";
                String resolution = "";
                String screenType = "";
                String operatingSystem = "";
                String imageTechnology = "";
                String processor = "";
                String refreshRate = "";
                String speakerPower = "";
                String internetConnection = "";
                String wirelessConnectivity = "";
                String usbPorts = "";
                String videoAudioInputPorts = "";
                String audioOutputPorts = "";
                String manufacturer = "";
                String manufacturedIn = "";
                String releaseYear = "";

                // Lấy thông tin từ các thẻ <li>
                Elements specifications = mainContent.select("div.box-specifi ul.text-specifi");

                for (Element specification : specifications) {
                    for (Element li : specification.select("li")) {
                        String label = li.select("strong").text().toLowerCase();

                        switch (label) {
                            case "kích cỡ màn hình:":
                                screenSize = li.select("span").text();
                                break;
                            case "độ phân giải:":
                                resolution = li.select("a").text(); // Nếu có thẻ <a>
                                break;
                            case "loại màn hình:":
                                screenType = li.select("span").text();
                                break;
                            case "hệ điều hành:":
                                operatingSystem = li.select("span").text();
                                break;
                            case "công nghệ hình ảnh:":
                                imageTechnology = li.select("a").text(); // Nếu có thẻ <a>
                                break;
                            case "bộ xử lý:":
                                processor = li.select("a").text(); // Nếu có thẻ <a>
                                break;
                            case "tần số quét thực:":
                                refreshRate = li.select("span").text();
                                break;
                            case "tổng công suất loa:":
                                speakerPower = li.select("span").text();
                                break;
                            case "kết nối internet:":
                                internetConnection = li.select("span").text();
                                break;
                            case "kết nối không dây:":
                                wirelessConnectivity = li.select("span").text();
                                break;
                            case "usb:":
                                usbPorts = li.select("span").text();
                                break;
                            case "cổng nhận hình ảnh, âm thanh:":
                                videoAudioInputPorts = li.select("span").text();
                                break;
                            case "cổng xuất âm thanh:":
                                audioOutputPorts = li.select("span").text();
                                break;
                            case "hãng:":
                                manufacturer = li.select("span").text().replaceAll("Xem thông tin hãng", "");
                                break;
                            case "nơi sản xuất:":
                                manufacturedIn = li.select("span").text();
                                break;
                            case "năm ra mắt:":
                                releaseYear = li.select("span").text();
                                break;
                        }
                    }
                }
                result.setScreenSize(screenSize);
                result.setResolution(resolution);
                result.setScreenType(screenType);
                result.setOperatingSystem(operatingSystem);
                result.setImageTechnology(imageTechnology);
                result.setProcessor(processor);
                result.setRefreshRate(refreshRate);
                result.setSpeakerPower(speakerPower);
                result.setInternetConnection(internetConnection);
                result.setWirelessConnectivity(wirelessConnectivity);
                result.setUsbPorts(usbPorts);
                result.setVideoAudioInputPorts(videoAudioInputPorts);
                result.setAudioOutputPorts(audioOutputPorts);
                result.setManufacturer(manufacturer);
                result.setManufacturedIn(manufacturedIn);
                result.setReleaseYear(releaseYear);


            }
        } else {
            // Đọc phản hồi lỗi
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;

                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                System.err.println("Error Response: " + errorResponse);
//                return null;
            }
        }

        return result;
    }

    public void addProducts(List<DMXProduct> products, Document doc) throws IOException {
        Elements listItems = doc.select("li.item.__cate_" + idCategory);

        for (Element productElement : listItems) {
            String id = productElement.attr("data-id");
            String name = productElement.select("h3").text().trim();
            String price = productElement.select("strong.price").text().replace("₫", "").replace(".", "");
            if(price.length() ==0) continue;
            String priceOld = price;
            String imgLink = productElement.select("img").attr("data-src");
            String productLink = mainUrl + productElement.select("a").attr("href");
//            String oldPrice = productElement.select("p.price-old.black").text().replace("₫", "").replace(".", "");
            String discountPercent = "0";
            String itemGift = "";

            Element boxPElement = productElement.selectFirst("div.box-p");
            if (boxPElement != null) {
                Element percentElement = boxPElement.selectFirst("span.percent");
                if (percentElement != null) {
                    discountPercent = percentElement.text().replace("%", "");
                }

                if (!discountPercent.equals("0")) {
                    Element priceOldElement = boxPElement.selectFirst("p.price-old.black");
                    if (priceOldElement != null) {
                        priceOld = priceOldElement.text().replace("₫", "").replace(".", "");
                    }
                }

            }

            Element itemGiftElement = productElement.selectFirst("p.item-gift");
//            System.out.println(itemGiftElement);
            if (itemGiftElement != null) {
                itemGift = itemGiftElement.text();
//                System.out.println(itemGift);

            }
            DMXProduct product = new DMXProduct(id, name, price, priceOld, imgLink, discountPercent, productLink, itemGift);
            product = (DMXProduct) getProductDetail(product);
//            System.out.println(product.getItemGift());
//            System.out.println("-----------------------------");
            products.add(product);
        }
    }
    public String parseCategory(String category) {
        switch (category) {
            case "tivi":
                return "1942";
            case "tu-lanh":
                return "1943";
            case "may-lanh":
                return "2002";
            case "bo-lau-nha":
                return "4927";
            case "lo-vi-song":
                return "1987";
            case "dtdd":
                return "42";
            default:
                throw new IllegalArgumentException("Không có giá trị cho category: " + category);
        }
    }

    public String createUrlCategory(String mainUrl, String category, String idCategory, int o, int page) {
        return String.format("%s/Category/FilterProductBox?c=%s&o=%d&pi=%d", mainUrl, idCategory, o, page);
    }




}
