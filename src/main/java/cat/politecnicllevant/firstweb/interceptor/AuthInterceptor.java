package cat.politecnicllevant.firstweb.interceptor;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtre senzill que redirigeix cap al login si no hi ha usuari a la sessió.
 */
@WebFilter("/game")
public class AuthInterceptor implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);

        // Només deixam passar les peticions si l'usuari ja té sessió oberta
        if (session != null && session.getAttribute("user") != null) {
            filterChain.doFilter(request,response);
            return;
        }

        // En cas contrari, redirigim al formulari d'inici de sessió
        response.sendRedirect("login");
    }
}
