package com.aeropress.projekt.patryk.barcodescanner;

public class Product {

    public String barcode;
    public String name;
    public float cal;
    public float carbon;
    public float fat;
    public float protein;

    public Product(String bar,String name, float cal,float carbon, float fat,float protein)
    {
        this.barcode=bar;
        this.name=name;
        this.cal=cal;
        this.carbon=carbon;
        this.fat=fat;
        this.protein=protein;
    }


}
