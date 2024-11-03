package crawlData;

public abstract class AProduct {
    protected String id;
    protected String name;
    protected String price;
    protected String oldPrice;
    protected String imgLink;
    protected String discountPercent;
    protected String productLink;
    protected String screenSize;
    protected String resolution;
    protected String operatingSystem;
    protected String imageTechnology;
    protected String processor;
    protected String refreshRate;
    protected String speakerPower;
    protected String internetConnection;
    protected String wirelessConnectivity;
    protected String usbPorts;
    protected String videoAudioInputPorts;
    protected String manufacturer;
    protected String manufacturedIn;
    protected String releaseYear;

    public AProduct(String id, String name, String price, String oldPrice, String imgLink, String discountPercent, String productLink, String screenSize, String resolution, String operatingSystem, String imageTechnology, String processor, String refreshRate, String speakerPower, String internetConnection, String wirelessConnectivity, String usbPorts, String videoAudioInputPorts, String manufacturer, String manufacturedIn, String releaseYear) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.oldPrice = oldPrice;
        this.imgLink = imgLink;
        this.discountPercent = discountPercent;
        this.productLink = productLink;
        this.screenSize = screenSize;
        this.resolution = resolution;
        this.operatingSystem = operatingSystem;
        this.imageTechnology = imageTechnology;
        this.processor = processor;
        this.refreshRate = refreshRate;
        this.speakerPower = speakerPower;
        this.internetConnection = internetConnection;
        this.wirelessConnectivity = wirelessConnectivity;
        this.usbPorts = usbPorts;
        this.videoAudioInputPorts = videoAudioInputPorts;
        this.manufacturer = manufacturer;
        this.manufacturedIn = manufacturedIn;
        this.releaseYear = releaseYear;
    }

    public AProduct() {
    }

    public AProduct(String id, String name, String price, String oldPrice, String imgLink, String discountPercent, String productLink) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.oldPrice = oldPrice;
        this.imgLink = imgLink;
        this.discountPercent = discountPercent;
        this.productLink = productLink;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public void setImageTechnology(String imageTechnology) {
        this.imageTechnology = imageTechnology;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public void setRefreshRate(String refreshRate) {
        this.refreshRate = refreshRate;
    }

    public void setSpeakerPower(String speakerPower) {
        this.speakerPower = speakerPower;
    }

    public void setInternetConnection(String internetConnection) {
        this.internetConnection = internetConnection;
    }

    public void setWirelessConnectivity(String wirelessConnectivity) {
        this.wirelessConnectivity = wirelessConnectivity;
    }

    public void setUsbPorts(String usbPorts) {
        this.usbPorts = usbPorts;
    }

    public void setVideoAudioInputPorts(String videoAudioInputPorts) {
        this.videoAudioInputPorts = videoAudioInputPorts;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setManufacturedIn(String manufacturedIn) {
        this.manufacturedIn = manufacturedIn;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public String getImgLink() {
        return imgLink;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public String getProductLink() {
        return productLink;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public String getResolution() {
        return resolution;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public String getImageTechnology() {
        return imageTechnology;
    }

    public String getProcessor() {
        return processor;
    }

    public String getRefreshRate() {
        return refreshRate;
    }

    public String getSpeakerPower() {
        return speakerPower;
    }

    public String getInternetConnection() {
        return internetConnection;
    }

    public String getWirelessConnectivity() {
        return wirelessConnectivity;
    }

    public String getUsbPorts() {
        return usbPorts;
    }

    public String getVideoAudioInputPorts() {
        return videoAudioInputPorts;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getManufacturedIn() {
        return manufacturedIn;
    }

    public String getReleaseYear() {
        return releaseYear;
    }
}
