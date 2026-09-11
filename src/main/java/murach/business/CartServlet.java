package murach.business;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private static final String VIEWS = "/";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        String url;

        switch (action) {

            case "add": {
                // Bấm "Add To Cart" ở trang Index -> luôn thêm với quantity = 1
                String productCode = request.getParameter("productCode");
                Product product = ProductDB.getProduct(productCode);
                if (product != null) {
                    LineItem lineItem = new LineItem();
                    lineItem.setProduct(product);
                    lineItem.setQuantity(1);
                    cart.addItem(lineItem); // cộng dồn nếu đã có trong giỏ
                }
                url = VIEWS + "cart.jsp";
                break;
            }

            case "update": {
                // Bấm "Update" ở trang Cart -> gán số lượng mới do người dùng nhập
                String productCode = request.getParameter("productCode");
                String quantityString = request.getParameter("quantity");
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityString.trim());
                } catch (Exception e) {
                    quantity = 1;
                }
                if (quantity < 0) {
                    quantity = 0;
                }
                cart.updateItem(productCode, quantity);
                url = VIEWS + "cart.jsp";
                break;
            }

            case "remove": {
                // Bấm "Remove Item" ở trang Cart -> xóa sản phẩm khỏi giỏ
                String productCode = request.getParameter("productCode");
                cart.removeItem(productCode);
                url = VIEWS + "cart.jsp";
                break;
            }

            case "checkout": {
                url = VIEWS + "checkout.jsp";
                break;
            }

            case "cart": {
                // Chỉ xem lại trang giỏ hàng, không thay đổi gì
                url = VIEWS + "cart.jsp";
                break;
            }

            case "shop":
            default: {
                // Bấm "Continue Shopping", hoặc truy cập lại trang danh sách sản phẩm
                request.setAttribute("products", ProductDB.getProducts());
                url = VIEWS + "index.jsp";
                break;
            }
        }

        getServletContext().getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
