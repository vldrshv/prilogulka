package com.example.prilogulka.data;

public class GiftCard {
    private int id;
    private String name;
    private int destination;
    private int coastBronze, coastSilver, coastGolden;
    private String description;
    private String bronzeCode;
    private String silverCode;
    private String goldenCode;
    private String ownerEmail;

    public GiftCard(int id, String name, int destination,
                    int coast_bronze, int coast_silver, int coast_golden,
                    String description,
                    String bronze_code,
                    String silver_code,
                    String golden_code,
                    String ownerEmail) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.coastBronze = coast_bronze;
        this.coastSilver = coast_silver;
        this.coastGolden = coast_golden;
        this.description = description;
        this.bronzeCode = bronze_code;
        this.silverCode = silver_code;
        this.goldenCode = golden_code;
        this.ownerEmail = ownerEmail;
    }

    public GiftCard(String name, int destination,
                    int coast_bronze, int coast_silver, int coast_golden,
                    String description,
                    String bronze_code,
                    String silver_code,
                    String golden_code,
                    String ownerEmail) {
        this.name = name;
        this.destination = destination;
        this.coastBronze = coast_bronze;
        this.coastSilver = coast_silver;
        this.coastGolden = coast_golden;
        this.description = description;
        this.bronzeCode = bronze_code;
        this.silverCode = silver_code;
        this.goldenCode = golden_code;
        this.ownerEmail = ownerEmail;

    }

    public GiftCard() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getCoastBronze() {
        return coastBronze;
    }

    public void setCoastBronze(int coastBronze) {
        this.coastBronze = coastBronze;
    }

    public int getCoastSilver() {
        return coastSilver;
    }

    public void setCoastSilver(int coastSilver) {
        this.coastSilver = coastSilver;
    }

    public int getCoastGolden() {
        return coastGolden;
    }

    public void setCoastGolden(int coastGolden) {
        this.coastGolden = coastGolden;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBronzeCode() {
        return bronzeCode;
    }

    public void setBronzeCode(String bronzeCode) {
        this.bronzeCode = bronzeCode;
    }

    public String getSilverCode() {
        return silverCode;
    }

    public void setSilverCode(String silverCode) {
        this.silverCode = silverCode;
    }

    public String getGoldenCode() {
        return goldenCode;
    }

    public void setGoldenCode(String goldenCode) {
        this.goldenCode = goldenCode;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
}
