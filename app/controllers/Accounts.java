package controllers;

import static play.data.Form.form;

import javax.persistence.PersistenceException;

import models.Account;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.accountFormList;
import views.html.accountFormCreate;
import views.html.accountFormEdit;

import java.math.BigDecimal;

public class Accounts extends Controller {
	
	/**
	 * GET		/accounts					controllers.Accounts.list(p:Int ?= 0, s ?= "owner", o ?= "asc", f ?= "")
	 * @param page
	 * @param sortBy
	 * @param order
	 * @param filter
	 * @return
	 */
	public static Result list(int page, String sortBy, String order, String filter){
	
        return ok(
                accountFormList.render(  //requires import views.html.*;
                    Account.page(page, 10, sortBy, order, filter),
                    sortBy, order, filter
                )
            );
	}

	/**
	 * GET     /account              	controllers.Accounts.create()
	 * Display the AccountCreationForm
	 * @return
	 */
	public static Result create(){
		Form<Account> accountForm = form(Account.class);
		return ok(
				accountFormCreate.render(accountForm)
		);
	}
	
	/**
	 * POST    /account                  	controllers.Accounts.save()
	 * @return
	 */
	public static Result save(){
		Form<Account> accountForm = form(Account.class).bindFromRequest();
		if(accountForm.hasErrors()){
			return badRequest(accountFormCreate.render(accountForm));
		}
//        if (accountForm.get().balance == null)
//            accountForm.get().balance = new BigDecimal(0.00);

        accountForm.get().save();
		flash("success", "Account " + accountForm.get().owner + " has been created");
		return Application.GO_ACCOUNTS;
	}

//	# Edit account
	/**
	 * GET     /account/:id              	controllers.Accounts.edit(id:Long)
	 * @param id
	 * @return
	 */
	public static Result edit(Long id){
		Form<Account> accountForm = form(Account.class).fill(
				Account.find.byId(id));
		return ok(
				accountFormEdit.render(id, accountForm)
		);
	}
	
	/**
	 * POST    /account/:id              	controllers.Accounts.update(id:Long)
	 * @param id
	 * @return
	 */
	public static Result update(Long id){
		Form<Account> accountForm = form(Account.class).bindFromRequest();
		if(accountForm.hasErrors()){
			return badRequest(accountFormEdit.render(id, accountForm));
		}
//        if (accountForm.get().balance == null)
//            accountForm.get().balance = new BigDecimal(0.00);
        accountForm.get().update(id);
		flash("success", "Account " + accountForm.get().owner + " has been changed");
		return Application.GO_ACCOUNTS;
	}

	/**
	 * DELETE    /account/:id       	controllers.Accounts.delete(id:Long)
	 * @param id
	 * @return
	 */
	public static Result delete(Long id){

		System.out.println("blabla");

		try{
			Account.find.byId(id).delete();
			flash("success", "Account has been deleted");
		} 
		catch (PersistenceException pe){
			flash("error", "Account " + Account.find.ref(id).owner + " cannot be deleted. Found referencing transactions.");
		}

		return Application.GO_ACCOUNTS;
	}

}
