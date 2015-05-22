//
//  EpisodiosViewController.swift
//  PacienteMobile
//
//  Created by Mario Hernandez on 16/03/15.
//  Copyright (c) 2015 Equinox. All rights reserved.
//

import UIKit

class EpisodiosViewController: UIViewController , UITableViewDelegate,UITableViewDataSource {
    
    
    
    
    var items : NSArray = []
    
    var idx = ViewController.MyVariables.usuario["id"] as! NSInteger
    

    
    
    @IBOutlet weak var tableView: UITableView!
    
    
    @IBAction func cerrarSesión(sender: UIBarButtonItem) {
        
        self.dismissViewControllerAnimated(false, completion: nil)
        println("se cerró sesión")
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        // var x = ViewController.MyVariables.usuario["nombre"] as! NSString
        
        self.title = "Episodios"
        
        
        //var idx = ViewController.MyVariables.usuario["id"] as! NSInteger
        
        
        
        var con = Connector()

        
        items = con.doGet("/paciente/\(idx)/episodio")
        
   //     print(items)
        
        // Do any additional setup after loading the view, typically from a nib.
        
        tableView.registerClass(EpisodioCell.self, forCellReuseIdentifier: "cella")
        tableView.dataSource = self
        tableView.delegate = self
        
        //println(items)
    }
    
    override func viewWillAppear(animated: Bool) {
        //println("Voy a aparecer ***********")
        if(ViewController.MyVariables.creado){
           ViewController.MyVariables.creado = false
           
            var con = Connector()
            items = con.doGet("/paciente/\(idx)/episodio")
            self.tableView.reloadData()
        }
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return items.count;
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell  = tableView.dequeueReusableCellWithIdentifier("superCell",forIndexPath: indexPath) as! EpisodioCell
        
        
        
        
        var x : NSDictionary = items[indexPath.row] as! NSDictionary
        
        var c = x["fecha"] as! String
        
        cell.fechaText?.text = c
        
        let h = x["id"] as! Int
        cell.idText?.text = "\(h)"
        
        return cell
        
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        
        let cell =  tableView.cellForRowAtIndexPath(indexPath) as! EpisodioCell
        
        //Ver pacientes selected
        let main : UIStoryboard = self.storyboard!
        let controller : EpisodioDetail =  main.instantiateViewControllerWithIdentifier("episodioDetail") as! EpisodioDetail
       // controller.localizacion = cell.localizacion
       // controller.nivelDolor = cell.nivelDolor
       // controller.fecha = items.objectAtIndex(indexPath.row)["fecha"] as! String
        controller.episodio = items.objectAtIndex(indexPath.row) as! NSDictionary
        self.navigationController?.pushViewController(controller, animated: true)

    }
    
    
    func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
      //  println("HOlaaa")
        
        let con = Connector()
        var epi = items[indexPath.row]["id"] as! Int
        con.doDelete("/paciente/\(idx)/episodio/\(epi)")

        sleep(1)
        items = con.doGet("/paciente/\(idx)/episodio")
        
        tableView.reloadData()
        
     //   tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: UITableViewRowAnimation.Fade)
    }
    

    
}