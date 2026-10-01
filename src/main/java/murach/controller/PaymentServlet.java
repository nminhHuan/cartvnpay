package murach.controller;

import murach.business.Cart;
import murach.util.VnpayConfig;
import murach.util.VnpayUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** Tạo giao dịch và chuyển hướng người dùng sang cổng thanh toán VNPay. */
@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getCount() == 0) {
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }
        if (!VnpayConfig.isConfigured()) {
            request.setAttribute("error",
                    "Chưa cấu hình VNPAY_TMN_CODE / VNPAY_HASH_SECRET trên server.");
            getServletContext().getRequestDispatcher("/checkout.jsp").forward(request, response);
            return;
        }

        // VNPay yêu cầu số tiền nhân 100 (không có phần thập phân)
        long amount = Math.round(cart.getTotal()) * 100L;
        String txnRef = generateTxnRef();

        // Lưu lại để đối chiếu khi VNPay trả về
        session.setAttribute("vnp_TxnRef", txnRef);
        session.setAttribute("vnp_Amount", amount);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        String createDate = fmt.format(cal.getTime());
        cal.add(Calendar.MINUTE, 15);
        String expireDate = fmt.format(cal.getTime());

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", VnpayConfig.VNP_VERSION);
        params.put("vnp_Command", VnpayConfig.VNP_COMMAND);
        params.put("vnp_TmnCode", VnpayConfig.getTmnCode());
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", "Thanh toan don hang " + txnRef);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", VnpayConfig.getReturnUrl(request));
        params.put("vnp_IpAddr", VnpayUtil.getClientIp(request));
        params.put("vnp_CreateDate", createDate);
        params.put("vnp_ExpireDate", expireDate);

        String bankCode = request.getParameter("bankCode");
        if (bankCode != null && !bankCode.trim().isEmpty()) {
            params.put("vnp_BankCode", bankCode.trim());
        }

        response.sendRedirect(VnpayUtil.buildPaymentUrl(params));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/index");
    }

    private String generateTxnRef() {
        // Mã giao dịch duy nhất trong ngày: HHmmss + 4 số ngẫu nhiên
        SimpleDateFormat fmt = new SimpleDateFormat("HHmmss");
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        return fmt.format(new java.util.Date()) + String.format("%04d", new Random().nextInt(10000));
    }
}
