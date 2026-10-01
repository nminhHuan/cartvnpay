package murach.controller;

import murach.util.VnpayConfig;
import murach.util.VnpayUtil;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** Trang người dùng được VNPay chuyển về sau khi thanh toán. */
@WebServlet("/vnpay-return")
public class VnpayReturnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean validSignature = VnpayConfig.isConfigured() && VnpayUtil.verifySignature(request);
        String responseCode = request.getParameter("vnp_ResponseCode");
        String transactionStatus = request.getParameter("vnp_TransactionStatus");
        String txnRef = request.getParameter("vnp_TxnRef");
        String amountStr = request.getParameter("vnp_Amount");

        HttpSession session = request.getSession();
        String expectedRef = (String) session.getAttribute("vnp_TxnRef");
        Long expectedAmount = (Long) session.getAttribute("vnp_Amount");

        boolean matchesOrder = expectedRef != null && expectedRef.equals(txnRef)
                && expectedAmount != null && String.valueOf(expectedAmount).equals(amountStr);

        boolean success = validSignature && matchesOrder
                && "00".equals(responseCode) && "00".equals(transactionStatus);

        String message;
        if (!validSignature) {
            message = "Chữ ký không hợp lệ. Giao dịch bị từ chối.";
        } else if (!matchesOrder) {
            message = "Thông tin đơn hàng không khớp hoặc phiên làm việc đã hết hạn.";
        } else if (success) {
            message = "Thanh toán thành công!";
        } else if ("24".equals(responseCode)) {
            message = "Bạn đã huỷ giao dịch.";
        } else {
            message = "Thanh toán không thành công (mã lỗi: " + responseCode + ").";
        }

        if (success) {
            session.removeAttribute("cart");
            session.removeAttribute("vnp_TxnRef");
            session.removeAttribute("vnp_Amount");
        }

        long amountVnd = 0;
        try {
            amountVnd = Long.parseLong(amountStr) / 100L;
        } catch (Exception ignored) {
        }

        request.setAttribute("success", success);
        request.setAttribute("message", message);
        request.setAttribute("txnRef", txnRef);
        request.setAttribute("amount", String.format(java.util.Locale.US, "%,d", amountVnd).replace(',', '.') + " ₫");
        request.setAttribute("bankCode", request.getParameter("vnp_BankCode"));
        request.setAttribute("transactionNo", request.getParameter("vnp_TransactionNo"));
        request.setAttribute("payDate", request.getParameter("vnp_PayDate"));

        getServletContext().getRequestDispatcher("/payment-result.jsp").forward(request, response);
    }
}
