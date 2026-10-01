package murach.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

public class VnpayUtil {

    public static String hmacSHA512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot compute HMAC SHA512", e);
        }
    }

    /** Sắp xếp tham số theo tên, encode giá trị -> chuỗi "k1=v1&k2=v2" dùng để ký. */
    public static String buildHashData(Map<String, String> params) {
        List<String> names = new ArrayList<>(params.keySet());
        Collections.sort(names);
        StringBuilder sb = new StringBuilder();
        for (String name : names) {
            String value = params.get(name);
            if (value == null || value.isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append('&');
            }
            sb.append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
        }
        return sb.toString();
    }

    /** Tạo URL thanh toán đầy đủ kèm vnp_SecureHash. */
    public static String buildPaymentUrl(Map<String, String> params) {
        String hashData = buildHashData(params);
        String secureHash = hmacSHA512(VnpayConfig.getHashSecret(), hashData);
        return VnpayConfig.getPayUrl() + "?" + hashData + "&vnp_SecureHash=" + secureHash;
    }

    /** Xác thực chữ ký của dữ liệu VNPay gửi về (Return URL hoặc IPN). */
    public static boolean verifySignature(HttpServletRequest request) {
        Map<String, String> fields = new java.util.HashMap<>();
        for (String name : Collections.list(request.getParameterNames())) {
            if (name.startsWith("vnp_")
                    && !name.equals("vnp_SecureHash")
                    && !name.equals("vnp_SecureHashType")) {
                fields.put(name, request.getParameter(name));
            }
        }
        String received = request.getParameter("vnp_SecureHash");
        if (received == null) {
            return false;
        }
        String calculated = hmacSHA512(VnpayConfig.getHashSecret(), buildHashData(fields));
        return calculated.equalsIgnoreCase(received);
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            return ip.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
