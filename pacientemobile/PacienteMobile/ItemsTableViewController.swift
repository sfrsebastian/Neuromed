//
//  ItemsTableViewController.swift
//  PacienteMobile
//
//  Created by Mario Hernandez on 16/05/15.
//  Copyright (c) 2015 Equinox. All rights reserved.
//

import UIKit

class ItemsTableViewController: UITableViewController , UISearchBarDelegate , UISearchDisplayDelegate {
    
    
    var tipo = "medicamento"
    
    //var items = ["Mario","Sebas","Juan","Ozzy"]
    
    var items : [NSDictionary] = []
    
    
    var filteredItems = []
    
    
    
    
    
    override func viewDidLoad() {
        let con = Connector()
        items = con.doGet("/\(tipo)") as! [NSDictionary]
        
        println("Y ahora los items \n \(items)")
        self.tableView.reloadData()
        
        
    }
    
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if(tableView == self.searchDisplayController?.searchResultsTableView){
            return filteredItems.count
        }
        else {
            return items.count
        }
    }
    
    
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        let cell = self.tableView.dequeueReusableCellWithIdentifier("celly") as! UITableViewCell
        
        var item : String
        
        if(tableView == self.searchDisplayController?.searchResultsTableView){
            item = filteredItems[indexPath.row]["titulo"] as! String
        }
        else{
            item = items[indexPath.row]["titulo"] as! String
        }
        
        cell.textLabel?.text = item
        
        //cell.accessoryType = UITableViewCellAccessoryType.Checkmark
        
        
        
        return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        
        var item : String
        var itemId = 0
        
        if(tableView == self.searchDisplayController?.searchResultsTableView){
            item = filteredItems[indexPath.row]["titulo"] as! String
            itemId = filteredItems[indexPath.row]["id"] as! Int
            //            self.searchDisplayController?.searchResultsTableView.cellForRowAtIndexPath(indexPath)?.accessoryType = UITableViewCellAccessoryType.Checkmark
            
            
            
        }
        else {
            item = items[indexPath.row]["titulo"] as! String
            itemId = items[indexPath.row]["id"] as! Int
        }
        
        var ya = false
        
        if(tipo == "medicamento")
        {
            for var i = 0; i < ViewController.MyVariables.medicamentos.count && !ya ; i++
            {
                var num =  ViewController.MyVariables.medicamentos[i] as! Int
                if (num == itemId) {
                    ViewController.MyVariables.medicamentos.removeObjectAtIndex(i)
                    
                    ya = true
                }
            }
        } else if(tipo == "causa")
        {
            
            for var i = 0; i < ViewController.MyVariables.causas.count && !ya ; i++
            {
                var num =  ViewController.MyVariables.causas[i] as! Int
                if (num == itemId) {
                    ViewController.MyVariables.causas.removeObjectAtIndex(i)
                    
                    ya = true
                }
            }
            
            
        } else if(tipo == "intervalo")
        {
            
            for var i = 0; i < ViewController.MyVariables.patrones.count && !ya ; i++
            {
                var num =  ViewController.MyVariables.patrones[i] as! Int
                if (num == itemId) {
                    ViewController.MyVariables.patrones.removeObjectAtIndex(i)
                    
                    ya = true
                }
            }
            
            
        }
        
        var msj = "Agregado"
        if(ya){
            msj = "Eliminado"
        }
        else{
            if(tipo == "medicamento"){
                ViewController.MyVariables.medicamentos.addObject(itemId)
            }
            else if(tipo == "causa"){
                ViewController.MyVariables.causas.addObject(itemId)
            }
            else if(tipo == "intervalo"){
                ViewController.MyVariables.patrones.addObject(itemId)
            }
        }
        
        // Mostrar alerta
        var alert = UIAlertController(title: msj, message: item , preferredStyle: UIAlertControllerStyle.Alert)
        self.presentViewController(alert, animated: true, completion: nil)
        
        // quitar alerta
        let delay = 0.7 * Double(NSEC_PER_SEC)
        var time = dispatch_time(DISPATCH_TIME_NOW, Int64(delay))
        dispatch_after(time, dispatch_get_main_queue(), {
            alert.dismissViewControllerAnimated(true, completion: {})
        })
        
        
        //                   tableView.cellForRowAtIndexPath(indexPath)?.accessoryType = UITableViewCellAccessoryType.Checkmark
        
        
        
    }
    
    func filterContentForSearchText(searchText : String , scope : String = "Title"){
        filteredItems = items.filter({(item : NSDictionary ) -> Bool in
            
            var categoryMatch = (scope == "Title")
            var stringMatch = (item["titulo"] as! String).rangeOfString(searchText)
            
            return categoryMatch && ( stringMatch != nil)
            
        })
        
    }
    
    func searchDisplayController(controller: UISearchDisplayController, shouldReloadTableForSearchString searchString: String!) -> Bool {
        
        self.filterContentForSearchText(searchString, scope: "Title")
        
        return true
        
    }
    
    func searchDisplayController(controller: UISearchDisplayController, shouldReloadTableForSearchScope searchOption: Int) -> Bool {
        
        self.filterContentForSearchText(self.searchDisplayController!.searchBar.text, scope: "Title")
        
        return true
        
    }
    
    
    
}