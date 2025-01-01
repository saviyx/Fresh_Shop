/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Response_DTO;
import dto.User_DTO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Mail;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author savindu umantha
 */
@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
       Response_DTO response_DTO = new Response_DTO();
       
       Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
       
       User_DTO user_DTO = gson.fromJson(request.getReader(), User_DTO.class);
       
       if(user_DTO.getFirst_name().isEmpty()){
           response_DTO.setContent("Please enter your first name");
       }else if(user_DTO.getLast_name().isEmpty()){
           response_DTO.setContent("Please enter your last name");
       }else if(user_DTO.getEmail().isEmpty()){
           response_DTO.setContent("Please enter your email");
       }else if(!Validations.isEmailValid(user_DTO.getEmail())){
           response_DTO.setContent("Please enter valid email");
       }else if(user_DTO.getPassword().isEmpty()){
           response_DTO.setContent("Please enter your password");
       }else if(!Validations.isPasswordValid(user_DTO.getPassword())){
           response_DTO.setContent("Password must include atleast one uppercase letter, a number, a special character and be 8 characters long");
       }else{
       
           Session session = HibernateUtil.getSessionFactory().openSession();
           Criteria criteria1 = session.createCriteria(User.class);
           criteria1.add(Restrictions.eq("email",user_DTO.getEmail()));
           
           if(!criteria1.list().isEmpty()){
               //if user already registereed
               response_DTO.setContent("This email is already used!");
           }else{
               //generate verificationcode
               int code = (int)(Math.random() * 1000000);
               
               //save user
               final User user = new User();
               user.setEmail(user_DTO.getEmail());
               user.setFirst_name(user_DTO.getFirst_name());
               user.setLast_name(user_DTO.getLast_name());
               user.setPassword(user_DTO.getPassword());
               user.setVerification(String.valueOf(code));
               
               //send verification code
               Thread sendMailThread = new Thread(new Runnable() {
                   @Override
                   public void run() {
                      Mail.sendMail(
                            user_DTO.getEmail(),
                            "Fresh Shop Verification",
                            "<h1 style=\\\"color:#6482AD;\\\">Your verification code "+ code +"</h1>"
                      );
                   }
               });
               sendMailThread.start();
               
               session.save(user);
               session.beginTransaction().commit();
               
               request.getSession().setAttribute("email", user_DTO.getEmail());
               response_DTO.setSuccess(true);
               response_DTO.setContent("Registration Complete. Please verify your account to proceed.");
           }
       
           session.close();
           
       }
        
       response.setContentType("application/json");
       response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));
    }

   
}
