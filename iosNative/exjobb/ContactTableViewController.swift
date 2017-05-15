//
//  ContactTableViewController.swift
//  exjobb
//
//  Created by Exjobb on 2017-03-27.
//  Copyright © 2017 Exjobb. All rights reserved.
//

import UIKit;
import Contacts;
import ContactsUI;

class ContactTableViewController: UITableViewController, CNContactPickerDelegate {
    
    //  Contact
    let contactStore = CNContactStore();
    let contactPicker = CNContactPickerViewController();
    
    var contacts: [CNContact] = [];
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //  Check contact permission and read contacts
        requestContacts();
        
        contactPicker.delegate = self;
        presentContactPicker();
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - Table view data source
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1;
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return contacts.count;
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ContactCell", for: indexPath)
        
        // Configure the cell...
        let contact = contacts[indexPath.row];
        let formatter = CNContactFormatter();
        cell.textLabel?.text = formatter.string(from: contact);
        return cell
    }
    
    
    
    //  MARK: Private methods
    /*  ------------ CONTACTS ------------ */
    private func requestContacts(){
        let status = CNContactStore.authorizationStatus(for: .contacts);
        switch(status){
        case .notDetermined:
            //  Request permission
            contactStore.requestAccess(for: .contacts, completionHandler: { (authorized:Bool, error: Error?) in
                if(authorized){
                    self.readContacts();
                }
            });
            break;
            
        case .denied:
            alertContactsNotAvailable();
            break;
        case .restricted:
            alertContactsNotAvailable();
            break;
        case .authorized:
            readContacts();
            break;
        default:
            break;
        }
    }
    
    private func alertContactsNotAvailable(){
        let alert = UIAlertController(title: "Contacts not allowed", message: "Error getting access to contacts", preferredStyle: .alert);
        
        let okButton = UIAlertAction(title: "OK", style: .default, handler: nil);
        
        alert.addAction(okButton);
        
        present(alert, animated: true, completion: nil);
    }
    
    private func addExistingContacts(){
        
    }
    
    private func readContacts() {
        
        //  These keys are the only things iphone will let us access. Add more here for more information
        let keysToFetch = [CNContactFormatter.descriptorForRequiredKeys(for: .fullName)];
        
        
        do {
            let groups = try contactStore.groups(matching: nil);
            //let defaultContainer = try contactStore.containers(matching: nil);
            if(groups.count > 0){
                let predicate = try CNContact.predicateForContactsInGroup(withIdentifier: groups[0].identifier);
                contacts = try contactStore.unifiedContacts(matching: predicate, keysToFetch: keysToFetch);
                reload();
            }
        } catch {
            alertContactsNotAvailable();
        }
        
    }
    
    private func reload(){
        tableView.reloadData();
    }
    
    private func presentContactPicker(){
        self.present(contactPicker, animated: true, completion: nil);
    }
    /*  ------------ /CONTACTS ------------ */
    
    
    /*   ------------ CNContactDelegate ------------ */
    func contactPicker(_ picker: CNContactPickerViewController, didSelect contact: CNContact) {
        contacts.append(contact);
        reload();
    }
    
    
    //  MARK: Actions
    @IBAction func contactsButtonPressed(_ sender: UIBarButtonItem) {
        presentContactPicker();
    }
    
    
    /*
     // Override to support conditional editing of the table view.
     override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
     // Return false if you do not want the specified item to be editable.
     return true
     }
     */
    
    /*
     // Override to support editing the table view.
     override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
     if editingStyle == .delete {
     // Delete the row from the data source
     tableView.deleteRows(at: [indexPath], with: .fade)
     } else if editingStyle == .insert {
     // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
     }
     }
     */
    
    /*
     // Override to support rearranging the table view.
     override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {
     
     }
     */
    
    /*
     // Override to support conditional rearranging of the table view.
     override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
     // Return false if you do not want the item to be re-orderable.
     return true
     }
     */
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
}
