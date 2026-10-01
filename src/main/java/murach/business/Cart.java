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

    public void removeItem(String productCode) {
        for (int i = 0; i < items.size(); i++) {
            LineItem item = items.get(i);
            if (item.getProduct().getCode().equals(productCode)) {
                items.remove(i);
                return;
            }
        }
    }

    public String getTotalCurrencyFormat() {
        return java.text.NumberFormat
                .getCurrencyInstance(new java.util.Locale("vi", "VN"))
                .format(getTotal());
    }

    public double getTotal() {
        double total = 0.0;
        for (LineItem item : items) {
            total += item.getTotal();
        }
        return total;
    }
}
