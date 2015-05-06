//
//  ViewController.swift
//  PacienteMobile
//
//  Created by Mario Hernandez on 14/03/15.
//  Copyright (c) 2015 Equinox. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    struct MyVariables {
        static var usuario : NSDictionary = ["":""]
        static var token : String = ""
    }
    
    @IBOutlet weak var usuarioText: UITextField!
    
    @IBOutlet weak var claveText: UITextField!

    @IBOutlet weak var errorText: UILabel!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
     func logear() -> NSDictionary{
        
      var  con: Connector = Connector()
        
        let result =  con.doPost("/usuario/autenticar", dict: ["email" : usuarioText.text , "password" : claveText.text]) as NSDictionary
        
        sleep(2)
        
        return result
        
    }
    

    
    override func shouldPerformSegueWithIdentifier(identifier: String?, sender: AnyObject?) -> Bool {

        var data : NSDictionary? = logear()
        
        print(data)

        
        
       if (data != nil && data?["rol"]! as! NSString == "Paciente"){
        
        MyVariables.usuario = data!
        
        let global_settings = NSUserDefaults.standardUserDefaults()
        let idd = data?["id"] as! Int
        let tok = data?["token"] as! String
        global_settings.setValue("\(idd)", forKey: "DOCTOR_ID")
        global_settings.setValue(tok, forKey: "TOKEN")
        
        global_settings.synchronize()
      
            return true
        }else{
        
         self.errorText.hidden = false
           return false
    }
    }
    
  
    
    
}

