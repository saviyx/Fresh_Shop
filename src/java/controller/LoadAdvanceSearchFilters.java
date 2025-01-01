
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
@WebServlet(name = "LoadAdvanceSearchFilters", urlPatterns = {"/LoadAdvanceSearchFilters"})
public class LoadAdvanceSearchFilters extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Gson gson = new Gson();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Criteria criteria = session.createCriteria(Category.class);
        criteria.addOrder(Order.asc("name"));
        List<Category> categoryList = criteria.list();
        
        Product_Status product_Status = (Product_Status) session.get(Product_Status.class,1);
        
        Criteria criteria2 = session.createCriteria(Product.class);
        criteria2.add(Restrictions.eq("product_status", product_Status));
        criteria2.addOrder(Order.asc("id"));
        List<Product> productList = criteria2.list();
        
        for(Product product : productList){
            product.setUser(null);
        }
        
        JsonObject jsonobject = new JsonObject();
        jsonobject.add("categoryList", gson.toJsonTree(categoryList));
        jsonobject.add("productList", gson.toJsonTree(productList));
        
        int productCount = productList.size();
        jsonobject.addProperty("productCount", productCount);
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonobject));
        session.close();
    }

    
}
