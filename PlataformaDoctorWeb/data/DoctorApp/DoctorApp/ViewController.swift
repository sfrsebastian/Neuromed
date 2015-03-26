//
//  ViewController.swift
//  DoctorApp
//
//  Created by Felipe Macbook Pro on 3/16/15.
//  Copyright (c) 2015 Felipe-Otalora. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var txtUsuario : UITextField?
    @IBOutlet weak var txtPass : UITextField?

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func shouldPerformSegueWithIdentifier(identifier: String?, sender: AnyObject?) -> Bool {
        let connector : Connector = Connector()
        
        let usr : String = txtUsuario!.text
        let pass : String = txtPass!.text
        
        var result : NSDictionary = connector.doPost("/usuario/autenticar", dict: ["email" : usr , "password" : pass])
        
        if (result.count > 1){
            let global_settings = NSUserDefaults.standardUserDefaults()
            global_settings.setValue(result, forKey: "DOCTOR")
            
            
            return true
        }else{
            return false
        }
    }

//    @IBAction func doLogin(sender: UIButton) {
//        var txt = txtUsuario?.text
//        println("Esto dice: \(txt)")
//        
//        connector.doGet("/")
//    }
}

