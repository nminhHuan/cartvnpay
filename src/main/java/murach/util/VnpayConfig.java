package murach.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Cấu hình VNPay. Đọc từ biến môi trường để không lộ secret trong source code.
 *   VNPAY_TMN_CODE     : Terminal ID (Merchant code) do VNPay cấp
 *   VNPAY_HASH_SECRET  : Secret key do VNPay cấp
 *   VNPAY_URL          : (tuỳ chọn) mặc định là sandbox
 *   VNPAY_RETURN_URL   : (tuỳ chọn) mặc định tự suy ra từ request, dạng https://host/vnpay-return
 */
public class VnpayConfig {

    public static final String VNP_VERSION = "2.1.0";
    public static final String VNP_COMMAND = "pay";

    // Thông tin SANDBOX (chỉ để test). Biến môi trường VNPAY_* sẽ ghi đè các giá trị này.
    // Khi chạy thật hãy đặt key thật qua biến môi trường, không ghi vào source.
    private static final String SANDBOX_TMN_CODE = "J6M6VJDD";
    private static final String SANDBOX_HASH_SECRET = "ZZRDZYYNHQUGMANJBWSYDHHPMIRSOYKG";

    public static String getTmnCode() {
        return env("VNPAY_TMN_CODE", SANDBOX_TMN_CODE);
    }

    public static String getHashSecret() {
        return env("VNPAY_HASH_SECRET", SANDBOX_HASH_SECRET);
    }

    public static String getPayUrl() {
        return env("VNPAY_URL", "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");
    }

    public static boolean isConfigured() {
        return !getTmnCode().isEmpty() && !getHashSecret().isEmpty();
    }

    /** Render chạy sau reverse proxy nên phải ưu tiên X-Forwarded-Proto / Host. */
    public static String getReturnUrl(HttpServletRequest request) {
        String fixed = System.getenv("VNPAY_RETURN_URL");
        if (fixed != null && !fixed.trim().isEmpty()) {
            return fixed.trim();
        }
        String proto = request.getHeader("X-Forwarded-Proto");
        if (proto == null || proto.isEmpty()) {
            proto = request.getScheme();
        }
        String host = request.getHeader("X-Forwarded-Host");
        if (host == null || host.isEmpty()) {
            host = request.getHeader("Host");
        }
        if (host == null || host.isEmpty()) {
            host = request.getServerName() + ":" + request.getServerPort();
        }
        return proto + "://" + host + request.getContextPath() + "/vnpay-return";
    }

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.trim().isEmpty()) ? def : v.trim();
    }
}
