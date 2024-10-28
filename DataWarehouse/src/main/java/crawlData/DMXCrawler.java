package crawlData;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.nio.charset.StandardCharsets;


public class DMXCrawler {
    private static String mainUrl;
    private static String fileLocation;

    public static void main(String[] args) {
        mainUrl = "https://www.dienmayxanh.com";
        fileLocation = "./data/DMX";
        crawlDMXData("tivi");
    }

    public static void crawlDMXData(String category) {
        if(mainUrl == null){
            System.out.println("Vui lòng thiết lập URL");
        }else{
            try {
                String idCategory = parseCategory(category);
                int page = 0;
                boolean isCrawlAllProduct = true;
                List<DMXProduct> DMXProducts = new ArrayList<>();

                while (isCrawlAllProduct) {
                    String url = createUrlCategory(mainUrl, category, idCategory, 13, page);
                    System.out.println("Đang crawl page: " + page + "\nurl: " + url);

                    String response = sendPostRequest(url);
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);

                        // Lấy thông tin cần thiết
                        boolean isShowFilterMonopoly = jsonObject.getBoolean("isShowFilterMonopoly");
                        int totalProducts = jsonObject.getInt("total");
                        String listProductsHtml = jsonObject.getString("listproducts");
                        Document doc = Jsoup.parse(listProductsHtml);
                        Elements listItems = doc.select("li.item.__cate_"+idCategory);
                        addProducts(DMXProducts, listItems);

                        if(totalProducts <20){
                            isCrawlAllProduct = false;
                        }
                        page++;
                    } else {
                        System.out.println("Error: Unable to fetch data.");
                        break;
                    }
                }

                System.out.println("Tổng số sản phẩm crawl: " + DMXProducts.size());
                exportProductsToCsv(DMXProducts, "DMX", fileLocation);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String sendPostRequest(String urlString) throws IOException {
        boolean isParentCate = false, isShowCompare= false, prevent = false;
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
    private static DMXProduct getProductDetail(DMXProduct DMXProduct) throws IOException {
        DMXProduct result = DMXProduct;
//        System.out.println(product.getProductLink());
        URL url = new URL(DMXProduct.getProductLink());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");


        // Kiểm tra mã phản hồi
        int statusCode = conn.getResponseCode();
        System.out.println("Response Code: " + statusCode);

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
                                manufacturer = li.select("span").text().replaceAll("Xem thông tin hãng","");
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
    private static void addProducts(List<DMXProduct> products, Elements productsElement) throws IOException {
        for (Element productElement : productsElement) {
            String id = productElement.attr("data-id");
            String name = productElement.select("h3").text().trim();
            String price = productElement.select("strong.price").text().replace("₫", "").replace(".", "");
            String priceOld = price;
            String imgLink = productElement.select("img").attr("data-src");
            String productLink = mainUrl +productElement.select("a").attr("href");
//            String oldPrice = productElement.select("p.price-old.black").text().replace("₫", "").replace(".", "");
            String discountPercent = "0";
            String itemGift = "";

            Element boxPElement = productElement.selectFirst("div.box-p");
            if (boxPElement != null) {
                Element percentElement = boxPElement.selectFirst("span.percent");
                if (percentElement != null) {
                    discountPercent = percentElement.text().replace("%", "");
                }

                if(!discountPercent.equals("0")){
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
            product = getProductDetail(product);
//            System.out.println(product.getItemGift());
//            System.out.println("-----------------------------");
            products.add(product);
        }
    }

    private static String parseCategory(String category) {
        switch (category) {
            case "tivi": return "1942";
            case "tu-lanh": return "1943";
            case "may-lanh": return "2002";
            case "bo-lau-nha": return "4927";
            case "lo-vi-song": return "1987";
            case "dtdd": return "42";
            default: throw new IllegalArgumentException("Không có giá trị cho category: " + category);
        }
    }

    private static String createUrlCategory(String mainUrl, String category, String idCategory, int o, int page) {
        return String.format("%s/Category/FilterProductBox?c=%s&o=%d&pi=%d", mainUrl, idCategory, o, page);
    }


    private static void exportProductsToCsv(List<DMXProduct> DMXProducts, String source, String location) {
        String filename = String.format("%s/%sdata_%s.csv",location, source, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Ghi tiêu đề
            writer.write("id,name,price,oldPrice," +
                    "imgLink,discountPercent,productLink,screenSize," +
                    "resolution,screenType,operatingSystem,imageTechnology," +
                    "processor,refreshRate,speakerPower,internetConnection," +
                    "wirelessConnectivity,usbPorts,videoAudioInputPorts," +
                    "audioOutputPorts,manufacturer,manufacturedIn,releaseYear,itemGift, crawlDate\n");

            // Ghi từng sản phẩm
            for (DMXProduct product : DMXProducts) {
                String st = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        escapeCsvValue(product.getId()),
                        escapeCsvValue(product.getName()),
                        escapeCsvValue(product.getPrice()),
                        escapeCsvValue(product.getOldPrice()),
                        escapeCsvValue(product.getImgLink()),
                        escapeCsvValue(product.getDiscountPercent()),
                        escapeCsvValue(product.getProductLink()),
                        escapeCsvValue(product.getScreenSize()),
                        escapeCsvValue(product.getResolution()),
                        escapeCsvValue(product.getScreenType()),
                        escapeCsvValue(product.getOperatingSystem()),
                        escapeCsvValue(product.getImageTechnology()),
                        escapeCsvValue(product.getProcessor()),
                        escapeCsvValue(product.getRefreshRate()),
                        escapeCsvValue(product.getSpeakerPower()),
                        escapeCsvValue(product.getInternetConnection()),
                        escapeCsvValue(product.getWirelessConnectivity()),
                        escapeCsvValue(product.getUsbPorts()),
                        escapeCsvValue(product.getVideoAudioInputPorts()),
                        escapeCsvValue(product.getAudioOutputPorts()),
                        escapeCsvValue(product.getManufacturer()),
                        escapeCsvValue(product.getManufacturedIn()),
                        escapeCsvValue(product.getReleaseYear()),
                        escapeCsvValue(product.getItemGift()),
                        new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                writer.write(st);
                System.out.println(st);

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

}
