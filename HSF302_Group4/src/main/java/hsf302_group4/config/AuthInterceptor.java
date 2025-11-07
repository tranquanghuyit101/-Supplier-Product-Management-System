package hsf302_group4.config;

import hsf302_group4.entity.UserAccount;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest req,
                             @NonNull HttpServletResponse res,
                             @NonNull Object handler) throws Exception {
        String uri = req.getRequestURI();
        if (
                uri.equals("/") ||
                        uri.equals("/login") ||
                        uri.equals("/login.css") ||
                        uri.equals("/product.css") ||
                        uri.equals("/suppliers.css") ||
                        uri.startsWith("/js") ||
                        uri.startsWith("/images") ||
                        uri.startsWith("/webjars") ||
                        uri.endsWith(".css") ||
                        uri.endsWith(".js")   ||
                        uri.endsWith(".png")  ||
                        uri.endsWith(".jpg")  ||
                        uri.endsWith(".jpeg") ||
                        uri.endsWith(".gif")  ||
                        uri.endsWith(".svg")
        ) {
            return true;
        }

        UserAccount user = (UserAccount) req.getSession().getAttribute("loginUser");
        if (user == null) {
            res.sendRedirect("/");
            return false;
        }
        return true;
    }
}
