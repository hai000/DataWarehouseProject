package crawlData;

public class NKProduct extends AProduct {
    private String warrantyPeriod;
    private String tiviType;
    private String hdr;
    private String soundTechnology;
    private String memory;
    private String voiceSearch;

    public NKProduct(String id, String name, String price, String oldPrice, String imgLink, String discountPercent, String productLink, String screenSize, String resolution, String operatingSystem, String imageTechnology, String processor, String refreshRate, String speakerPower, String internetConnection, String wirelessConnectivity, String usbPorts, String videoAudioInputPorts, String manufacturer, String manufacturedIn, String releaseYear, String warrantyPeriod, String tiviType, String hdr, String sounTechnology, String memory, String voiceSearch) {
        super(id, name, price, oldPrice, imgLink, discountPercent, productLink, screenSize, resolution, operatingSystem, imageTechnology, processor, refreshRate, speakerPower, internetConnection, wirelessConnectivity, usbPorts, videoAudioInputPorts, manufacturer, manufacturedIn, releaseYear);
        this.warrantyPeriod = warrantyPeriod;
        this.tiviType = tiviType;
        this.hdr = hdr;
        this.soundTechnology = sounTechnology;
        this.memory = memory;
        this.voiceSearch = voiceSearch;
    }

    public NKProduct(String id, String name, String price, String oldPrice, String imgLink, String discountPercent, String productLink) {
        super(id, name, price, oldPrice, imgLink, discountPercent, productLink);
    }

    public NKProduct() {
    }

    public String getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public String getTiviType() {
        return tiviType;
    }

    public String getHdr() {
        return hdr;
    }

    public String getSoundTechnology() {
        return soundTechnology;
    }

    public String getMemory() {
        return memory;
    }

    public String getVoiceSearch() {
        return voiceSearch;
    }

    public void setWarrantyPeriod(String warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public void setTiviType(String tiviType) {
        this.tiviType = tiviType;
    }

    public void setHdr(String hdr) {
        this.hdr = hdr;
    }

    public void setSoundTechnology(String soundTechnology) {
        this.soundTechnology = soundTechnology;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public void setVoiceSearch(String voiceSearch) {
        this.voiceSearch = voiceSearch;
    }

    @Override
    public String toString() {
        return "NKProduct{" +

                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", oldPrice='" + oldPrice + '\'' +
                ", imgLink='" + imgLink + '\'' +
                ", discountPercent='" + discountPercent + '\'' +
                ", productLink='" + productLink + '\'' +
                ", screenSize='" + screenSize + '\'' +
                ", resolution='" + resolution + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", imageTechnology='" + imageTechnology + '\'' +
                ", processor='" + processor + '\'' +
                ", refreshRate='" + refreshRate + '\'' +
                ", speakerPower='" + speakerPower + '\'' +
                ", internetConnection='" + internetConnection + '\'' +
                ", wirelessConnectivity='" + wirelessConnectivity + '\'' +
                ", usbPorts='" + usbPorts + '\'' +
                ", videoAudioInputPorts='" + videoAudioInputPorts + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", manufacturedIn='" + manufacturedIn + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", warrantyPeriod='" + warrantyPeriod + '\'' +
                ", tiviType='" + tiviType + '\'' +
                ", hdr='" + hdr + '\'' +
                ", soundTechnology='" + soundTechnology + '\'' +
                ", memory='" + memory + '\'' +
                ", voiceSearch='" + voiceSearch + '\'' +
                '}';
    }
}
