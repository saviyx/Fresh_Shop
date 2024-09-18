/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Product;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author User
 */
@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest requsest, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();
       
        try {
            String productID = requsest.getParameter("id");

            if (Validations.isInteger(productID)) {
                Product product = (Product) session.get(Product.class, Integer.parseInt(productID));
                product.getUser().setPassword(null);
                product.getUser().setVerification(null);
                product.getUser().setEmail(null);



                JsonObject jsonObject = new JsonObject();
                jsonObject.add("product", gson.toJsonTree(product));


                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(jsonObject));
            }else{
            
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
