package edu.school21;

import edu.school21.annotation.HtmlForm;
import edu.school21.annotation.HtmlInput;

@HtmlForm(fileName = "user_form.html", action = "/users", method = "post")
public class HtmlProcessor {
  @HtmlInput(type = "text", name = "first_name", placeholder = "Enter First Name")
  String firstName;
  @HtmlInput(type = "text", name = "last_name", placeholder = "Enter Last Name")
  String lastName;
  @HtmlInput(type = "password", name = "password", placeholder = "Enter Password")
  String password;
}
