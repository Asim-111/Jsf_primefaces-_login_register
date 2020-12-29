package managedbean;


import backingbean.User;
import db.DbConnection;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
public class UserBean {
    private User user;
    private DbConnection dbConnection;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserBean() {
        user = new User();
        dbConnection = new DbConnection();
    }

    public void registerUser() {

        addMessage("Data Submitted To Database!!");

        System.out.println(user.getEmail());
        String username = user.getUsername();
        String firstname = user.getFirstname();
        String lastname = user.getLastname();
        String email = user.getEmail();
        String password = user.getPassword();
        String confirmpassword = user.getConfirmpassword();
        String dob = user.getDob();
        String gender = user.getGender();
        dbConnection.insertRecord(username, firstname, lastname, email, password, confirmpassword, dob, gender);

    }

    public String loginUser() {
        dbConnection.loginUser(user.getEmail(), user.getPassword());
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.setAttribute("email", user.getEmail());
        session.setMaxInactiveInterval(15 * 60);
        addMessage("Login Sucessfull!!");
        return "welcome.xhtml?faces-redirect=true";
    }
    public String logoutUser(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        return "login.xhtml?faces-redirect=true";
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public List<User> getUsers() {

        List<User> abc = new ArrayList<User>();

        ResultSet rs = dbConnection.getRecords();

        try {

            while (rs.next()) {

                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setDob(rs.getString("dob"));
                user.setGender(rs.getString("gender"));

                abc.add(user);

            }

        } catch (Exception e) {
        }

        return abc;
    }


}
