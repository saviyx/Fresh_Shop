/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author savindu umantha
 */
@WebServlet(name = "RemoveItems", urlPatterns = {"/RemoveItems"})
public class RemoveItems extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();

        Response_DTO response_DTO = new Response_DTO();

        String id = req.getParameter("id");

        Session session = HibernateUtil.getSessionFactory().openSession();

        if (id.isEmpty()) {
            response_DTO.setContent("Cart Id required");
        } else {
            if (req.getSession().getAttribute("user") != null) {
                

                User_DTO user_DTO = (User_DTO) req.getSession().getAttribute("user");
                
                Criteria criteria = session.createCriteria(User.class);
                criteria.add(Restrictions.eq("email", user_DTO.getEmail()));
                
                User user = (User) criteria.uniqueResult();
                Product product = (Product) session.get(Product.class,  Integer.parseInt(id));

                Criteria criteria1 = session.createCriteria(Cart.class);
                criteria1.add(Restrictions.eq("product", product));
                criteria1.add(Restrictions.eq("user", user));
                

                if (criteria1.list().isEmpty()) {
                    response_DTO.setContent("Cart Item not found");
                } else {
                    Cart cart = (Cart) criteria1.uniqueResult();
                    session.delete(cart);
                    session.beginTransaction().commit();
                    response_DTO.setSuccess(true);
                }
            } else {
                HttpSession httpSession = req.getSession();

                if (httpSession.getAttribute("sessionCart") != null) {

                    ArrayList<Cart_DTO> cartarray = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");

                    Cart_DTO foundcart = null;

                    Product product = (Product) session.get(Product.class, Integer.parseInt(id));

                    for (Cart_DTO cart_DTO : cartarray) {

                        if (cart_DTO.getProduct().getId() == product.getId()) {

                            foundcart = cart_DTO;
                            break;

                        }

                    }

                    if (foundcart != null) {

                        cartarray.remove(foundcart);

                        response_DTO.setSuccess(true);
                        response_DTO.setContent("Successful.");

                    } else {
                        response_DTO.setContent("Product Not Found.");
                    }

                } else {
                    response_DTO.setContent("Cart Not Found.");
                }

            }
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(response_DTO));
    }

}
