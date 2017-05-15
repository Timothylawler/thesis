import { Component, OnInit, ElementRef, ViewChild } from "@angular/core";
import {isAndroid, isIOS} from "platform";


const contactProvider = require("../../native-modules/contact-provider/");
import Contact from "../../models/contact-model";



@Component({
  selector: "my-app",
  templateUrl: "pages/contacts/contacts.html",
  styleUrls: ["pages/contacts/contacts-common.css"]
})
export default class ContactsPage implements OnInit{

	contactsList: Array<Contact> = [];

	constructor(){
		
	}

	ngOnInit(){
		if(isAndroid){
			contactProvider.getAllContacts().then((result) => {
				this.contactsList = result.contacts;
			}, (error) => {
				console.log(error);
				alert("Error fetching contacts: " +  error);
			});
		}
		if(isIOS){
			contactProvider.displayContactPicker().then((result)=>{
				this.contactsList.push(new Contact(result.data));
			}, (error) => {
				console.log(error);
				alert("Error with contacts: " + error);
			})
		}
	}


}