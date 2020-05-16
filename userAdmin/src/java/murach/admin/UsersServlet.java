package murach.admin;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import murach.business.User;
import murach.data.UserDB;

public class UsersServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();

        String url = "/index.jsp";
        
        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "display_users";  // default action
        }
        
        // perform action and set URL to appropriate page
        if (action.equals("display_users")) {            
            // get list of users
            List<User> users = UserDB.selectUsers();
            // set list as a request attribute
            request.setAttribute("users", users);
            // forward to index.jsp
        } 
        else if (action.equals("display_user")) {
            url = "/user.jsp";
            // get specified email
            String email = request.getParameter("email");
            // get user for email
            User user = UserDB.selectUser(email);
            // set as session attribute
            session.setAttribute("user", user);
            // forward to user.jsp
        }
        else if (action.equals("update_user")) {
            // get user from session
            User user = (User) session.getAttribute("user");
            // get new data from request
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            // update user
            user.setFirstName(firstName);
            user.setLastName(lastName);
            // update user in database
            UserDB.update(user);
            // get current list of users
            List<User> users = UserDB.selectUsers();
            // set as request attribute
            request.setAttribute("users", users);
            // forward to index.jsp
        }
        else if (action.equals("delete_user")) {
            // get the user for the specified email
            String email = request.getParameter("email");
            User user = UserDB.selectUser(email);
            // delete the user
            UserDB.delete(user);
            // get current list of users
            List<User> users = UserDB.selectUsers();
            // set as request attribute
            request.setAttribute("users", users);
            // forward to index.jsp
        }
        
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }    
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }    
}