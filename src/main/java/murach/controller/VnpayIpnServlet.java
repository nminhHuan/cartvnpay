package murach.controller;

import murach.util.VnpayConfig;
import murach.util.VnpayUtil;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * IPN: VNPay gọi server-to-server để thông báo kết quả (đáng tin cậy hơn Return URL).
 * Cần khai báo URL này (https://<domain>/vnpay-ipn) trong trang quản trị sandbox của VNPay.
 * Ứng dụng chưa có database nên chỉ xác thực chữ ký và phản hồi đúng định dạng;
 * khi có bảng đơn hàng, hãy cập nhật trạng thái đơn tại đây.
 */
@WebServlet("/vnpay-ipn")
public class VnpayIpnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        String body;
        if (!VnpayConfig.isConfigured() || !VnpayUtil.verifySignature(request)) {
            body = "{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}";
        } else {
            // TODO: kiểm tra vnp_TxnRef tồn tại, vnp_Amount khớp, chưa xử lý trước đó, rồi cập nhật đơn hàng.
            body = "{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}";
        }
        response.getWriter().write(body);
    }
}
