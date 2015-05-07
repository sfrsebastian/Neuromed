//
//  TableViewController.swift
//  DoctorApp
//
//  Created by Felipe Macbook Pro on 3/16/15.
//  Copyright (c) 2015 Felipe-Otalora. All rights reserved.
//

import UIKit

class TableViewController: UITableViewController {
    
    var doctorArray : NSArray = []
    
    var refresher : UIRefreshControl!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        refresher = UIRefreshControl()
        refresher.attributedTitle = NSAttributedString(string: "Pull to refresh")
        
        refresher.addTarget(self, action: "refreshTable:", forControlEvents: UIControlEvents.ValueChanged)
        self.tableView.addSubview(refresher)
        
        var connector : Connector = Connector()
        //        doctorArray = connector.doGet("/doctor")
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false
        
        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func refreshTable(sender : AnyObject){
        var connector : Connector = Connector()
        doctorArray = connector.doGet("/doctor")
        self.tableView.reloadData()
        refresher.endRefreshing()
    }
    
    // MARK: - Table view data source
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Potentially incomplete method implementation.
        // Return the number of sections.
        return 3
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete method implementation.
        // Return the number of rows in the section.
        if(section == 0){
            return 1
        }else if(section == 1){
            return 2
        }else{
            return doctorArray.count
        }
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        if(indexPath.section == 0){
            let cell = tableView.dequeueReusableCellWithIdentifier("doctorInfo", forIndexPath: indexPath) as! MenuTableViewCell
            
            //            cell.imageView?.image = UIImage(named: "doctor")
            let conector : Connector = Connector()
            var dict = conector.doGet("/paciente")[0] as! NSDictionary
            
            //            cell.imageView?.image = conector.getImage(dict.valueForKey("picture") as NSString)
            cell.lblDoctor?.text = "Doctor Perez"
            
            return cell
        }else if(indexPath.section == 1){
            let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath) as! UITableViewCell
            
            if(indexPath.row == 0){
                cell.textLabel?.text = "Ver Pacientes"
                cell.imageView?.image = UIImage(named: "pacientes")
            }else if(indexPath.row == 1){
                cell.textLabel?.text = "Ver Episodios"
                cell.imageView?.image = UIImage(named: "episodios")
            }
            
            return cell
        }else{
            
            let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath) as! UITableViewCell
            
            // Configure the cell...
            var dictActual = doctorArray[indexPath.row] as! NSDictionary
            cell.textLabel?.text = dictActual.valueForKey("nombre") as? String
            //        cell.detailTextLabel?.text = dictActual.valueForKey("email") as? String
            cell.imageView?.image = UIImage(named: "doctor")
            
            return cell
        }
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        if(indexPath.section == 1){
            if(indexPath.row == 0){
                //Ver pacientes selected
                let main : UIStoryboard = self.storyboard!
                let controller : PacientesTableViewController =  main.instantiateViewControllerWithIdentifier("PacientesTableViewController") as! PacientesTableViewController
                self.navigationController?.pushViewController(controller, animated: true)
            }else if (indexPath.row == 1){
                //Ver episodios selected
                
            }
        }
        
        println("This is the row: \(indexPath.row) for the section: \(indexPath.section)")
    }
    
    /*
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
    // Return NO if you do not want the specified item to be editable.
    return true
    }
    */
    
    /*
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
    if editingStyle == .Delete {
    // Delete the row from the data source
    tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
    } else if editingStyle == .Insert {
    // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }
    }
    */
    
    /*
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {
    
    }
    */
    
    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
    // Return NO if you do not want the item to be re-orderable.
    return true
    }
    */
    
    /*
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    }
    */
    
    override func tableView(tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if(section == 0){
            return 0.0
        }else{
            return 36.0
        }
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if(indexPath.section == 0){
            return 85.0
        }else{
            return 44.0
        }
    }
    
    override func tableView(tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if(section == 0){
            return nil
        }else{
            var lineD : UIView = UIView(frame: CGRect(x: 0.0, y: 35.0, width: 375.0, height: 1.0))
            var lineU : UIView = UIView(frame: CGRect(x: 0.0, y: 0.0, width: 375.0, height: 1.0))
            lineD.backgroundColor = UIColor(red: 235.0/255, green: 235.0/255, blue: 235.0/255, alpha: 1.0)
            lineU.backgroundColor = UIColor(red: 235.0/255, green: 235.0/255, blue: 235.0/255, alpha: 1.0)
            var view : UIView = UIView(frame: CGRect(x: 0.0, y: 0.0, width: 375.0, height: 50.0))
            view.addSubview(lineD)
            view.addSubview(lineU)
            view.backgroundColor = UIColor(red: 240.0/255, green: 240.0/255, blue: 240.0/255, alpha: 1.0)
            return view
        }
    }
    
}
