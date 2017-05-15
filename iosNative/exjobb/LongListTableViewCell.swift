//
//  LongListTableViewCell.swift
//  exjobb
//
//  Created by Exjobb on 2017-03-28.
//  Copyright © 2017 Exjobb. All rights reserved.
//

import UIKit

class LongListTableViewCell: UITableViewCell {

    @IBOutlet weak var firstNameLabel: UILabel!
    @IBOutlet weak var lastNameLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    @IBAction func EditButtonPressed(_ sender: UIButton) {
        //  Set dafult dummy values
        firstNameLabel.text = "New First Name";
        lastNameLabel.text = "New Last Name";
    }
}
