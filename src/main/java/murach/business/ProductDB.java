package murach.business;

import java.util.ArrayList;
import java.util.List;

public class ProductDB {

    // Tra cứu 1 sản phẩm theo mã (dùng khi Add to Cart / Update)
    public static Product getProduct(String productCode) {
        Product p = new Product();
        p.setCode(productCode);
        if (productCode.equals("8601")) {
            p.setDescription("86 (the band) - True Life Songs and Pictures");
            p.setPrice(150000);
        } else if (productCode.equals("pf01")) {
            p.setDescription("Paddlefoot - The first CD");
            p.setPrice(130000);
        } else if (productCode.equals("pf02")) {
            p.setDescription("Paddlefoot - The second CD");
            p.setPrice(150000);
        } else if (productCode.equals("jr01")) {
            p.setDescription("Joe Rut - Genuine Wood Grained Finish");
            p.setPrice(150000);
        } else {
            return null;
        }
        return p;
    }

    // Lấy toàn bộ danh sách sản phẩm để hiển thị ở trang Index (CD list)
    public static List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        products.add(getProduct("8601"));
        products.add(getProduct("pf01"));
        products.add(getProduct("pf02"));
        products.add(getProduct("jr01"));
        return products;
    }
}
