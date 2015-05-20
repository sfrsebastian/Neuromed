//
//  NuevoEpisodio1ViewController.swift
//  PacienteMobile
//
//  Created by Mario Hernandez on 16/05/15.
//  Copyright (c) 2015 Equinox. All rights reserved.
//

import UIKit

class NuevoEpisodio1ViewController : UIViewController {
    
    @IBOutlet weak var dolorLabel: UILabel!
    
    @IBOutlet weak var stepper: UIStepper!
    
   
    @IBAction func changed() {
        dolorLabel.text = Int(stepper.value).description
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        ViewController.MyVariables.causas.removeAllObjects()
        ViewController.MyVariables.medicamentos.removeAllObjects()
        
        stepper.wraps = true
        stepper.autorepeat = true
        stepper.maximumValue = 10
        stepper.minimumValue = 1
    }
    
    
    
    
    
    func mostrarTableView(tipo : String){
        let main : UIStoryboard = self.storyboard!
        let controller : ItemsTableViewController =  main.instantiateViewControllerWithIdentifier("items") as! ItemsTableViewController
        controller.tipo = tipo
        
        self.navigationController?.pushViewController(controller, animated: true)

    }
    
    
    @IBAction func escogerMedicamentos() {
        mostrarTableView("medicamento")
    }
    
    
    @IBAction func escogerCausas() {
        mostrarTableView("causa")
    }
    
    
    @IBAction func escogerPatrones() {
        mostrarTableView("intervalo")
    }
    
    
    @IBAction func siguiente() {
        
        var d = dolorLabel.text?.toInt()
        ViewController.MyVariables.nivelDolor = d!
    }

    
    
    
}
