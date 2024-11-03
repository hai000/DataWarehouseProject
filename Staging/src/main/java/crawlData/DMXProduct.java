package crawlData;

public class DMXProduct extends AProduct {
    private String screenType;
    private String audioOutputPorts;
    private String itemGift;

    public DMXProduct(String id, String name, String price, String oldPrice, String imgLink, String discountPercent, String productLink, String screenSize, String resolution, String operatingSystem, String imageTechnology, String processor, String refreshRate, String speakerPower, String internetConnection, String wirelessConnectivity, String usbPorts, String videoAudioInputPorts, String manufacturer, String manufacturedIn, String releaseYear, String screenType, String audioOutputPorts, String itemGift) {
        super(id, name, price, oldPrice, imgLink, discountPercent, productLink, screenSize, resolution, operatingSystem, imageTechnology, processor, refreshRate, speakerPower, internetConnection, wirelessConnectivity, usbPorts, videoAudioInputPorts, manufacturer, manufacturedIn, releaseYear);
        this.screenType = screenType;
        this.audioOutputPorts = audioOutputPorts;
        this.itemGift = itemGift;
    }

    public DMXProduct(String id, String name, String price, String oldPrice, String imgLink, String discountPercent, String productLink, String itemGift) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.oldPrice = oldPrice;
        this.imgLink = imgLink;
        this.discountPercent = discountPercent;
        this.productLink = productLink;
        this.itemGift = itemGift;
    }

    public String getScreenType() {
        return screenType;
    }

    public String getAudioOutputPorts() {
        return audioOutputPorts;
    }

    public String getItemGift() {
        return itemGift;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public void setAudioOutputPorts(String audioOutputPorts) {
        this.audioOutputPorts = audioOutputPorts;
    }

    public void setItemGift(String itemGift) {
        this.itemGift = itemGift;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", oldPrice='" + oldPrice + '\'' +
                ", imgLink='" + imgLink + '\'' +
                ", productLink='" + productLink + '\'' +
                ", screenSize='" + screenSize + '\'' +
                ", resolution='" + resolution + '\'' +
                ", screenType='" + screenType + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", processor='" + processor + '\'' +
                ", refreshRate='" + refreshRate + '\'' +
                ", speakerPower='" + speakerPower + '\'' +
                ", internetConnection='" + internetConnection + '\'' +
                ", wirelessConnectivity='" + wirelessConnectivity + '\'' +
                ", usbPorts='" + usbPorts + '\'' +
                ", videoAudioInputPorts='" + videoAudioInputPorts + '\'' +
                ", audioOutputPorts='" + audioOutputPorts + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", manufacturedIn='" + manufacturedIn + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", discountPercent='" + discountPercent + '\'' +
                ", itemGift='" + itemGift + '\'' +
                '}';
    }
}
