//
//  ItemsTableViewController.swift
//  PacienteMobile
//
//  Created by Mario Hernandez on 16/05/15.
//  Copyright (c) 2015 Equinox. All rights reserved.
//

import UIKit

class ItemsTableViewController: UITableViewController , UISearchBarDelegate , UISearchDisplayDelegate {
    
    
    var items = ["Mario","Sebas","Juan","Ozzy"]
    
    var filteredItems = []

    

    
    
    override func viewDidLoad() {
        
        self.tableView.reloadData()
    }
    
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
         return 0
    }
    
    


}