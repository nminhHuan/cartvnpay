package murach.business;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable {
    private ArrayList<LineItem> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public ArrayList<LineItem> getItems() {
        return items;
    }

    public int getCount() {
        return items.size();
    }

    // Dùng cho nút "Add To Cart" ở trang Index.
    // Nếu sản phẩm đã có trong giỏ thì CỘNG DỒN số lượng,
    // nếu chưa có thì thêm mới vào giỏ.
    public void addItem(LineItem itemToAdd) {
        String code = itemToAdd.getProduct().getCode();
        for (LineItem item : items) {
            if (item.getProduct().getCode().equals(code)) {
                item.setQuantity(item.getQuantity() + itemToAdd.getQuantity());
                return;
            }
        }
        items.add(itemToAdd);
    }

    // Dùng cho nút "Update" ở trang Cart.
    // GÁN LẠI (ghi đè) số lượng đúng bằng số người dùng nhập.
    // Nếu số lượng <= 0 thì xóa luôn sản phẩm khỏi giỏ.
    public void updateItem(String productCode, int quantity) {
        for (int i = 0; i < items.size(); i++) {
            LineItem item = items.get(i);
            if (item.getProduct().getCode().equals(productCode)) {
                if (quantity <= 0) {
                    items.remove(i);
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
    }

    // Dùng cho nút "Remove Item" ở trang Cart.
    public void removeItem(String productCode) {
        for (int i = 0; i < items.size(); i++) {
            LineItem item = items.get(i);
            if (item.getProduct().getCode().equals(productCode)) {
                items.remove(i);
                return;
            }
        }
    }

    public double getTotal() {
        double total = 0.0;
        for (LineItem item : items) {
            total += item.getTotal();
        }
        return total;
    }
}
