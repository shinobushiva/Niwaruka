package niwaruka.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import niwaruka.controller.niwaruka.login.LoginController;

public class UserAcconutFilter implements Filter {

    public void init(FilterConfig arg0) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

        try {
            String target = ((HttpServletRequest) request).getRequestURI();
            HttpSession session = ((HttpServletRequest) request).getSession();

            // System.out.println("dofilter : " + target);

            if (session == null) {
                /* まだ認証されていない */
                session = ((HttpServletRequest) request).getSession(true);
                session.setAttribute("target", target);

                ((HttpServletResponse) response).sendRedirect("/niwaruka");
                return;
            } else {
                Object loginCheck =
                    session.getAttribute(LoginController.USER_ID_SESSION_KEY);
                // System.out.println(loginCheck);
                if (loginCheck == null) {
                    /* まだ認証されていない */
                    session.setAttribute("target", target);
                    ((HttpServletResponse) response).sendRedirect("/niwaruka");
                    return;
                }
            }

            filterChain.doFilter(request, response);
        } catch (ServletException se) {
        } catch (IOException e) {
        }
    }

    public void destroy() {
    }

}
