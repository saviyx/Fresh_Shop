/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Product;
import entity.Product_Status;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author savindu umantha
 */
@WebServlet(name = "AdvanceSearch", urlPatterns = {"/AdvanceSearch"})
public class AdvancedSearch extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
        Gson gson = new Gson();
        
        JsonObject responseJsonObject= new JsonObject();
        responseJsonObject.addProperty("success", false);
        
        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        int categoryId = requestJsonObject.get("categoryId").getAsInt();
        double startPrice = requestJsonObject.get("startPrice").getAsDouble();
        double endPrice = requestJsonObject.get("endValue").getAsDouble();
        String sortBy = requestJsonObject.get("sortBy").getAsString();
        
        System.out.println(categoryId);
        System.out.println(startPrice);
        System.out.println(endPrice);
        System.out.println(sortBy);
        
        Criteria criteria1 = session.createCriteria(Product.class);
        
        Product_Status product_Status = (Product_Status) session.get(Product_Status.class, 1);
        criteria1.add(Restrictions.eq("product_status", product_Status));
        
        if(categoryId != 0){
            Category category = (Category) session.get(Category.class, categoryId);
            criteria1.add(Restrictions.eq("category", category));
        }
        
        criteria1.add(Restrictions.ge("price", startPrice));
        criteria1.add(Restrictions.le("price", endPrice));
        
        if(sortBy.equals("newToOld")){
            criteria1.addOrder(Order.desc("id"));
        }else if(sortBy.equals("oldToNew")){
            criteria1.addOrder(Order.asc("id"));
        }else if (sortBy.equals("lowToHigh")){
            criteria1.addOrder(Order.asc("price"));
        }else if (sortBy.equals("highToLow")){
             criteria1.addOrder(Order.desc("price"));
        }
        
        //get product list
        List<Product> productList = criteria1.list();
        
        if(productList.isEmpty()){
            responseJsonObject.addProperty("message", "noProducts");
        }else{
             responseJsonObject.addProperty("message", "successful");
             for(Product product : productList){
                 product.setUser(null);
             }
             
             int productCount = productList.size();
             
             responseJsonObject.addProperty("productCount", productCount);
             responseJsonObject.add("productList", gson.toJsonTree(productList));
        }
        
        responseJsonObject.addProperty("success", true);
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
        
   
    }

    
  
}
