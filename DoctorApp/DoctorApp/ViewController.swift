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
        
        let result : NSDictionary = connector.doPost("/usuario/autenticar", dict: ["email" : usr , "password" : pass]) as NSDictionary
        
        if (result.count > 1){
            let idDoc : Int = result.valueForKey("id") as! Int
            let token : String = result.valueForKey("token") as! String
            
            let global_settings = NSUserDefaults.standardUserDefaults()
            global_settings.setValue("\(idDoc)", forKey: "DOCTOR_ID")
            global_settings.setValue(token, forKey: "TOKEN")
            
            global_settings.synchronize()
            
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

