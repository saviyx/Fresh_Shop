/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Product;
import entity.Weight;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import java.util.List;
import model.HibernateUtil;

/**
 *
 * @author kavis
 */
@WebServlet(name = "ShopData", urlPatterns = {"/ShopData"})
public class ShopData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();


        Criteria criteria = session.createCriteria(Category.class);
        criteria.addOrder(Order.asc("name"));
        List<Category> categoryList = criteria.list();

       


        
        
        Criteria criteria3 = session.createCriteria(Product.class);
        criteria3.addOrder(Order.asc("id"));
        List<Product> productList = criteria3.list();

        JsonObject jsonobject = new JsonObject();
       
        jsonobject.add("categoryList", gson.toJsonTree(categoryList));
 
        jsonobject.add("productList", gson.toJsonTree(productList));

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(jsonobject));
        session.close();

    }

}